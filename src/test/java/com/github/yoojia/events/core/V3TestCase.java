package com.github.yoojia.events.core;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.3
 */
public class V3TestCase {

    @Test
    public void test() throws InterruptedException {
        ExecutorService loop = Executors.newSingleThreadExecutor();
        ExecutorService worker = Executors.newCachedThreadPool();
        Dispatcher dispatcher = new Dispatcher(new Schedule(loop, worker));

        dispatcher.addHandler(new EventHandler() {
            @Override
            public void onEvent(EventMessage event) throws Exception {
                System.err.println("- " + event.getEvent() + "\t@Thread: " + Thread.currentThread().getId());
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
            public boolean accept(EventMessage event) {
                return true;
            }
        });

        for (int i = 0; i < 100; i++) {
            dispatcher.emit(new EventMessage("msg-" + i));
        }

        TimeUnit.SECONDS.sleep(1);
    }
}
