package org.yuanrui.sync;

import java.util.LinkedList;
import java.util.Queue;

class ProducerConsumerExample {
    private final Queue<Integer> queue = new LinkedList<>();
    private final int CAPACITY = 5;

    public void produce() throws InterruptedException {
        int value = 0;
        while (true) {
            synchronized (this) {
                while (queue.size() == CAPACITY) {
                    System.out.println("Queue is full, producer is waiting");
                    wait();
                }
                System.out.println("Produced: " + value);
                queue.add(value++);
                notify();
                Thread.sleep(1000);
            }
        }
    }

    public void consume() throws InterruptedException {
        while (true) {
            synchronized (this) {
                while (queue.isEmpty()) {
                    System.out.println("Queue is empty, consumer is waiting");
                    wait();
                }
                int value = queue.poll();
                System.out.println("Consumed: " + value);
                notify();
                Thread.sleep(1000);
            }
        }
    }

    public static void main(String[] args) {
        ProducerConsumerExample example = new ProducerConsumerExample();

        Thread producerThread = new Thread(() -> {
            try {
                example.produce();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        });

        Thread consumerThread = new Thread(() -> {
            try {
                example.consume();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        });

        producerThread.start();
        consumerThread.start();
    }
}
