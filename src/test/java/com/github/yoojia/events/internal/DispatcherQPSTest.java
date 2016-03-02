package com.github.yoojia.events.internal;

import com.github.yoojia.events.BaseTestCase;
import com.github.yoojia.events.Payload;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

/**
 * @author 陈小锅 (yoojia.chen@gmail.com)
 * @since 1.0
 */
public class DispatcherQPSTest extends BaseTestCase {

    private final static int COUNT_PAYLOAD = 1000;
    private final static int COUNT_NOP = COUNT_PAYLOAD * 1000 * 5;

    private static class TestEventHandler extends Payload implements EventHandler {

        protected TestEventHandler(int count) {
            super(count);
        }

        @Override
        public void onEvent(EventMessage event) throws Exception {
            hitEvt1();
            hitEvt2();
        }

        @Override
        public void onErrors(Exception errors) {

        }

        @Override
        public int scheduleType() {
            return Schedule.ON_CALLER_THREAD;
        }
    }

    @Test
    public void test(){

        Dispatcher dispatcher = new Dispatcher(SharedSchedule.getDefault());

        TestEventHandler payload = new TestEventHandler(COUNT_NOP);

        dispatcher.addHandler(payload, new EventFilter() {
            @Override
            public boolean accept(EventMessage event) {
                return true;
            }
        });

        final long timeBeforeEmits = System.nanoTime();

        for (int i = 0; i < payload.perEvtCount; i++) {
            final String strEvent = String.valueOf(NOW());
            dispatcher.emit(new EventMessage(strEvent));
        }

        final long timeAfterEmits = NOW();

        try {
            payload.await();
        } catch (InterruptedException e) {
            fail("EventDispatcher Test, Wait fail");
        }

        dispatcher.removeHandler(payload);

        assertThat(payload.evt1Calls.get(), equalTo(payload.perEvtCount));
        assertThat(payload.evt2Calls.get(), equalTo(payload.perEvtCount));

        // MBP: 6707866, 6300427, 6865571
        printStatistics("Dispatch.QPS", timeBeforeEmits, timeAfterEmits, payload.totalCalls);
    }
}