package com.example.rx;

import com.example.rx.*;
import com.example.rx.schedulers.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SchedulerTest {
    @Test
    public void testIOThreadScheduler_RunsInDifferentThread() {
        Scheduler scheduler = new IOThreadScheduler();
        final boolean[] isDifferentThread = {false};

        scheduler.execute(() -> {
            isDifferentThread[0] = !Thread.currentThread().getName().equals("main");
        });

        assertTrue(isDifferentThread[0]);
    }

    @Test
    public void testComputationScheduler_UsesFixedPool() {
        Scheduler scheduler = new ComputationScheduler();
        final boolean[] isComputationThread = {false};

        scheduler.execute(() -> {
            isComputationThread[0] = Thread.currentThread().getName().startsWith("pool-");
        });

        assertTrue(isComputationThread[0]);
    }

    @Test
    public void testSingleThreadScheduler_RunsSequentially() {
        Scheduler scheduler = new SingleThreadScheduler();
        final List<String> executionOrder = new ArrayList<>();

        scheduler.execute(() -> executionOrder.add("Task 1"));
        scheduler.execute(() -> executionOrder.add("Task 2"));

        // В реальном тесте нужно дождаться выполнения
        try { Thread.sleep(100); } catch (InterruptedException ignored) {}

        assertEquals(Arrays.asList("Task 1", "Task 2"), executionOrder);
    }
}
