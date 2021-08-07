package com.interviewdemo;

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
 * Young Gen: Serial Copying（Serial）、 Prrallel Scavenge（Parallel）、 ParNew（  UserParNewGC  只在新生区用）   G1
 *
 *
 * Old Gen：  Serial MCS（UserSerialOldGC）、   Parallel Compacting（ UserParallelOldGC）、  CMS(ConcMarkSweepGC)    G1
 *
 *
 * STW  stop the word
 *
 * 穿行收集器：只使用一个线程进行垃圾回收，回收时会暂停用户线程；最古老、最稳定、以及高效的收集器，可能会产生较长时间停顿（Stop-The-World），
 *         对于单核CPU环境来说，没有线程交互的操作，可以获取最高的单线程垃圾回收效率，因此 Serial垃圾回收器，依然是java虚拟机运行在Client模式下默认的新生代垃圾收集器；
 *         对应参数：-XX:+UserSerialGC
 *         开启后会使用：Serial(Young区用) + Serial Old(Old区用)的手机器组合
 *         表示：新生代、老年代都会使用串性回收收集器，新生代使用复制算法、老年代使用标记-整理算法
 *
 */

public class GCDemo {

    public static void main(String[] args) {

    }



}
