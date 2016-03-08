package com.github.yoojia.events;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author 陈小锅 (yoojia.chen@gmail.com)
 * @since 2.0
 */
public class EventsMissedTest {

    private class TargetPayload extends TestPayload {

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
        void onMissed(String evt) {
            // FIXME 未调用，需要修复Bug
            System.err.println("- Handle dead event: " + evt);
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
