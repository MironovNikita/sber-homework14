package org.application.ExecutionManager;

public interface ExecutionManager {
    Context execute(Runnable callback, Runnable... tasks);
}
