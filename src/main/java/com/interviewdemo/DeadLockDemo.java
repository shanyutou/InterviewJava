package com.interviewdemo;

import sun.plugin2.main.server.HeartbeatThread;

import java.util.concurrent.TimeUnit;

class HoldLockThread implements Runnable {

    private String lockA;
    private String lockB;

    public HoldLockThread(String lockA, String lockB){
        this.lockA = lockA;
        this.lockB = lockB;
    }


    @Override
    public void run() {
        synchronized (lockA) {
            System.out.println("自己持有：" + lockA + "尝试获取：" + lockB);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (lockB){
                System.out.println("自己持有：" + lockA + "尝试获取：" + lockB);
            }

        }
    }
}


public class DeadLockDemo {

    /**
     * jps -l 查询java线程号
     * jstack 线程号 查看线程死锁情况
     *
     * @param args
     */

    public static void main(String[] args) {
        String lockA = "lockA";
        String lockB = "lockB";

        new Thread(new HoldLockThread(lockA, lockB)).start();
        new Thread(new HoldLockThread(lockB, lockA)).start();

    }
}
