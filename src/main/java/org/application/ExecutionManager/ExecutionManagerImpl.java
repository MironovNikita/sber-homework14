package org.application.ExecutionManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ExecutionManagerImpl implements ExecutionManager {
    private final ExecutorService executorService;

    public ExecutionManagerImpl(int threadsCount) {
        executorService = Executors.newFixedThreadPool(threadsCount);
    }

    @Override
    public Context execute(Runnable callback, Runnable... tasks) {
        ExecutionContext context = new ExecutionContext(tasks.length, callback);

        for (Runnable task : tasks) {
            executorService.submit(() -> {
                try {
                    task.run();
                    context.onTaskCompleted();
                } catch (Exception e) {
                    context.onTaskFailed();
                }
            });
        }

        return context;
    }

    private class ExecutionContext implements Context {
        private final int totalTasks;
        private final Runnable callback;
        private final AtomicInteger completedTaskCount = new AtomicInteger(0);
        private final AtomicInteger failedTaskCount = new AtomicInteger(0);
        private final AtomicInteger interruptedTaskCount = new AtomicInteger(0);
        private volatile boolean finished = false;

        public ExecutionContext(int totalTasks, Runnable callback) {
            this.totalTasks = totalTasks;
            this.callback = callback;
        }

        public void onTaskCompleted() {
            completedTaskCount.incrementAndGet();
            checkIfFinished();
        }

        public void onTaskFailed() {
            failedTaskCount.incrementAndGet();
            checkIfFinished();
        }

        private synchronized void checkIfFinished() {
            if (completedTaskCount.get() + failedTaskCount.get() == totalTasks && !finished) {
                finished = true;
                callback.run();
                notifyAll();
            }
        }

        @Override
        public int getCompletedTaskCount() {
            return completedTaskCount.get();
        }

        @Override
        public int getFailedTaskCount() {
            return failedTaskCount.get();
        }

        @Override
        public int getInterruptedTaskCount() {
            return interruptedTaskCount.get();
        }

        @Override
        public void interrupt() {
            executorService.shutdownNow();
            interruptedTaskCount.set(totalTasks - completedTaskCount.get() - failedTaskCount.get());
            checkIfFinished();
        }

        @Override
        public boolean isFinished() {
            return finished;
        }
    }
}

