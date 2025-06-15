package org.example;

import org.example.rx.Disposable;
import org.example.rx.Observable;
import org.example.rx.schedulers.ComputationScheduler;
import org.example.rx.schedulers.IOThreadScheduler;
import org.example.rx.schedulers.SingleThreadScheduler;

public class Main {
    public static void main(String[] args) {
        // Инициализация различных планировщиков для выполнения задач
        IOThreadScheduler ioScheduler = new IOThreadScheduler();
        ComputationScheduler computationScheduler = new ComputationScheduler();
        SingleThreadScheduler singleThreadScheduler = new SingleThreadScheduler();

        // Пример 1: Использование flatMap для преобразования и объединения потоков
        System.out.println("\nПример 1: Использование flatMap");
        Observable<Integer> numbers = Observable.create(observer -> {
            try {
                for (int i = 1; i <= 3; i++) {
                    observer.onNext(i);
                }
                observer.onComplete();
            } catch (Exception e) {
                observer.onError(e);
            }
        });

        Disposable disposable = numbers
            .flatMap(number -> Observable.create(observer -> {
                try {
                    // Имитация работы с задержкой
                    Thread.sleep(100);
                    observer.onNext(number * 10);
                    observer.onNext(number * 20);
                    observer.onComplete();
                } catch (Exception e) {
                    observer.onError(e);
                }
            }))
            .subscribe(
                item -> System.out.println("FlatMap результат: " + item),
                error -> System.out.println("Ошибка: " + error.getMessage()),
                () -> System.out.println("Обработка flatMap завершена!")
            );

        // Пример 2: Обработка ошибок
        System.out.println("\nПример 2: Обработка ошибок");
        Observable.create(observer -> {
            try {
                observer.onNext(1);
                observer.onNext(2);
                throw new RuntimeException("Симуляция ошибки");
            } catch (Exception e) {
                observer.onError(e);
            }
        })
        .subscribe(
            item -> System.out.println("Получено: " + item),
            error -> System.out.println("Обработанная ошибка: " + error.getMessage()),
            () -> System.out.println("Этот вызов не произойдет из-за ошибки")
        );

        // Пример 3: Использование Disposable для отмены подписки
        System.out.println("\nПример 3: Использование Disposable");
        Observable<Integer> infiniteStream = Observable.create(observer -> {
            int i = 0;
            while (true) {
                observer.onNext(i++);
                Thread.sleep(100); // Имитация задержки между элементами
            }
        });

        Disposable infiniteDisposable = infiniteStream
            .subscribeOn(ioScheduler)
            .observeOn(computationScheduler)
            .subscribe(
                item -> System.out.println("Бесконечный поток: " + item),
                error -> System.out.println("Ошибка: " + error.getMessage()),
                () -> System.out.println("Этот вызов не будет выполнен")
            );

        // Ожидание и отмена подписки на бесконечный поток
        try {
            Thread.sleep(500); // Дать время потоку поработать
            infiniteDisposable.dispose(); // Отменить подписку
            System.out.println("Бесконечный поток отменен");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Ожидание завершения всех операций перед завершением программы
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
