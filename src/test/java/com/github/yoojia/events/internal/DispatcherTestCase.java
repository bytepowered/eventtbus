package com.github.yoojia.events.internal;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.2
 */
public class DispatcherTestCase {

    public static final int COUNT = 100;

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
                return true;
            }
        });

        for (int i = 0; i < COUNT; i++) {
            dispatcher.emit("msg-" + i);
        }

        countDownLatch.await();
    }
}
