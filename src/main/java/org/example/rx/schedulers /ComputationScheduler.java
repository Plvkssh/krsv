package com.example.rx.schedulers;

import com.example.rx.core.Scheduler;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Scheduler для CPU-интенсивных операций (FixedThreadPool).
 */
public class ComputationScheduler implements Scheduler {
    private final Executor executor = Executors.newFixedThreadPool(
        Runtime.getRuntime().availableProcessors()
    );

    @Override
    public void execute(Runnable task) {
        executor.execute(task);
    }

    @Override
    public Executor getExecutor() {
        return executor;
    }
}
