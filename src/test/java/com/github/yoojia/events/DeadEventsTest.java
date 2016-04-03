package com.github.yoojia.events;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * @author 陈小锅 (yoojia.chen@gmail.com)
 * @since 2.0
 */
public class DeadEventsTest {

    private class TargetPayload extends TestPayload {

        private final CountDownLatch missedCounting = new CountDownLatch(2);

        protected TargetPayload(int count) {
            super(count);
        }

        @Subscribe(events = "str")
        void onString(String evt) {
            hitEvt1();
        }

        @Subscribe(events = "int")
        void onInt(int evt) {
            hitEvt2();
        }

        @Subscribe(events = EventPayload.DEAD_EVENT)
        void onMissedTyped(String strDeadEvent) {
            missedCounting.countDown();
            System.err.println("- [String]:Handle dead event: " + strDeadEvent);
        }

        @Subscribe(events = EventPayload.DEAD_EVENT)
        void onMissedObject(Any anyDeadEvent) {
            missedCounting.countDown();
            System.err.println("- [Object]:Handle dead event: " + anyDeadEvent);
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

        events.emit("str", "should-missed");
        events.emit("int", "should-missed");
        events.emit("int", 155);

        TargetPayload target = new TargetPayload(1);

        events.register(target);

        events.emit("str", "HAHA");
        events.emit("int", 2016);

        events.emit("miss-str", "DeadWithHandler");
        events.emit("miss-int", 155);
        events.emit("miss-array", 123, 456);

        target.await();

        events.unregister(target);

        Assert.assertEquals(1, target.evt1Calls.get());
        Assert.assertEquals(1, target.evt2Calls.get());
    }
}
