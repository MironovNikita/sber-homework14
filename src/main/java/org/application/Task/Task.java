package org.application.Task;

import java.util.concurrent.Callable;

public class Task<T> {
    private final Callable<? extends T> callable;
    private volatile T result;
    private volatile boolean executed = false;
    private volatile boolean exceptionThrown = false;
    private volatile RuntimeException runtimeException;

    public Task(Callable<? extends T> callable) {
        this.callable = callable;
    }

    public synchronized T get() {
        if (!executed) {
            try {
                result = callable.call();
            } catch (Exception exception) {
                exceptionThrown = true;
                runtimeException = new CustomTaskException("При выполнении задачи возникло исключение", exception);
            }
            executed = true;
            notifyAll();
        }

        if (exceptionThrown) {
            throw runtimeException;
        }

        return result;
    }
}
