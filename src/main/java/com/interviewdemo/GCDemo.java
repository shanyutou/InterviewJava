package com.interviewdemo;

import java.util.Random;

/**
 * GC 垃圾回收的四种算法：（引用计数、复制拷贝、标记清除、标记整理）
 * CC算法的落地就是《垃圾回收器》
 * Serial(串行) 只使用一个线程进行垃圾回收，回收时会暂停用户线程；最古老、最稳定、以及高效的收集器
 * Parallel(并行) 多个垃圾回收线程，回收时暂停用户线程，停顿时间比穿行短，性能好，适合科学计算；
 * CMS( ConcMarkSweepGC 并发标记清除)  用户线程和垃圾回收线程同时执行，（不一定并行，可以交替），不用停顿用户线程、适合互联网公司，对响应时间有要求；
 * G1   G1垃圾回收器，将堆内存分割成不同的区域，然后并发的对其进行垃圾回收。
 *
 * 另外几种垃圾收集器
 * UserParNewGC
 * UserParallelOldGC
 * UserSerialOldGC
 *
 * 查看默认垃圾收集器方法：java -XX:+PrintCommandLineFlags -version  默认是  -XX:+UseParallelGC
 * 设置使用哪种垃圾回收期 -XX:+UserConcMarkSweepGC
 *
 * Young Gen: Serial Copying（Serial）、  ParNew（  UserParNewGC  只在新生区用）、 Prrallel Scavenge（Parallel）  G1
 *
 *
 * Old Gen：  Serial MCS（UserSerialOldGC）、   Parallel Compacting（ UserParallelOldGC）、  CMS(ConcMarkSweepGC)    G1
 *
 * 参数解释
 * STW  stop the word
 * DefNew       Default New Generation
 * Tenured      Old
 * ParNew       Paralled New Generation
 * PSYoungGen   ParaLLel Svavenge
 * ParOldGen    Paralled Old Generation
 *
 * ******新生代********
 *
 * 穿行收集器(Serial）：只使用一个线程进行垃圾回收，回收时会暂停用户线程；最古老、最稳定、以及高效的收集器，可能会产生较长时间停顿（Stop-The-World），
 *         对于单核CPU环境来说，没有线程交互的操作，可以获取最高的单线程垃圾回收效率，因此 Serial垃圾回收器，依然是java虚拟机运行在Client模式下默认的新生代垃圾收集器；
 *         对应参数：-XX:+UserSerialGC
 *         开启后会使用：Serial(Young区用) + Serial Old(Old区用)的手机器组合
 *         表示：新生代、老年代都会使用串性回收收集器，新生代使用复制算法、老年代使用标记-整理算法
 *         参数：-Xms10m -Xmx10m -XXPrintGCDetails -XXPrintCommandLineFlags -XX:+UserSerialGC
 *
 * ParNew(并行)收集器  使用多线程进行垃圾回收，在垃圾收集时，会Stop the World 暂停其它所有的工作线程直到他收集结束。
 *          ParNew收集器其实就是Serial收集器新生代的并行多线程版本，其余行为和Serial收集器完全一样，最常见的应用场景是配合老年代的CMS GC 工作，
 *          ParNew垃圾收集器在垃圾收集过程中同样要暂停其它的工作线程。它是很多java虚拟机运行在Server模式下新生代的默认垃圾收集器。
 *          常用JVM参数：-XX:+UseParNewGC 启用ParNew收集器，只影响新生代的收集，不影响老年代，开启上述参数后，会使用ParNew(Young区) + Serial Old的收集器组合， 新生代使用复制算法，老年代采用标记整理算法
 *          但是 ParNew + Tenured 这样的搭配在java8中已经不再被推荐
 *          参数：-Xms10m -Xmx10m -XXPrintGCDetails -XXPrintCommandLineFlags -XX:+UseParNewGC
 *          备注： -XX:ParallelGCThReads 限制线程数量，默认开启和CPU数目相同的线程数
 *
 *
 * Prrallel Scavenge（Parallel）收集器类似ParNew也是一个新生代垃圾收集器，使用复制算法，也是一个并行的多线程收集器，俗称吞吐量优先收集器 一句话：串行收集器的新生代和老年代的并行化；在垃圾收集时，会Stop the World 暂停其它所有的工作线程直到他收集结束。
 *          它重点的关注：可控制的吞吐量（Thoughput=运行用户代码时间/(运行用户代码时间 + 垃圾收集时间)）高吞吐量意味着高效利用CPU的时间，它，多用于在后台运算而不需要太多交互的任务。
 *          自适应调节策略也是ParallelScacenge收集器与ParNews收集器的一个重要区别。（自适应调节策略：虚拟机会根据当前系统的运行情况收集性能监控信息，动态调整这些参数已提供最合适的停顿时间（-XX:MaxGCPauseMillis）或最大的吞吐量）
 *          常用JVM参数：-XX:+UseParallelGC 或-XX:+UseParallelOldGC(可相互激活) 使用Parallel Scanvenge收集器
 *          开启该参数后：新生代使用复制算法，老年代使用标记-整理算法
 *
 *          参数：-Xms10m -Xmx10m -XXPrintGCDetails -XXPrintCommandLineFlags -XX:+UseParallelGC
 *          备注： -XX:ParallelGCThReads=N 表示启动多少个线程数，默认开启和CPU数目相同的线程数， cpu>8  N= 5/8   cpu<8  N= 实际个数
 *
 *
 * ******老年代代********
 *
 * Parallel Compacting（ UserParallelOldGC） 是 Parallel Scavaenge的老年代版本，使用多线程的标记-整理算法，Parallel Old收集器在JDK1.6才开始提供。
 *          JDK1.6之前，Parallel Scavaenge收集器只能搭配老年代的Serial Old收集器，只能保证新生代的吞吐量优先，无法保证整体的吞吐量。在JDK1.6之前（Parallel Scavenge + Serial Old）
 *          Parallel Old 正是为了在老年代提供同样吞吐量优先的垃圾收集器，如果系统对吞吐量要求比较高，JDK1.8后可以优先考虑新生代Parallel Scavenge和老年代Parallel Old收集器的搭配策略。 在JDK 1.8及以后（Parallel Scavenge+Parallel Old）
 *           常用JVM参数：-XX:+UseParallelOldGC
 *           开启该参数后：新生代使用复制算法，老年代使用标记-整理算法
 *
 *           参数：-Xms10m -Xmx10m -XXPrintGCDetails -XXPrintCommandLineFlags -XX:+UseParallelGC
 *           备注： -XX:ParallelGCThReads=N 表示启动多少个线程数，默认开启和CPU数目相同的线程数， cpu>8  N= 5/8   cpu<8  N= 实际个数
 *
 *
 * CMS收集器（Concurrent Mark Sweep: 并发标记清除） 是一种以获取最短回收停顿时间为目标的收集器；并发收集停顿低，并发指的是与用户线程一起执行
 *          适合应用在互联网网站或者B/S系统的服务器上，这类应用尤其重视服务器的响应速度，希望系统的停顿时间最短。
 *          CMS非常适合堆内存大、CPU核数多的服务器端应用，也是G1出现前大型应用的首选收集器
 *          参数：-XX:_UseConcMarkSweepGC  开启该参数，自动会打开 -XX:UseParNewGC打开；
 *          开启该参数，使用Parnew（Young区用）+ CMS（Old区用） + Seriial Old的收集器组合，Serial Old 将作为 CMS出错后的后备收集器
 *          优点：暂停时间短，响应快；
 *          缺点：占用CPU资源高；由于并发进行，CMS在收集与应用线程会同时增加对堆内存的占用，也就是说，CMS必须要在老年代堆内存用尽之前完成垃圾回收，否则CMS回收失败时，将触发担保机制，串行老年代收集器，将会以STW的方式进行一次GC,从而造成较大时间停顿；
 *               标记清除算法无法整理空间碎片，
 *          参数：-Xms10m -Xmx10m -XX:+PrintGCDetails -XXPrintCommandLineFlags -XX:+UseConcMarkSweepGC
 *
 *
 * 老年区串行收集器(SerialOld）：只使用一个线程进行垃圾回收，回收时会暂停用户线程；最古老、最稳定、以及高效的收集器，可能会产生较长时间停顿（Stop-The-World），
 *
 *
 * G1 参数：-Xms10m -Xmx10m -XX:+PrintGCDetails -XX:+PrintCommandLineFlags -XX:+UseG1GC
 *      特点：适合多处理器和大容量内存环境，在实现高吞吐量的同时，尽可能芒祖垃圾手机暂停时间的要求。
 *          像CMS收集器一样，能与应用程序线程并发执行。
 *          整理空闲线程更快。
 *          需要更多时间来预测GC停顿时间。
 *          不希望牺牲大量的吞吐性能；
 *          不需要更大的java Heap；
 *       设计目标是取代CMS收集器，相比CMS：
 *          G1是一个又内存整理过程的垃圾收集器，不会产生很多内存碎片；
 *          G1的stop the world更可控，G1在停顿时间上添加了预测机制，用户可以指定期望停顿时间。
 *
 *       主要的改变是Eden，Survivor、Tenured等内存不再是连续区域了，而变成一个个大小一样的regin；每个regin从1M到32M不等。
 *       G1尽可能压缩STW时间，
 *       整体采用标记-整理算法，局部使用复制算法，不会产生内存。
 *       G1不再区分年轻代、老年代，存不再是连续区域了，而变成一个个大小一样的regin；每个regin从1M到32M不等。
 *       G1其本省依然在小范围内进行年轻代、老年代的区分，保留新生代、老年代，但不再是物理隔离，而是一部分Regin集合，并且不需要连续，也就是说依然会采用不同的GC方式处理不同的区域。
 *       G1虽然也是分代收集器，但整个内存分区不存在物理上的年轻代、老年代，也不需要完全独立的survivor堆做复制准备。G1只用逻辑上的分代概念，或者说每个分区都可能随G1的运行在不同代之间前后切换；
 *
 *
 *
 *
 *
 */

public class GCDemo {

    public static void main(String[] args) {
        System.out.println("******GCDEMO  hello******");
        try {
            String str = "atguigu";
            while (true){
                str += str + new Random().nextInt(555555) + new Random().nextInt(8888888);
                str.intern();
            }

        }catch (Throwable e) {
            e.printStackTrace();
        }
    }



}
