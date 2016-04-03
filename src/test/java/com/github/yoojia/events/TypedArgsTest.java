package com.github.yoojia.events;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * @author 陈小锅 (yoojia.chen@gmail.com)
 * @since 2.0
 */
public class TypedArgsTest {

    public static final int COUNT = 10;

    @Test
    public void test() throws InterruptedException {
        NextEvents nextEvents = new NextEvents(Schedulers.newCaller());
        EventsPayload payload = new EventsPayload(COUNT);

        nextEvents.register(payload);
        for (int i = 0; i < payload.perEvtCount; i++) {
            nextEvents.emit("same-type", "yoojia", 18, 1024);
            nextEvents.emit("diff-type", "chen", 99, 2048.0f);
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

        @Subscribe(events = "diff-type")
        public void onDiffTypesEvents(String name, int age, Float weight) {
            System.err.println("[Typed args] name=" + name + ", age=" + age + ", weight=" + weight);
            assertThat(name, equalTo("chen"));
            assertThat(age, equalTo(99));
            assertThat(weight, equalTo(2048f));
            hitEvt1();
        }

        @Subscribe(events = "same-type")
        public void onSameTypesEvents(String name, int age, int weight) {
            System.err.println("name=" + name + ", age=" + age + ", weight=" + weight);
            assertThat(name, equalTo("yoojia"));
            assertThat(age, equalTo(18));
            assertThat(weight, equalTo(1024));
            hitEvt2();
        }

    }

}
