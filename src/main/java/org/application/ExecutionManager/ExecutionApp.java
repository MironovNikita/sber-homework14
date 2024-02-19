package org.application.ExecutionManager;

public class ExecutionApp {
    public static void main(String[] args) {
        ExecutionManager executionManager = new ExecutionManagerImpl(5);

        Runnable callback = () -> System.out.println("Callback выполнен");

        Runnable[] tasks = new Runnable[10];
        for (int i = 0; i < tasks.length; i++) {
            int taskId = i + 1;
            tasks[i] = () -> {
                System.out.println("Задача " + taskId + " началась");
                try {
                    Thread.sleep(1000);
                    if (taskId % 2 == 0) {
                        throw new RuntimeException("Задача " + taskId + " завершилась с ошибкой");
                    }
                    System.out.println("Задача " + taskId + " завершена");
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                } catch (RuntimeException exception) {
                    Thread.currentThread().interrupt();
                    System.out.println("Произошло исключение в задаче " + taskId + ": " + exception.getMessage());
                    throw exception;
                }
            };
        }

        Context context = executionManager.execute(callback, tasks);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        context.interrupt();

        while (!context.isFinished()) {
            System.out.println("Контекст ещё не завершён...");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("Все задачи завершены или прерваны");
        System.out.println("Завершённые задачи: " + context.getCompletedTaskCount());
        System.out.println("Незавершённые задачи: " + context.getFailedTaskCount());
        System.out.println("Прерванные задачи: " + context.getInterruptedTaskCount());
    }
}
