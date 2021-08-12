package com.interviewdemo;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * java 自带的监控工具VisualVM
 * VisualVM 是一款免费的，集成了多个 JDK 命令行工具的可视化工具，它能为您提供强大的分析能力，对 Java 应用程序做性能分析和调优。
 * 这些功能包括生成和分析海量数据、跟踪内存泄漏、监控垃圾回收器、执行内存和 CPU 分析，同时它还支持在 MBeans 上进行浏览和操作。
 * 准备工作
 * 自从 JDK 6 Update 7 以后已经作为 Oracle JDK 的一部分，位于 JDK 根目录的 bin 文件夹下，无需安装，直接运行即可。
 *
 *  内存分析篇
 *
 * VisualVM 通过检测 JVM 中加载的类和对象信息等帮助我们分析内存使用情况，我们可以通过 VisualVM 的监视标签对应用程序进行内存分析。
 *
 * 1）内存堆Heap
 * 首先我们来看内存堆Heap使用情况，我本机eclipse的进程在visualVM显示如下：
 *
 *
 */


class TestPerm{
    public final static int OUTOFMEMORY = 200000000;

    private String oom;

    private int length;

    StringBuffer tempOOM = new StringBuffer();

    public TestPerm(int leng) {
        this.length = leng;

        int i = 0;
        while (i < leng) {
            i++;
            try {
                tempOOM.append("a");
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                break;
            }
        }
        this.oom = tempOOM.toString();

    }

    public String getOom() {
        return oom;
    }

    public int getLength() {
        return length;
    }
}

class TestMateSpace {

    private static List<Object> insList = new ArrayList<Object>();

    public static void permLeak() throws Exception {
    for (int i = 0; i < 1000; i++) {
            URL[] urls = getURLS();
            URLClassLoader urlClassloader = new URLClassLoader(urls, null);
            Class<?> logfClass = Class.forName("org.apache.commons.logging.LogFactory", true,urlClassloader);
            Method getLog = logfClass.getMethod("getLog", String.class);
            Object result = getLog.invoke(logfClass, "TestPermGen");
            insList.add(result);
            System.out.println(i + ": " + result);
        }
}

private static URL[] getURLS() throws MalformedURLException {
    File libDir = new File("E:/dev/maven/repository/commons-logging/commons-logging/1.1.1");
    File[] subFiles = libDir.listFiles();
    int count = subFiles.length;
    URL[] urls = new URL[count];
    for (int i = 0; i < count; i++) {
            urls[i] = subFiles[i].toURI().toURL();
        }
    return urls;
}
}

class TestCPUDemo {
    public static void main(String[] args) throws InterruptedException {

        cpuFix();
    }


    /**
     * cpu 运行固定百分比
     *
     * @throws InterruptedException
     */
    public static void cpuFix() throws InterruptedException {
        // 80%的占有率
        int busyTime = 8;
        // 20%的占有率
        int idelTime = 2;
        // 开始时间
        long startTime = 0;

        while (true) {
            // 开始时间
            startTime = System.currentTimeMillis();

            /*
             * 运行时间
             */
            while (System.currentTimeMillis() - startTime < busyTime) {
                ;
            }

            // 休息时间
            Thread.sleep(idelTime);
        }
    }
}


class TestThread extends Thread {

    public TestThread(String name) {
    }

    @Override
    public void run() {
        while (true) {

        }
    }
}

class Resource {
    private int i;

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

}


class LockThread1 extends Thread {
    private Resource r1, r2;

    public LockThread1(Resource r1, Resource r2) {
        this.r1 = r1;
        this.r2 = r2;
    }

    @Override
    public void run() {
        int j = 0;
        while (true) {
            synchronized (r1) {
                System.out.println("The first thread got r1's lock " + j);
                synchronized (r2) {
                    System.out.println("The first thread got r2's lock  " + j);
                }
            }
            j++;
        }
    }

}

class LockThread0 extends Thread {
    private Resource r1, r2;

    public LockThread0(Resource r1, Resource r2) {
        this.r1 = r1;
        this.r2 = r2;
    }

    @Override
    public void run() {
        int j = 0;
        while (true) {
            synchronized (r2) {
                System.out.println("The second thread got r2's lock  " + j);
                synchronized (r1) {
                    System.out.println("The second thread got r1's lock" + j);
                }
            }
            j++;
        }
    }

}

class DeadLock {

    public static void test(){
        Resource r1 = new Resource();
        Resource r0 = new Resource();

        Thread myTh1 = new LockThread1(r1, r0);
        Thread myTh0 = new LockThread0(r1, r0);

        myTh1.setName("DeadLock-1 ");
        myTh0.setName("DeadLock-0 ");

        myTh1.start();
        myTh0.start();
    }

}

public class VisualVMDemo {



    public static void main(String[] args) throws Exception {
        /**
         * 堆空间测试
         */
//        TimeUnit.SECONDS.sleep(20);
//        TestPerm testPerm = new TestPerm(TestPerm.OUTOFMEMORY);
//        System.out.println(testPerm.getOom().length());
//        TimeUnit.SECONDS.sleep(1000);

        /**
         * 元空间测试
         */
//        TimeUnit.SECONDS.sleep(20);
//        TestMateSpace.permLeak();
//        TimeUnit.SECONDS.sleep(1000);

        /**
         * cpu 测试
         */
//        TimeUnit.SECONDS.sleep(20);
//        TestCPUDemo.cpuFix();
//        TimeUnit.SECONDS.sleep(1000);

        /**
         * 线程 测试
         */
//        TimeUnit.SECONDS.sleep(20);
//
//        TestThread mt1 = new TestThread("Thread a");
//        TestThread mt2 = new TestThread("Thread b");
//
//        mt1.setName("My-Thread-1 ");
//        mt2.setName("My-Thread-2 ");
//
//        mt1.start();
//        mt2.start();
//
//        TimeUnit.SECONDS.sleep(1000);


        /**
         * 死锁 测试
         */
        TimeUnit.SECONDS.sleep(20);
        DeadLock.test();
        TimeUnit.SECONDS.sleep(1000);

    }


}
