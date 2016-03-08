package com.github.yoojia.events.internal;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.2
 */
public class DispatcherTestCase {

    public static final int COUNT = 10;

    @Test
    public void test() throws InterruptedException {
        ExecutorService loop = Executors.newSingleThreadExecutor();
        ExecutorService worker = Executors.newCachedThreadPool();
        Dispatcher dispatcher = new Dispatcher(new Schedule(loop, worker));
        final CountDownLatch countDownLatch = new CountDownLatch(COUNT);
        dispatcher.addHandler(new EventHandler() {
            @Override
            public void onEvent(Object event) throws Exception {
                countDownLatch.countDown();
                System.err.println("- " + event + "\t@Thread: " + Thread.currentThread().getId());
            }

            @Override
            public void onErrors(Exception errors) {
                errors.printStackTrace();
            }

            @Override
            public int scheduleType() {
                return Schedule.ON_THREADS;
            }

        }, new EventFilter() {
            @Override
            public boolean accept(Object event) {
                return String.class.equals(event.getClass());
            }
        });

        for (int i = 0; i < COUNT; i++) {
            dispatcher.emit("msg-" + i);
        }

        countDownLatch.await();

        // On missed dead event listener
        final AtomicBoolean missedDeadEvent = new AtomicBoolean(false);
        dispatcher.setOnMissedDeadEventListener(new OnMissedDeadEventListener() {
            @Override
            public void onMissedDeadEvent(DeadEvent event) {
                System.err.println("- missed event: " + event);
                missedDeadEvent.set(true);
            }
        });
        dispatcher.emit(10000L);
        Assert.assertTrue(missedDeadEvent.get());

        // On dead event
        dispatcher.addHandler(new EventHandler() {
            @Override
            public void onEvent(Object event) throws Exception {
                System.err.println("- on dead event: " + event);
                Assert.assertTrue( event instanceof DeadEvent);
            }

            @Override
            public void onErrors(Exception errors) {}

            @Override
            public int scheduleType() {
                return Schedule.ON_CALLER_THREAD;
            }
        }, new EventFilter() {
            @Override
            public boolean accept(Object event) {
                return DeadEvent.class.equals(event.getClass());
            }
        });

        dispatcher.emit(20000L);
    }
}
