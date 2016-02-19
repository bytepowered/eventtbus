package com.github.yoojia.events;

import com.github.yoojia.events.core.*;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

/**
 * @author 陈小锅 (yoojia.chen@gmail.com)
 * @since 1.0
 */
public class ReactorTest extends BaseTester {

    private final static int COUNT_PAYLOAD = 1000;
    private final static int COUNT_NOP = COUNT_PAYLOAD * 1000 * 5;

    private static class TestSubscriber extends Payload implements Subscriber<String> {

        protected TestSubscriber(int count) {
            super(count);
        }

        @Override
        public void onCall(String input) throws Exception {
            hitEvt1();
            hitEvt2();
        }

        @Override
        public void onErrors(String input, Exception errors) {

        }
    }

    @Test
    public void test(){

        Reactor<String> reactor = new Reactor<>(Schedules.newCaller());

        TestSubscriber payload = new TestSubscriber(COUNT_NOP);

        reactor.add(Descriptors.create1(payload, Schedule.FLAG_ON_CALLER_THREAD, new Filter<String>() {
            @Override
            public boolean accept(String item) {
                return true;
            }
        }));

        final long timeBeforeEmits = System.nanoTime();

        for (int i = 0; i < payload.perEvtCount; i++) {
            final String strEvent = String.valueOf(NOW());
            reactor.emit(strEvent);
        }

        final long timeAfterEmits = NOW();

        try {
            payload.await();
        } catch (InterruptedException e) {
            fail("Reactor Test, Wait fail");
        }

        reactor.remove(payload);

        assertThat(payload.evt1Calls.get(), equalTo(payload.perEvtCount));
        assertThat(payload.evt2Calls.get(), equalTo(payload.perEvtCount));

        final long timeWhenAllFinished = NOW();
        final long emitMicros = (timeAfterEmits - timeBeforeEmits) / 1000;
        final long deliveredMicros = (timeWhenAllFinished - timeBeforeEmits) / 1000;
        int deliveryRate = (int) (payload.totalCalls / (deliveredMicros / 1000000d));

        System.err.println("Reactor\t ### " +
                        "Delivered:" + deliveryRate + "/s" +
                        "\t\tEmit:" + TimeUnit.MICROSECONDS.toMillis(emitMicros) + "ms" +
                        "\t\tRuns:" + TimeUnit.MICROSECONDS.toMillis(deliveredMicros) + "ms" +
                        "\t\tCalls:" + payload.totalCalls
        );
    }
}
