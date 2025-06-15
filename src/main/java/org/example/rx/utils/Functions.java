package com.example.rx.utils;

/**
 * Functional interfaces for RxJava operations
 */
public final class Functions {

    @FunctionalInterface
    public interface Function<T, R> {
        R apply(T t) throws Exception;
    }

    @FunctionalInterface
    public interface Predicate<T> {
        boolean test(T t) throws Exception;
    }

    @FunctionalInterface
    public interface Consumer<T> {
        void accept(T t) throws Exception;
    }

    @FunctionalInterface
    public interface Action {
        void run() throws Exception;
    }

    @FunctionalInterface
    public interface ObservableOnSubscribe<T> {
        void subscribe(Observer<T> emitter) throws Exception;
    }
}
