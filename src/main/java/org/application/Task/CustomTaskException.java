package org.application.Task;

public class CustomTaskException extends RuntimeException {
    public CustomTaskException(String message, Throwable cause) {
        super(message, cause);
    }
}
