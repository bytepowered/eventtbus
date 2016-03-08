package com.github.yoojia.events;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author 陈小锅 (yoojia.chen@gmail.com)
 * @since 2.0
 */
public class SameTypesTest {

    public static final int COUNT = 10;

    @Test
    public void test() throws InterruptedException {
        NextEvents nextEvents = new NextEvents(Schedules.newCaller());
        EventsPayload payload = new EventsPayload(COUNT);

        nextEvents.register(payload);
        for (int i = 0; i < payload.perEvtCount; i++) {
            nextEvents.emit("users", "yoojia", 18, 1024);
        }

        payload.await();

        nextEvents.unregister(payload);

        assertThat(payload.evt1Calls.get(), equalTo(payload.perEvtCount));

    }

    private static class EventsPayload extends TestPayload {

        protected EventsPayload(int count) {
            super(count);
        }

        @Subscribe(on = "users")
        public void onEvents(String name, int age, int weight) {
            System.err.println("name=" + name + ", age=" + age + ", weight=" + weight);
            hitEvt1();
            hitEvt2();
            assertThat(name, equalTo("yoojia"));
            assertThat(age, equalTo(18));
            assertThat(weight, equalTo(1024));
        }

    }

}
