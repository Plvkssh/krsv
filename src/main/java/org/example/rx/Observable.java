package com.example.rx;

import com.example.rx.utils.Functions.*;

public class Observable<T> {
    private final ObservableOnSubscribe<T> source;

    private Observable(ObservableOnSubscribe<T> source) {
        this.source = source;
    }

    public static <T> Observable<T> create(ObservableOnSubscribe<T> source) {
        return new Observable<>(source);
    }

    public Disposable subscribe(Observer<T> observer) {
        try {
            source.subscribe(observer);
        } catch (Exception e) {
            observer.onError(e);
        }
        return () -> {};
    }

    public <R> Observable<R> map(Function<T, R> mapper) {
        return new Observable<>(downstream -> 
            subscribe(new Observer<T>() {
                @Override
                public void onNext(T item) {
                    try {
                        downstream.onNext(mapper.apply(item));
                    } catch (Exception e) {
                        downstream.onError(e);
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

    public Observable<T> filter(Predicate<T> predicate) {
        return new Observable<>(downstream -> 
            subscribe(new Observer<T>() {
                @Override
                public void onNext(T item) {
                    try {
                        if (predicate.test(item)) {
                            downstream.onNext(item);
                        }
                    } catch (Exception e) {
                        downstream.onError(e);
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
                    } catch (Exception e) {
                        downstream.onError(e);
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

    public Observable<T> subscribeOn(Scheduler scheduler) {
        return new Observable<>(downstream -> 
            scheduler.schedule(() -> {
                subscribe(downstream);
                return null;
            })
        );
    }

    public Observable<T> observeOn(Scheduler scheduler) {
        return new Observable<>(downstream -> 
            subscribe(new Observer<T>() {
                @Override
                public void onNext(T item) {
                    scheduler.execute(() -> downstream.onNext(item));
                }

                @Override
                public void onError(Throwable error) {
                    scheduler.execute(() -> downstream.onError(error));
                }

                @Override
                public void onComplete() {
                    scheduler.execute(downstream::onComplete);
                }
            })
        );
    }
}
