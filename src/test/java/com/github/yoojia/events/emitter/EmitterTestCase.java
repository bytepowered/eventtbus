package com.github.yoojia.events.emitter;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.2
 */
public class EmitterTestCase {

    public static final int THREAD_COUNT = 10;

    final EventEmitter mEmitter = new EventEmitter();

    @Test
    public void test() throws InterruptedException {
        // missed
        mEmitter.emit(1024.0f);

        final CountDownLatch counting = new CountDownLatch(THREAD_COUNT);
        final Subscriber subscriber = new Subscriber() {

            @Override
            public void onEvent(Object event) throws Exception {
                counting.countDown();
                final long tid = Thread.currentThread().getId();
                System.err.println("- " + event + "\t@Thread: " + tid);
            }

            @Override
            public void onError(Exception errors) {
                errors.printStackTrace();
            }

        };

        // 只处理String类型的事件
        mEmitter.addSubscriber(subscriber, new EventFilter() {
            @Override
            public boolean accept(Object event) {
                return String.class.equals(event.getClass());
            }
        });

        // 发射事件
        for (int i = 0; i < THREAD_COUNT; i++) {
            mEmitter.emit("msg-" + i);
        }

        counting.await();

        mEmitter.removeSubscriber(subscriber);
    }

    @Test
    public void testEventMissed(){
        final AtomicBoolean missedFlag = new AtomicBoolean(false);
        mEmitter.addEventInterceptor(new EventInterceptor() {
            @Override
            public boolean handle(Object event) {
                if (event instanceof DeadEvent) {
                    missedFlag.set(true);
                    System.err.println("- missed event: " + event);
                }
                return true;
            }
        });
        mEmitter.emit(10000L);
        Assert.assertTrue(missedFlag.get());
    }

    @Test(expected = InvokeException.class)
    public void testErrorOnErrors(){
        final Subscriber caller = new Subscriber() {
            @Override
            public void onEvent(Object event) throws Exception {
                throw new IllegalArgumentException("ERROR-EVENTS:");
            }

            @Override
            public void onError(Exception errors) {
                Assert.assertEquals(IllegalArgumentException.class, errors.getClass());
                throw new IllegalStateException("ERROR-ON-ERRORS");
            }

        };
        mEmitter.addSubscriber(caller, new EventFilter() {
            @Override
            public boolean accept(Object event) {
                return int.class.equals(event.getClass())
                        || Integer.class.equals(event.getClass());
            }
        });
        mEmitter.emit(123);
        mEmitter.removeSubscriber(caller);
    }

}
