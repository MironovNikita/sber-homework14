package org.application.Task;

import java.util.concurrent.*;

public class TaskApp {
    public static void main(String[] args) {
        System.out.println("Задание №1 - класс Task");

        Task<Integer> task = new Task<>(() -> {
            TimeUnit.SECONDS.sleep(5);
            System.out.println(Thread.currentThread().getName() + " начал выполнение");
            return 5;
        });

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Future<Integer> result1 = executorService.submit(task::get);
        Future<Integer> result2 = executorService.submit(task::get);
        Future<Integer> result3 = executorService.submit(task::get);

        executorService.shutdown();

        try {
            System.out.println("Результат 1: " + result1.get());
            System.out.println("Результат 2: " + result2.get());
            System.out.println("Результат 3: " + result3.get());
        } catch (InterruptedException | ExecutionException exception) {
            exception.printStackTrace();
        }

        if (result1.isDone() && result2.isDone() && result3.isDone()) {
            try {
                int res1 = result1.get();
                int res2 = result2.get();
                int res3 = result3.get();
                if (res1 == res2 && res2 == res3) {
                    System.out.println("Результаты из всех потоков одинаковые: " + res1);
                } else {
                    System.out.println("Ошибка: результаты из разных потоков различны");
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}