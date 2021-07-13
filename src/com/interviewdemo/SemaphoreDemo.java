package com.interviewdemo;

import java.net.BindException;
import java.util.concurrent.Semaphore;

public class SemaphoreDemo {

    public static void main(String[] args) {

        Semaphore semaphore = new Semaphore(3);

        for (int i = 0; i < 6; i++) {
            final int index = i;
            new Thread(()->{
                try {
                    semaphore.acquire();
                    System.out.println("" + index + "进入停车场");
                    Thread.sleep(1000);
                    System.out.println("" + index + "离开停车场");
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }


    }

}
