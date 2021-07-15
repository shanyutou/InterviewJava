package com.interviewdemo;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程操纵资源类
 */
class MyResource{

    private int number = 1;//标记位置
    private Lock lock = new ReentrantLock();
    private Condition c1 = lock.newCondition();
    private Condition c2 = lock.newCondition();
    private Condition c3 = lock.newCondition();

    //指定打印次数
    public void print(int count){
        for (int i = 0; i < count; i++) {
            System.out.println(Thread.currentThread().getName() + i);
        }
    }

    public void action(int count){
        lock.lock();
        try {
            if (count == 5){
                if (number != 1){
                    c1.await();
                }
            }else if (count == 10) {
                if (number != 2){
                    c2.await();
                }
            } else {
                if (number != 3){
                    c3.await();
                }
            }

            print(count);

            if (count == 5){
                number = 2;
                c2.signalAll();
            }else if (count == 10) {
                number = 3;
                c3.signalAll();
            } else {
                number = 1;
                c1.signalAll();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }




}

public class LockAndConditionDemo {

    public static void main(String[] args) {
        MyResource resource = new MyResource();

        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                resource.action(5);
            }
        }).start();
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                resource.action(10);
            }
        }).start();
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                resource.action(15);
            }
        }).start();
    }

}
