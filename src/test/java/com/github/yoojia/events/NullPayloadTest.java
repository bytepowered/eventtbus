package com.github.yoojia.events;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author 陈小锅 (yoojia.chen@gmail.com)
 * @since 2.2.1
 */
public class NullPayloadTest {

    @Test(expected = IllegalArgumentException.class)
    public void test() throws InterruptedException {
        NextEvents nextEvents = new NextEvents();
        EventsPayload payload = new EventsPayload(1);
        nextEvents.register(payload);
        nextEvents.emit("users", "yoojia");
        nextEvents.emit("null", "throws", null);
        payload.await();
        nextEvents.unregister(payload);

        assertThat(payload.evt1Calls.get(), equalTo(payload.perEvtCount));
        assertThat(payload.evt2Calls.get(), equalTo(payload.perEvtCount));

    }

    private static class EventsPayload extends TestPayload {

        protected EventsPayload(int count) {
            super(count);
        }

        @Subscribe(events = "users")
        public void onEvents() {
            hitEvt1();
        }

        @Subscribe(events = "users")
        public void onEvents(String username) {
            hitEvt2();
        }

    }

}
