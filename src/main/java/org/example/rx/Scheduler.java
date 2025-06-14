package org.example.rx;

/**
 * Интерфейс для планирования выполнения задач на различных потоках.
 */
public interface Scheduler {

    /**
     * Запланировать выполнение переданной задачи.
     * @param task Задача, которую необходимо выполнить
     */
    void execute(Runnable task);
} 
