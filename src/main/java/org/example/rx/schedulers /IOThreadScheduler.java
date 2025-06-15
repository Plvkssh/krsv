package com.example.rx.schedulers;

import com.example.rx.Scheduler;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class IOThreadScheduler implements Scheduler {
    private final Executor executor = Executors.newCachedThreadPool();

    @Override
    public void schedule(Action action) {
        executor.execute(() -> {
            try {
                action.run();
            } catch (Exception e) {
                Thread.currentThread().getUncaughtExceptionHandler()
                    .uncaughtException(Thread.currentThread(), e);
            }
        });
    }

    @Override
    public Executor getExecutor() {
        return executor;
    }
}
