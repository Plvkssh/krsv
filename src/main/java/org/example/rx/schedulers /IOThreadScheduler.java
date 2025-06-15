package com.example.rx.schedulers;

import com.example.rx.Scheduler;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Scheduler для I/O операций (CachedThreadPool).
 */
public class IOThreadScheduler implements Scheduler {
    private final Executor executor = Executors.newCachedThreadPool();

    @Override
    public void execute(Runnable task) {
        executor.execute(task);
    }

    @Override
    public Executor getExecutor() {
        return executor;
    }
}
