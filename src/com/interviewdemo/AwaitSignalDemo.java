package com.interviewdemo;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * wait一般用于Synchronized中，而await只能用于ReentrantLock锁中
 */

class MySocial {

    private int number  = 0;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void produce(){

        try {
            lock.lock();
            while (number != 0){
                /**
                 * wait一般用于Synchronized中，而await只能用于ReentrantLock锁中
                 */
                condition.await();
            }
            Thread.sleep(1000);
            number++;
            System.out.println("生产" + number);
            condition.signalAll();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void consume(){
        lock.lock();
        try {
            lock.lock();
            while (number == 0){
                condition.await();
            }
            Thread.sleep(1000);
            number--;
            System.out.println("消费" + number);
            condition.signalAll();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }



}


public class AwaitSignalDemo {
    public static void main(String[] args) {
        MySocial social = new MySocial();

        new Thread(()->{
            for (int i = 0; i <6; i++) {
                social.consume();
            }
        }).start();

        new Thread(()->{
            for (int i = 0; i < 6; i++) {
                social.produce();
            }
        }).start();
    }
}
