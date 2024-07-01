package org.yuanrui.sync;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CyclicBarrierTiming {

    public static long time(Executor executor, int concurrency, Runnable action) throws InterruptedException, BrokenBarrierException {
        CyclicBarrier barrier = new CyclicBarrier(concurrency + 1); // 加1是因为主线程也要参与屏障

        for (int i = 0; i < concurrency; i++) {
            executor.execute(() -> {
                try {
                    barrier.await(); // 等待所有线程都准备好
                    action.run();
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        barrier.await(); // 主线程等待所有工作线程准备好
        long startNanos = System.nanoTime();

        barrier.await(); // 第二次等待所有工作线程完成任务
        return System.nanoTime() - startNanos;
    }

    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
        int concurrency = 5;
        Executor executor = Executors.newFixedThreadPool(concurrency);
        Runnable action = () -> {
            // 模拟执行任务
            try {
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + " completed.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        long timeTaken = time(executor, concurrency, action);
        System.out.println("Total time: " + timeTaken + " nanoseconds");
    }
}
