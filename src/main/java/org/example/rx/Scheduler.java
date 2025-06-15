package com.example.rx;

import java.util.concurrent.Executor;

/**
 * Определяет, в каком потоке выполняются задачи.
 */
public interface Scheduler {
    void execute(Runnable task);
    Executor getExecutor();
}
