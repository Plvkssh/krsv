package com.example.rx.operators;

import com.example.rx.Observable;
import com.example.rx.Observer;
import java.util.function.Function;

/**
 * Оператор map.
 */
public class MapOperator<T, R> implements Observable.Transformer<T, R> {
    private final Function<T, R> mapper;

    public MapOperator(Function<T, R> mapper) {
        this.mapper = mapper;
    }

    @Override
    public Observable<R> apply(Observable<T> source) {
        return new Observable<>(downstream -> 
            source.subscribe(new Observer<T>() {
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
}
