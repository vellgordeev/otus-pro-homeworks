package ru.gordeev;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ThreadPool {
    private static final Logger logger = LogManager.getLogger(ThreadPool.class);

    private final int numberOfThreads;
    private final List<Worker> workers;
    private final LinkedList<Runnable> taskQueue;
    private volatile boolean isShutdown;

    public ThreadPool(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
        this.taskQueue = new LinkedList<>();
        this.workers = new ArrayList<>(numberOfThreads);
        this.isShutdown = false;

        initializeWorkers();
    }

    private void initializeWorkers() {
        for (int i = 0; i < numberOfThreads; i++) {
            Worker worker = new Worker();
            workers.add(worker);
            worker.start();
        }
    }

    public synchronized void execute(Runnable task) {
        if (isShutdown) {
            throw new IllegalStateException("ThreadPool is shutdown, cannot accept new tasks");
        }
        synchronized (taskQueue) {
            taskQueue.add(task);
            taskQueue.notify();
            logger.info("Task added to queue: {}", task);
        }
    }

    public synchronized void shutdown() {
        isShutdown = true;
        for (Worker worker : workers) {
            worker.interrupt();
        }
        logger.info("ThreadPool is shutdown.");
    }

    private class Worker extends Thread {

        @Override
        public void run() {
            while (true) {
                Runnable task;
                synchronized (taskQueue) {
                    while (taskQueue.isEmpty() && !isShutdown) {
                        try {
                            taskQueue.wait();
                        } catch (InterruptedException e) {
                            if (isShutdown) {
                                Thread.currentThread().interrupt();
                                return;
                            }
                        }
                    }

                    if (isShutdown && taskQueue.isEmpty()) {
                        Thread.currentThread().interrupt();
                        return;
                    }

                    task = taskQueue.poll();
                }

                try {
                    if (task != null) {
                        task.run();
                        logger.info("Task executed: {}", task);
                    }
                } catch (RuntimeException e) {
                    logger.error("Task execution failed: {}", e.getMessage(), e);
                }
            }
        }
    }
}
