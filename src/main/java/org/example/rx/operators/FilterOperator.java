package com.example.rx.operators;

import com.example.rx.Observable;
import com.example.rx.Observer;
import java.util.function.Predicate;

/**
 * Оператор filter.
 */
public class FilterOperator<T> implements Observable.Transformer<T, T> {
    private final Predicate<T> predicate;

    public FilterOperator(Predicate<T> predicate) {
        this.predicate = predicate;
    }

    @Override
    public Observable<T> apply(Observable<T> source) {
        return new Observable<>(downstream -> 
            source.subscribe(new Observer<T>() {
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
}
