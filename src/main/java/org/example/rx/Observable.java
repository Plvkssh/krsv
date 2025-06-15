package com.example.rx;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Реактивный поток данных с поддержкой подписки и операторов.
 */
public class Observable<T> {
    private final ObservableOnSubscribe<T> source;

    private Observable(ObservableOnSubscribe<T> source) {
        this.source = source;
    }

    // Создаёт новый Observable
    public static <T> Observable<T> create(ObservableOnSubscribe<T> source) {
        return new Observable<>(source);
    }

    // Подписка
    public Disposable subscribe(Observer<T> observer) {
        source.subscribe(observer);
        return () -> {}; // Заглушка Disposable
    }

    // Оператор map
    public <R> Observable<R> map(Function<T, R> mapper) {
        return new Observable<>(downstream -> 
            subscribe(new Observer<T>() {
                @Override
                public void onNext(T item) {
                    downstream.onNext(mapper.apply(item));
                }

                @Override
                public void onError(Throwable error) {
                    downstream.onError(error);
                }

                @Override
                public void onComplete() {
                    downstream.onComplete();
                }
            })
        );
    }

    // Оператор filter
    public Observable<T> filter(Predicate<T> predicate) {
        return new Observable<>(downstream -> 
            subscribe(new Observer<T>() {
                @Override
                public void onNext(T item) {
                    if (predicate.test(item)) {
                        downstream.onNext(item);
                    }
                }

                @Override
                public void onError(Throwable error) {
                    downstream.onError(error);
                }

                @Override
                public void onComplete() {
                    downstream.onComplete();
                }
            })
        );
    }

    // Оператор flatMap
    public <R> Observable<R> flatMap(Function<T, Observable<R>> mapper) {
        return new Observable<>(downstream -> 
            subscribe(new Observer<T>() {
                @Override
                public void onNext(T item) {
                    try {
                        mapper.apply(item).subscribe(new Observer<R>() {
                            @Override
                            public void onNext(R mappedItem) {
                                downstream.onNext(mappedItem);
                            }

                            @Override
                            public void onError(Throwable error) {
                                downstream.onError(error);
                            }

                            @Override
                            public void onComplete() {}
                        });
                    } catch (Throwable error) {
                        downstream.onError(error);
                    }
                }

                @Override
                public void onError(Throwable error) {
                    downstream.onError(error);
                }

                @Override
                public void onComplete() {
                    downstream.onComplete();
                }
            })
        );
    }
}
