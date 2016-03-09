package com.github.yoojia.events;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * @author 陈小锅 (yoojia.chen@gmail.com)
 * @since 2.0
 */
public class EventsMissedTest {

    private class TargetPayload extends TestPayload {

        private final CountDownLatch missedCounting = new CountDownLatch(2);

        protected TargetPayload(int count) {
            super(count);
        }

        @Subscribe(on = "str")
        void onString(String evt) {
            hitEvt1();
        }

        @Subscribe(on = "int")
        void onInt(int evt) {
            hitEvt2();
        }

        @Subscribe(on = PayloadEvent.DEAD_EVENT)
        void onMissedTyped(String evt) {
            missedCounting.countDown();
            System.err.println("- [String]:Handle dead event: " + evt);
        }

        @Subscribe(on = PayloadEvent.DEAD_EVENT)
        void onMissedObject(Object evt) {
            missedCounting.countDown();
            System.err.println("- [Object]:Handle dead event: " + evt);
        }

        @Override
        public void await() throws InterruptedException {
            super.await();
            missedCounting.await();
        }
    }

    @Test
    public void test() throws InterruptedException {
        NextEvents events = new NextEvents();

        events.emit("str", "WILL-MISS");
        events.emit("int", "WILL-MISS");
        events.emit("int", 999);

        TargetPayload target = new TargetPayload(1);

        events.register(target);

        events.emit("str", "HAHA");
        events.emit("int", 2016);
        events.emit("HAHA", "will-miss");

        target.await();

        events.unregister(target);

        Assert.assertEquals(1, target.evt1Calls.get());
        Assert.assertEquals(1, target.evt2Calls.get());
    }
}
