package org.example.rx;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Класс, представляющий Observable в паттерне "Наблюдатель".
 * @param <T> Тип элементов, которые эмитит Observable
 */
public class Observable<T> {
    private final Consumer<Observer<T>> source;

    private Observable(Consumer<Observer<T>> source) {
        this.source = source;
    }

    /**
     * Создает новый Observable из функции источника.
     * @param source Функция, определяющая, как эмитятся элементы
     * @param <T> Тип элементов
     * @return новый экземпляр Observable
     */
    public static <T> Observable<T> create(Consumer<Observer<T>> source) {
        return new Observable<>(source);
    }

    /**
     * Подписка на поток данных с возвратом Disposable для отмены.
     * @param observer Объект Observer для обработки событий
     * @return Disposable для отмены подписки
     */
    public Disposable subscribe(Observer<T> observer) {
        AtomicBoolean isDisposed = new AtomicBoolean(false);
        try {
            source.accept(new Observer<T>() {
                @Override
                public void onNext(T item) {
                    if (!isDisposed.get()) {
                        observer.onNext(item);
                    }
                }

                @Override
                public void onError(Throwable t) {
                    if (!isDisposed.get()) {
                        observer.onError(t);
                    }
                }

                @Override
                public void onComplete() {
                    if (!isDisposed.get()) {
                        observer.onComplete();
                    }
                }
            });
        } catch (Exception e) {
            if (!isDisposed.get()) {
                observer.onError(e);
            }
        }

        return new Disposable() {
            @Override
            public void dispose() {
                isDisposed.set(true);
            }

            @Override
            public boolean isDisposed() {
                return isDisposed.get();
            }
        };
    }

    /**
     * Подписка с использованием колбэков для обработки событий.
     * @param onNext Обработка новых элементов
     * @param onError Обработка ошибок
     * @param onComplete Обработка завершения потока
     * @return Disposable для отмены подписки
     */
    public Disposable subscribe(Consumer<T> onNext, Consumer<Throwable> onError, Runnable onComplete) {
        return subscribe(new Observer<T>() {
            @Override
            public void onNext(T item) { onNext.accept(item); }

            @Override
            public void onError(Throwable t) { onError.accept(t); }

            @Override
            public void onComplete() { onComplete.run(); }
        });
    }

    /**
     * Преобразует элементы потока с помощью функции map.
     * @param mapper Функция преобразования элемента
     * @param <R> Тип результата преобразования
     * @return новый Observable с преобразованными элементами
     */
    public <R> Observable<R> map(Function<T, R> mapper) {
        return new Observable<>(observer -> 
            this.subscribe(
                item -> {
                    try {
                        observer.onNext(mapper.apply(item));
                    } catch (Exception e) {
                        observer.onError(e);
                    }
                },
                observer::onError,
                observer::onComplete
            )
        );
    }

    /**
     * Фильтрация элементов по условию.
     * @param predicate Условие фильтрации
     * @return новый Observable с отфильтрованными элементами
     */
    public Observable<T> filter(Predicate<T> predicate) {
        return new Observable<>(observer -> 
            this.subscribe(
                item -> {
                    try {
                        if (predicate.test(item)) {
                            observer.onNext(item);
                        }
                    } catch (Exception e) {
                        observer.onError(e);
                    }
                },
                observer::onError,
                observer::onComplete
            )
        );
    }

    /**
     * Преобразование элементов в другие Observables и их объединение (flatMap).
     * @param mapper Функция преобразования элемента в другой Observable
     * @param <R> Тип элементов нового потока
     * @return объединенный поток с элементами из внутренних Observables
     */
    public <R> Observable<R> flatMap(Function<T, Observable<R>> mapper) {
        return new Observable<>(observer -> {
            AtomicBoolean disposedFlag = new AtomicBoolean(false);
            
            this.subscribe(
                item -> {
                    if (!disposedFlag.get()) {
                        try {
                            Observable<R> innerObs = mapper.apply(item);
                            innerObs.subscribe(observer::onNext, observer::onError, () -> {});
                        } catch (Exception e) {
                            observer.onError(e);
                        }
                    }
                },
                observer::onError,
                () -> {}
            );
        });
    }

    /**
     * Установка Scheduler для выполнения подписки.
     * @param scheduler Планировщик задач.
     * @return новый поток с выполнением на указанном Scheduler.
     */
    public Observable<T> subscribeOn(Scheduler scheduler) {
        return new Observable<>(observer -> scheduler.execute(() -> subscribe(observer)));
    }

    /**
     * Установка Scheduler для наблюдения за потоками.
     * @param scheduler Планировщик задач.
     * @return поток, наблюдаемый на указанном Scheduler.
   */
   public Observable<T> observeOn(Scheduler scheduler) { 
       return new Observable<>(observer -> 
           subscribe(
               item -> scheduler.execute(() -> observer.onNext(item)),
               error -> scheduler.execute(() -> observer.onError(error)),
               () -> scheduler.execute(observer::onComplete)
           )
       );
   }
}
