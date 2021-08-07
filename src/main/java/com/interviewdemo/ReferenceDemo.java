package com.interviewdemo;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public class ReferenceDemo {

    public static void main(String[] args) throws InterruptedException {
//        reference();
//        softRef_menory_enough();
//        softRef_menory_noEnough();
//        weakRef();
//        myReferenceQueue();
        phantomRef();
    }

    private static void reference() {
        Object obj1 = new Object();
        Object obj2 = obj1;
        obj1 = null;
        System.gc();
        /**
         * obj2 强引用
         * 不会被GC
         * */
        System.out.println(obj2);
    }

    /**
     * 软引用，内存够用
     */
    private static void softRef_menory_enough() {
        Object obj1 = new Object();
        SoftReference<Object> softReference = new SoftReference<Object>(obj1);
        System.out.println(obj1);
        System.out.println(softReference.get());
        obj1 = null;
        System.gc();
        System.out.println(obj1);
        System.out.println(softReference.get());
    }

    /**
     * 软引用，内存不够用，回收
     */
    private static void softRef_menory_noEnough() {
        Object obj1 = new Object();
        SoftReference<Object> softReference = new SoftReference<Object>(obj1);
        System.out.println(obj1);
        System.out.println(softReference.get());
        obj1 = null;
        try {
//            byte[] bytes = new byte[50 *1024*1024];
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            System.out.println(obj1);
            System.out.println(softReference.get());
        }
    }


    /**
     * 弱引用，内存不管够不够用，回收
     */
    private static void weakRef() {
        Object obj1 = new Object();
        WeakReference<Object> ref = new WeakReference<>(obj1);
        System.out.println(obj1);
        System.out.println(ref.get());
        obj1 = null;
        System.gc();
        System.out.println(obj1);
        System.out.println(ref.get());
    }

    /**
     * 引用队列,在被回收时，会放入引用队列一次；
     */
    private static void myReferenceQueue() throws InterruptedException {
        Object obj1 = new Object();
        ReferenceQueue referenceQueue = new ReferenceQueue();
        WeakReference<Object> ref = new WeakReference<>(obj1, referenceQueue);
        System.out.println(obj1);
        System.out.println(ref.get());
        System.out.println(referenceQueue.poll());
        System.out.println("+++++++++++++++++");
        obj1 = null;
        System.gc();
        Thread.sleep(500);
        System.out.println(obj1);
        System.out.println(ref.get());
        System.out.println(referenceQueue.poll());
    }

    /**
     * 虚引用，形同虚设；
     */
    private static void phantomRef() throws InterruptedException {
        Object obj1 = new Object();
        ReferenceQueue referenceQueue = new ReferenceQueue();
        PhantomReference<Object> ref = new PhantomReference<>(obj1, referenceQueue);
        System.out.println(obj1);
        System.out.println(ref.get());
        System.out.println(referenceQueue.poll());
        System.out.println("+++++++++++++++++");
        obj1 = null;
        System.gc();
        Thread.sleep(500);
        System.out.println(obj1);
        System.out.println(ref.get());
        System.out.println(referenceQueue.poll());
    }



}
