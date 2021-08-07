package com.interviewdemo;

import java.util.concurrent.*;

public class MyThreadPoolDemo {

    public static void main(String[] args) {

        ExecutorService executorService = new ThreadPoolExecutor(2,
                5,//CPU密集型：cpu核数 + 1； IO密集型：CPU核心数 * 2
                1L,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(3),
                Executors.defaultThreadFactory(),
/**
 * 四种拒绝策略，产生不同效果；
 */
//                new ThreadPoolExecutor.AbortPolicy()
                new ThreadPoolExecutor.DiscardOldestPolicy()
//                new ThreadPoolExecutor.CallerRunsPolicy()
//                new ThreadPoolExecutor.DiscardPolicy()
                );

        try {
            for (int i = 0; i < 10; i++){
                final int index = i;
                executorService.execute(()->{
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + "办理业务" + index);
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }


    }

}
