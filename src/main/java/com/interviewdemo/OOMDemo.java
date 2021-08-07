package com.interviewdemo;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OOMDemo {

    public static void main(String[] args) {
//        stackOverflowError();
//        autoFoMemoryErrorJavaHeapSpase();
//        GCOverheadLimitExceded();
//        directBUfferMemory();
        /**
         * 该方法会耗尽操作系统线程数，不能在物理机中执行，要在虚拟机中测试
         */
//        unableToCreateNewNativeThread();


        metaspaceOOMTest();




    }


    /**
     * 栈空间溢出, 属于错误
     */
    public static void stackOverflowError () {
        stackOverflowError();
    }

    /**
     * -Xms10m -Xmx10m -XX:+PrintGCDetails  调小堆内存
     * Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
     */
    public static void autoFoMemoryErrorJavaHeapSpase() {
        String str = "atguigu";
        while (true){
            // Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
            str += str + new Random().nextInt(111111111) + new Random().nextInt(222222222);
            str.intern();
        }
    }

    /**
     * -Xms10m -Xmx10m -XX:MaxDirectMemorySize=5m -XX:+PrintGCDetails
     * java.lang.OutOfMemoryError: GC overhead limit exceeded
     */
    public static void GCOverheadLimitExceded() {

        int i = 0;
        List<String > list = new ArrayList<>();
        try {
            while (true){
                list.add(String.valueOf(++i).intern());
            }
        }catch (Throwable e) {
            System.out.println(i);
            e.printStackTrace();
        }

    }

    /**
     * -XX:MaxDirectMemorySize=5m -XX:+PrintGCDetails
     * Exception in thread "main" java.lang.OutOfMemoryError: Direct buffer memory
     */
    public static void directBUfferMemory(){
        System.out.println(sun.misc.VM.maxDirectMemory()/(double)1024/1024);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /**
         * 设置只有5M，但是申请了6M， Direct buffer memory （直接内存溢出）
         */
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(6*1024*1024);
    }

    /**
     * ulimit -a 查看最大文件描述数，则为最大线程数， 该方法只能在虚拟机中执行，不然会让操作系统崩溃；
     * unable To Create New Native Thread
     */
    public static void unableToCreateNewNativeThread(){
        for (int i = 0;  ;i++){
            System.out.println("****i = "+ i);
            new Thread(()->{
                try {
                    Thread.sleep(Integer.MAX_VALUE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    /**
     * -XX:MetaspaceSize=8m -XX:MaxMetaspaceSize=8m -XX:+PrintGCDetails
     *
     * 包含：虚拟机加载的类的信息、常量池、静态变量、及时编译后的代码
     *
     * 模拟Metaspace空间溢出，我们不断往元空间灌，类占用的空间就会超出
     */

    public static void metaspaceOOMTest(){

        int i = 0;
        try {
            /**
             * cglib 动态加载类，撑满元空间
              */
            while (true){
                i++;
                Enhancer enhancer = new Enhancer();
                enhancer.setSuperclass(OOMDemo.class);
                enhancer.setUseCache(false);
                enhancer.setCallback(new MethodInterceptor() {
                    @Override
                    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                        return method.invoke(o, objects);
                    }
                });
                enhancer.create();
            }
        }catch (Throwable e){
            System.out.println("多少次后发生异常：" + i);
            e.printStackTrace();
        }
    }

}


