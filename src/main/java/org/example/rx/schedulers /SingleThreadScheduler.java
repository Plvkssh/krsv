package org.example.rx.schedulers;

import org.example.rx.Scheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleThreadScheduler implements Scheduler {
    private final ExecutorService executor;

    public SingleThreadScheduler() {
        this.executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void execute(Runnable task) {
        executor.execute(task);
    }
} 
