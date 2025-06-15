package com.example.rx;

/**
 * Позволяет отменить подписку.
 */
public interface Disposable {
    void dispose();
    boolean isDisposed();
}
