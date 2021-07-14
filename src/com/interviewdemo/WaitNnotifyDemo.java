package com.interviewdemo;

/**
 * 生产消费问题
 */

class ValueObject
{
    public static int value = 0;
}

class MyProducer{

    private Object lock;

    public MyProducer(Object lock){
        this.lock = lock;
    }

    public void setValue(){
        try {
            synchronized (lock){
                /**
                 * 此处如果使用if，则会在超过两个线程时候ValueObject.value !=0 wait 之后再次拿到锁时不一定value就是0，所以用while
                 */
                while (ValueObject.value != 0) {
                    lock.wait();
                }
                Thread.sleep(1000);
                ValueObject.value++;
                System.out.println("生产" + ValueObject.value);
                lock.notify();

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class MyConsumer{
    private Object lock;
    public MyConsumer(Object lock) {
        this.lock = lock;
    }

    public void getValue() {
        try {
            synchronized (lock) {
                while (ValueObject.value == 0) {
                    lock.wait();
                }
                Thread.sleep(1000);
                ValueObject.value--;
                System.out.println("消费" + ValueObject.value);
                lock.notify();

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}


public class WaitNnotifyDemo {

    public static void main(String[] args) {
        Object lock = new Object();
        MyProducer producer = new MyProducer(lock);
        MyConsumer consumer = new MyConsumer(lock);

        /**
         * 消费线程
         */
        new Thread(()->{

            for (int i = 0; i < 6; i++) {
                consumer.getValue();
            }
        }).start();
        /**
         * 生产线程
         */
        new Thread(()->{
            for (int i = 0; i < 6; i++) {
                producer.setValue();
            }
        }).start();

        /**
         * 生产线程
         */
        new Thread(()->{
            for (int i = 0; i < 6; i++) {
                producer.setValue();
            }
        }).start();
        /**
         * 消费线程
         */
        new Thread(()->{

            for (int i = 0; i < 6; i++) {
                consumer.getValue();
            }
        }).start();

    }

}
