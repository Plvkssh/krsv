package com.example.rx;

import com.example.rx.*;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class ObservableTest {
    @Test
    public void testMap_TransformsItems() {
        Observable.create(emitter -> {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onComplete();
            })
            .map(x -> x * 10)
            .subscribe(new Observer<Integer>() {
                int count = 0;

                @Override
                public void onNext(Integer item) {
                    count++;
                    if (count == 1) assertEquals(10, (int) item);
                    if (count == 2) assertEquals(20, (int) item);
                }

                @Override
                public void onError(Throwable error) {
                    fail("Unexpected error");
                }

                @Override
                public void onComplete() {
                    assertEquals(2, count);
                }
            });
    }

    @Test
    public void testFilter_OnlyEvenNumbers() {
        Observable.create(emitter -> {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onNext(4);
                emitter.onComplete();
            })
            .filter(x -> x % 2 == 0)
            .subscribe(new Observer<Integer>() {
                int count = 0;

                @Override
                public void onNext(Integer item) {
                    count++;
                    assertTrue(item % 2 == 0);
                }

                @Override
                public void onError(Throwable error) {
                    fail("Unexpected error");
                }

                @Override
                public void onComplete() {
                    assertEquals(2, count);
                }
            });
    }

    @Test
    public void testFlatMap_ExpandItems() {
        Observable.create(emitter -> {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onComplete();
            })
            .flatMap(x -> Observable.create(emitter -> {
                emitter.onNext(x * 10);
                emitter.onNext(x * 20);
                emitter.onComplete();
            }))
            .subscribe(new Observer<Integer>() {
                List<Integer> items = new ArrayList<>();

                @Override
                public void onNext(Integer item) {
                    items.add(item);
                }

                @Override
                public void onError(Throwable error) {
                    fail("Unexpected error");
                }

                @Override
                public void onComplete() {
                    assertEquals(4, items.size());
                    assertTrue(items.containsAll(Arrays.asList(10, 20, 20, 40)));
                }
            });
    }

    @Test
    public void testErrorHandling() {
        Observable.create(emitter -> {
                emitter.onNext(1);
                emitter.onError(new RuntimeException("Test error"));
                emitter.onNext(2); // Не должно дойти
            })
            .subscribe(new Observer<Integer>() {
                boolean errorReceived = false;

                @Override
                public void onNext(Integer item) {
                    if (item == 2) fail("Should not receive item after error");
                }

                @Override
                public void onError(Throwable error) {
                    errorReceived = true;
                    assertEquals("Test error", error.getMessage());
                }

                @Override
                public void onComplete() {
                    fail("Should not call onComplete after error");
                }
            });
    }
}
