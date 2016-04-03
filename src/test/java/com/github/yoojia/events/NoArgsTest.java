package com.github.yoojia.events;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author 陈小锅 (yoojia.chen@gmail.com)
 * @since 2.2
 */
public class NoArgsTest {

    public static final int COUNT = 10;

    @Test
    public void test() throws InterruptedException {
        NextEvents nextEvents = new NextEvents();
        EventsPayload payload = new EventsPayload(COUNT);

        nextEvents.register(payload);
        for (int i = 0; i < payload.perEvtCount; i++) {
            nextEvents.emit("users", "yoojia");
        }

        payload.await();

        nextEvents.unregister(payload);

        assertThat(payload.evt1Calls.get(), equalTo(payload.perEvtCount));
        assertThat(payload.evt2Calls.get(), equalTo(payload.perEvtCount * 2));

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

        @Subscribe(events = "users")
        public void onEvents(Any any) {
            hitEvt2();
        }

    }

}
