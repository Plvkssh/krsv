package org.example.rx.schedulers;

import org.example.rx.Scheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Планировщик, который использует фиксированный пул потоков для выполнения вычислительных задач.
 * По аналогии с Schedulers.computation() из RxJava.
 */
public class ComputationTaskScheduler implements Scheduler {
    private final ExecutorService taskExecutor;

    public ComputationTaskScheduler() {
        // Создаем исполнитель с количеством доступных ядер процессора
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        this.taskExecutor = Executors.newFixedThreadPool(availableProcessors);
    }

    @Override
    public void execute(Runnable task) {
        // Отправляем задачу на выполнение в пул потоков
        taskExecutor.execute(task);
    }
}
