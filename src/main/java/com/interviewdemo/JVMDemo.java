package com.interviewdemo;

public class JVMDemo {

    public static void main(String[] args) throws InterruptedException {
        long totalMemory = Runtime.getRuntime().totalMemory();
        long maxMemory = Runtime.getRuntime().maxMemory();
        System.out.println("totoal(-Xms)" + totalMemory/1024/1024);
        System.out.println("totoal(-Xmx)" + maxMemory/1024/1024);
        System.out.println("runing jvm demo ....");
        //设置超过堆空间最大空间
        byte[] a = new byte[50 * 1024 * 1024];
//        Thread.sleep(Integer.MAX_VALUE);

        //Xms 初始化堆空间 默认 内存 1/64；
        //Xms 最大堆空间  defaull 1/4；
        //Xss 栈空间 default 1024k；
        //Xmm 年轻代；
        //-XX:MetaspaceSize 元空间代替7的老年代，直接位于物理内存中；
        //-PrintGCDetails
        //-XX:SurvivorRatio (年轻代 幸存区比例默认：8  则是 Eden:form:to 8：1：1)
        //-XX:NewRatio (年轻代、老年代比例，默认1：2， -XX:NewRatio=4  新生代1：老板代4)
    }

}
