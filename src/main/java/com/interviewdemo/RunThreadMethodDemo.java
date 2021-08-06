package com.interviewdemo;

import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.util.concurrent.ListenableFutureTask;

import java.util.concurrent.*;

/**
 * 运行线程方法演示
 *
 * @author Jiwei
 * @date 2021/07/27
 */

public class RunThreadMethodDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        Method1 method1= new Method1();
        method1.setName("method 1");
        method1.start();

        Thread thread2 = new Thread(new Method2(), "method 2");
        thread2.start();

        new Thread(()->{
            System.out.println(Thread.currentThread().getName() + "运行起来");
                }, "method 3").start();

        FutureTask<Integer> futureTask = new FutureTask<>(new Method4());
        Thread thread4 = new Thread(futureTask, "method 4");
        thread4.start();
        try {
            Integer result = futureTask.get();
            System.out.println("结果是：" + result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //Spring 框架中带的，支持回调
        ListenableFutureTask listenableFutureTask = new ListenableFutureTask(new Method4());
        listenableFutureTask.addCallback(new CallBack());
        Thread thread5 = new Thread(listenableFutureTask, "method 5");
        thread5.start();


        int POOL_NUM = 10;     //线程池数量
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < POOL_NUM; i++) {
            Method6 m = new Method6();
            Future future = executorService.submit(m);
            System.out.println("获取结果" + future.get());
        }
        executorService.shutdown();

    }

}


/**
 * 继承Method
 *
 * @author Jiwei
 * @date 2021/07/27
 */
class Method1 extends Thread{
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "运行起来");
    }
}


/**
 * 实现Runnable
 *
 * @author Jiwei
 * @date 2021/07/27
 */
class Method2 implements Runnable{
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "运行起来");
    }
}

/**
 * 实现Callable
 *
 * @author Jiwei
 * @date 2021/07/27
 */
class Method4 implements Callable<Integer>{

    @Override
    public Integer call() throws Exception {
        System.out.println(Thread.currentThread().getName() + "运行起来");
        return 8848;
    }
}

/**
 * Callable, 回调实现
 *
 * @author Jiwei
 * @date 2021/07/27
 */
class CallBack implements ListenableFutureCallback {

    @Override
    public void onFailure(Throwable throwable) {

    }

    @Override
    public void onSuccess(Object o) {
        System.out.println("获取结果 " + o);
    }
}

class Method6 implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        return 8849;
    }
}





