package com.github.yoojia.events.internal;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.2
 */
public class DispatcherTestCase {

    public static final int THREAD_COUNT = 10;

    final Dispatcher dispatcher = new Dispatcher();

    @Test
    public void testThreads() throws InterruptedException {
        // missed
        dispatcher.emit(1024.0f);

        final CountDownLatch counting = new CountDownLatch(THREAD_COUNT);
        final Handler threads = new Handler() {
            @Override
            public void onEvent(Object event) throws Exception {
                counting.countDown();
                final long tid = Thread.currentThread().getId();
                System.err.println("- " + event + "\t@Thread: " + tid);
            }

            @Override
            public void onErrors(Exception errors) {
                errors.printStackTrace();
            }

        };

        dispatcher.addHandler(threads, new EventFilter() {
            @Override
            public boolean accept(Object event) {
                return String.class.equals(event.getClass());
            }
        });

        // events
        for (int i = 0; i < THREAD_COUNT; i++) {
            dispatcher.emit("msg-" + i);
        }

        counting.await();

        dispatcher.removeHandler(threads);
    }

    @Test
    public void testCaller(){
        final AtomicInteger callerThreadId = new AtomicInteger(-9);
        final Handler caller = new Handler() {
            @Override
            public void onEvent(Object event) throws Exception {
                callerThreadId.set((int) Thread.currentThread().getId());
            }

            @Override
            public void onErrors(Exception errors) {
                errors.printStackTrace();
            }

        };

        dispatcher.addHandler(caller, new EventFilter() {
            @Override
            public boolean accept(Object event) {
                return int.class.equals(event.getClass())
                        || Integer.class.equals(event.getClass());
            }
        });

        // caller
        dispatcher.emit(123);

        dispatcher.removeHandler(caller);

        Assert.assertEquals(Thread.currentThread().getId(), callerThreadId.get());
    }

    @Test
    public void testEventMissed(){
        final AtomicBoolean missedFlag = new AtomicBoolean(false);
        dispatcher.addOnEventHandler(new OnEventHandler() {
            @Override
            public boolean handleEvent(Object event) {
                if (event instanceof DeadEvent) {
                    missedFlag.set(true);
                    System.err.println("- missed event: " + event);
                }
                return true;
            }
        });
        dispatcher.emit(10000L);
        Assert.assertTrue(missedFlag.get());
    }

    @Test
    public void testErrorOnErrors(){
        final Handler caller = new Handler() {
            @Override
            public void onEvent(Object event) throws Exception {
                throw new IllegalArgumentException("ERROR-EVENTS:");
            }

            @Override
            public void onErrors(Exception errors) {
                Assert.assertEquals(IllegalArgumentException.class, errors.getClass());
                throw new IllegalStateException("ERROR-ON-ERRORS");
            }

        };
        dispatcher.addHandler(caller, new EventFilter() {
            @Override
            public boolean accept(Object event) {
                return int.class.equals(event.getClass())
                        || Integer.class.equals(event.getClass());
            }
        });
        dispatcher.emit(123);
        dispatcher.removeHandler(caller);
    }

}
