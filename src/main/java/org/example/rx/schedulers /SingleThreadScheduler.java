package com.example.rx.schedulers;

import com.example.rx.Scheduler;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Scheduler с одним потоком.
 */
public class SingleThreadScheduler implements Scheduler {
    private final Executor executor = Executors.newSingleThreadExecutor();

    @Override
    public void execute(Runnable task) {
        executor.execute(task);
    }

    @Override
    public Executor getExecutor() {
        return executor;
    }
}
