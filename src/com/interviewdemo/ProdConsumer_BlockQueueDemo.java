package com.interviewdemo;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 资源操纵类
 */
class MyResource_ProdConsumer_BlockQueueDemo{
    private volatile boolean FLAG = true;//默认开启，进行生产+消费
    private AtomicInteger atomicInteger = new AtomicInteger();

    BlockingQueue<String> blockingQueueQueue = null;

    public MyResource_ProdConsumer_BlockQueueDemo(BlockingQueue<String> blockingQueueQueue) {
        this.blockingQueueQueue = blockingQueueQueue;
        System.out.println(this.blockingQueueQueue.getClass().getName());
    }

    /**
     * 生产
     * @throws Exception
     */
    public void myProd()throws Exception {
        String  data = null;
        boolean retValue;
        while (FLAG) {
            data = atomicInteger.incrementAndGet() + "";
            TimeUnit.SECONDS.sleep(1);
            retValue = blockingQueueQueue.offer(data, 2L, TimeUnit.SECONDS);
            if (retValue){
                System.out.println(Thread.currentThread().getName() + "\t 插入队列 "+data+" 成功");
            }else {
                System.out.println(Thread.currentThread().getName() + "\t 插入队列 "+data+" 失败");

            }
        }
        System.out.println(Thread.currentThread().getName() + "\t main叫停了服务。。。。Flag=false,生产结束");
    }

    /**
     * 消费
     * @throws Exception
     */
    public void myConsume() throws Exception {
        while (FLAG) {
            String data = blockingQueueQueue.poll(2L, TimeUnit.SECONDS);
            if (data == null || data.equalsIgnoreCase("")){
                System.out.println(Thread.currentThread().getName() + "\t 消费失败，生产服务停了");
            }
            System.out.println(Thread.currentThread().getName() + "\t 消费 "+data+" 成功");
        }
        System.out.println(Thread.currentThread().getName() + "\t main叫停了服务。。。。Flag=false,消费结束");
    }

    public void stop() throws Exception {
        this.FLAG = false;
    }


}

/**
 * volatile/cas/atomicInteger/BlockQueue/线程交互/原子引用
 */
public class ProdConsumer_BlockQueueDemo {

    public static void main(String[] args) {
        ArrayBlockingQueue<String> listBlockingQueue = new ArrayBlockingQueue<String>(8);
        MyResource_ProdConsumer_BlockQueueDemo resource_Producer_BlockingQue = new MyResource_ProdConsumer_BlockQueueDemo(listBlockingQueue);
        new Thread(()->{
            try {
                resource_Producer_BlockingQue.myProd();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(()->{
            try {
                resource_Producer_BlockingQue.myConsume();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        try {
            TimeUnit.SECONDS.sleep(5);
            resource_Producer_BlockingQue.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
