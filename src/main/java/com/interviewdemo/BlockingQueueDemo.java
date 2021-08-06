package com.interviewdemo;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * add remove element 操作不正确时抛异常
 * offer poll  peek   操作不正确时返回true、false或者null
 * put  take    一直阻塞
 * offer poll 阻塞，在timeout之内
 */

class Producer implements Runnable {

    private final BlockingQueue queue;

    Producer(BlockingQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true){
                queue.put(produce());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    Object produce(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("生产了" + Thread.currentThread().getName());
        return Thread.currentThread().getName();
    }
}

class Consumer implements Runnable {

    private final BlockingQueue queue;

    Consumer(BlockingQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                consume(queue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    void consume(Object x) {
        System.out.println("消费了" + Thread.currentThread().getName());
    }
}

public class BlockingQueueDemo {

    public static void main(String[] args) {
        BlockingQueue queue = new LinkedBlockingQueue();
        Producer p = new Producer(queue);
        Consumer c1 = new Consumer(queue);
        Consumer c2 = new Consumer(queue);
        new Thread(p).start();
        new Thread(c1).start();
        new Thread(c2).start();
    }

}
