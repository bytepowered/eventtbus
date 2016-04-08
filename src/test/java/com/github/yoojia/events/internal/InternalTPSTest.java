package com.github.yoojia.events.internal;

import com.github.yoojia.events.*;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

/**
 * @author 陈小锅 (yoojia.chen@gmail.com)
 * @since 1.0
 */
public class InternalTPSTest extends $TestCase {

    private final static int COUNT_PAYLOAD = 1000;
    private final static int COUNT_NOP = COUNT_PAYLOAD * 1000 * 5;

    private static class TestEventHandler extends TestPayload implements EventHandler {

        protected TestEventHandler(int count) {
            super(count);
        }

        @Override
        public void onEvent(Object event) throws Exception {
            hitEvt1();
            hitEvt2();
        }

        @Override
        public void onErrors(Exception errors) {

        }

        @Override
        public On scheduleOn() {
            return On.CALLER_THREAD;
        }
    }

    @Test
    public void testWithSharedSchedule(){
        testWithSchedule(SharedScheduler.getDefault(), "SharedScheduler");
    }

    @Test
    public void testWithCallerSchedule(){
        testWithSchedule(new SchedulerImpl(), "CallerSchedule");
    }

    public void testWithSchedule(Scheduler scheduler, String tag){
        Dispatcher dispatcher = new Dispatcher(scheduler);
        TestEventHandler payload = new TestEventHandler(COUNT_NOP);

        dispatcher.addHandler(payload, new EventFilter() {
            @Override
            public boolean accept(Object event) {
                return true;
            }
        });

        final long timeBeforeEmits = System.nanoTime();

        for (int i = 0; i < payload.perEvtCount; i++) {
            final String strEvent = String.valueOf(NOW());
            dispatcher.emit(strEvent);
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

        // MyUbuntu: 6282669,     7030129,    7452475,    7458617
        // MyWin10:  15066775,    16627232,   16574237
        printStatistics("[" + tag + ".TPS/QPS", timeBeforeEmits, timeAfterEmits, payload.totalCalls);
    }
}
