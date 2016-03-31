package com.github.yoojia.events.internal;

import com.github.yoojia.events.SharedSchedule;
import org.junit.AfterClass;
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

    final static SharedSchedule schedule = new SharedSchedule();

    final Dispatcher dispatcher = new Dispatcher(schedule);

    @Test
    public void testThreads() throws InterruptedException {
        // missed
        dispatcher.emit(1024.0f);

        final CountDownLatch counting = new CountDownLatch(THREAD_COUNT);
        final EventHandler threads = new EventHandler() {
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

            @Override
            public int scheduleType() {
                return Scheduler0.ON_THREADS;
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
        final EventHandler caller = new EventHandler() {
            @Override
            public void onEvent(Object event) throws Exception {
                callerThreadId.set((int) Thread.currentThread().getId());
            }

            @Override
            public void onErrors(Exception errors) {
                errors.printStackTrace();
            }

            @Override
            public int scheduleType() {
                return Scheduler0.ON_CALLER_THREAD;
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
        dispatcher.setOnEventMissedListener(new OnEventMissedListener() {
            @Override
            public void onEvent(Object event) {
                missedFlag.set(true);
                System.err.println("- missed event: " + event);
            }
        });
        dispatcher.emit(10000L);
        Assert.assertTrue(missedFlag.get());
    }

    @Test
    public void testUnknownScheduleFlag() throws InterruptedException {
        final CountDownLatch counting = new CountDownLatch(1);
        final EventHandler unknown = new EventHandler() {
            @Override
            public void onEvent(Object event) throws Exception {}

            @Override
            public void onErrors(Exception errors) {
                counting.countDown();
            }

            @Override
            public int scheduleType() {
                return 9999;
            }
        };
        dispatcher.addHandler(unknown, new EventFilter() {
            @Override
            public boolean accept(Object item) {
                return true;
            }
        });

        try{
            dispatcher.emit("unknown-flag");
            counting.await();
        }finally {
            dispatcher.removeHandler(unknown);
        }

    }

    @Test
    public void testMainScheduleFlag() throws InterruptedException {
        final CountDownLatch counting = new CountDownLatch(1);
        final EventHandler main = new EventHandler() {
            @Override
            public void onEvent(Object event) throws Exception {}

            @Override
            public void onErrors(Exception errors) {
                counting.countDown();
            }

            @Override
            public int scheduleType() {
                return Scheduler0.ON_MAIN_THREAD;
            }
        };
        dispatcher.addHandler(main, new EventFilter() {
            @Override
            public boolean accept(Object item) {
                return true;
            }
        });

        try{
            dispatcher.emit("main-flag");
            counting.await();
        }finally {
            dispatcher.removeHandler(main);
        }
    }

    @Test
    public void testErrorOnErrors(){
        final EventHandler caller = new EventHandler() {
            @Override
            public void onEvent(Object event) throws Exception {
                throw new IllegalArgumentException("ERROR-EVENTS:");
            }

            @Override
            public void onErrors(Exception errors) {
                Assert.assertEquals(IllegalArgumentException.class, errors.getClass());
                throw new IllegalStateException("ERROR-ON-ERRORS");
            }

            @Override
            public int scheduleType() {
                return Scheduler0.ON_CALLER_THREAD;
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

    @AfterClass
    public static void after(){
        schedule.getWorkerThreads().shutdownNow();
        schedule.getLoopThread().shutdownNow();
    }
}
