package org.yuanrui.sync;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

public class PhaserTiming {

    public static long time(Executor executor, int concurrency, Runnable action) throws InterruptedException {
        Phaser phaser = new Phaser(concurrency + 1); // 主线程 + 并发线程数

        for (int i = 0; i < concurrency; i++) {
            executor.execute(() -> {
                phaser.arriveAndAwaitAdvance(); // 表示线程已准备好，等待其他线程
                try {
                    action.run();
                } finally {
                    phaser.arriveAndDeregister(); // 表示任务完成，注销该线程
                }
            });
        }

        // 主线程也作为一个参与者，等待所有工作线程准备好
        phaser.arriveAndAwaitAdvance();
        long startNanos = System.nanoTime();

        // 主线程等待所有工作线程完成任务
        phaser.arriveAndAwaitAdvance();
        return System.nanoTime() - startNanos;
    }

    public static void main(String[] args) throws InterruptedException {
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
