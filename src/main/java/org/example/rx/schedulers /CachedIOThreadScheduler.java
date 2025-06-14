package org.example.rx.schedulers;

import org.example.rx.Scheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Планировщик, использующий кэшируемый пул потоков для операций ввода-вывода.
 * Похож на Schedulers.io() из RxJava.
 */
public class CachedIOThreadScheduler implements Scheduler {
    private final ExecutorService threadPool;

    public CachedIOThreadScheduler() {
        // Инициализируем кэшируемый пул потоков
        this.threadPool = Executors.newCachedThreadPool();
    }

    @Override
    public void execute(Runnable runnableTask) {
        // Запускаем задачу в пуле потоков
        threadPool.execute(runnableTask);
    }
}
