package com.example.rx;

/**
 * Наблюдатель, реагирующий на события потока.
 */
public interface Observer<T> {
    void onNext(T item);  // Новый элемент
    void onError(Throwable error);  // Ошибка
    void onComplete();  // Завершение потока
}
