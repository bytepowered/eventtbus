package com.github.yoojia.events;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author 陈小锅 (yoojia.chen@gmail.com)
 * @since 2.2
 */
public class ObjectTypeTest {

    public static final int COUNT = 10;

    @Test
    public void test() throws InterruptedException {
        NextEvents nextEvents = new NextEvents(Schedulers.newCaller());
        EventsPayload payload = new EventsPayload(COUNT);

        nextEvents.register(payload);
        for (int i = 0; i < payload.perEvtCount; i++) {
            nextEvents.emit("int-type", 12);
            nextEvents.emit("obj-type", new Object());
            nextEvents.emit("any-type", new Object());
        }

        payload.await();

        nextEvents.unregister(payload);

        assertThat(payload.evt1Calls.get(), equalTo(payload.perEvtCount));
        assertThat(payload.evt2Calls.get(), equalTo(payload.perEvtCount));

    }

    private static class EventsPayload extends TestPayload {

        protected EventsPayload(int count) {
            super(count);
        }

        @Subscribe(events = "int-type")
        public void onIntEvents(int value) {
            hitEvt1();
        }

        @Subscribe(events = "obj-type")
        public void onObjectEvents(Object obj) {
            hitEvt2();
        }

        @Subscribe(events = "obj-type")
        public void onObjectStringEvents(String obj) {
            Assert.fail();
        }

        @Subscribe(events = "any-type")
        public void onAnyEvents(Object obj) {
            Assert.assertTrue(null != obj);
        }

        @Subscribe(events = "any-type")
        public void onAnyEvents(Any obj) {
            Assert.assertTrue(null != obj);
        }
    }

}
