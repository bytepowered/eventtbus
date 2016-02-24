package com.github.yoojia.events;

import com.github.yoojia.events.core.Schedule;
import com.google.common.eventbus.EventBus;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

/**
 * @author 陈小锅 (yoojia.chen@gmail.com)
 * @since 1.0
 */
public class BenchmarkTest extends BaseTestCase {

    private final static int COUNT_NOP = 10000 * 100;
    private final static int COUNT_PAYLOAD = 1000;

    private static class ThreadsNopPayload extends Payload{

        protected ThreadsNopPayload(int count) {
            super(count);
        }

        @Subscribe(on = "str", run = Runs.ON_THREADS)
        public void onEvents(Event evt){
            hitEvt1();
        }

        @Subscribe(on = "long", run = Runs.ON_THREADS)
        public void onEvents1(Event evt){
            hitEvt2();
        }

    }

    private static class CallerNopPayload extends Payload{

        protected CallerNopPayload(int count) {
            super(count);
        }

        @Subscribe(on = "str", run = Runs.ON_CALLER_THREAD)
        public void onEvents(Event evt){
            hitEvt1();
        }

        @Subscribe(on = "long", run = Runs.ON_CALLER_THREAD)
        public void onEvents1(Event evt){
            hitEvt2();
        }

    }

    private static class Threads1msPayload extends Payload{

        protected Threads1msPayload(int count) {
            super(count);
        }

        @Subscribe(on = "str", run = Runs.ON_THREADS)
        public void onEvents(Event evt) throws InterruptedException {
            Thread.sleep(1);
            hitEvt1();
        }

        @Subscribe(on = "long", run = Runs.ON_THREADS)
        public void onEvents1(Event evt) throws InterruptedException {
            Thread.sleep(1);
            hitEvt2();
        }

    }

    private static class Caller1msPayload extends Payload{

        protected Caller1msPayload(int count) {
            super(count);
        }

        @Subscribe(on = "str", run = Runs.ON_CALLER_THREAD)
        public void onEvents(Event evt) throws InterruptedException {
            Thread.sleep(1);
            hitEvt1();
        }

        @Subscribe(on = "long", run = Runs.ON_CALLER_THREAD)
        public void onEvents1(Event evt) throws InterruptedException {
            Thread.sleep(1);
            hitEvt2();
        }

    }

    private static class GuavaCallerNopPayload extends Payload{

        protected GuavaCallerNopPayload(int count) {
            super(count);
        }

        @com.google.common.eventbus.Subscribe
        public void onEvents(String evt) throws InterruptedException {
            hitEvt1();
        }

        @com.google.common.eventbus.Subscribe
        public void onEvents1(Long evt) throws InterruptedException {
            hitEvt2();
        }

    }

    private static class GuavaCaller1msPayload extends Payload{

        protected GuavaCaller1msPayload(int count) {
            super(count);
        }

        @com.google.common.eventbus.Subscribe
        public void onEvents(String evt) throws InterruptedException {
            Thread.sleep(1);
            hitEvt1();
        }

        @com.google.common.eventbus.Subscribe
        public void onEvents1(Long evt) throws InterruptedException {
            Thread.sleep(1);
            hitEvt2();
        }

    }

    private final ExecutorService CPUs = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);

    @Test
    public void testNop1(){
        testNextEvent(new ThreadsNopPayload(COUNT_NOP), Schedules.newService(CPUs), "MultiThreads(Nop Payload)");
    }

    @Test
    public void testNop2(){
        testNextEvent(new ThreadsNopPayload(COUNT_NOP), Schedules.sharedThreads(), "SharedThreads(Nop Payload)");
    }

    @Test
    public void testNop3(){
        testNextEvent(new CallerNopPayload(COUNT_NOP), Schedules.newCaller(), "CallerThread(Nop Payload)");
    }

    @Test
    public void testNop4(){
        testGuava(new GuavaCallerNopPayload(COUNT_NOP), "GuavaEvents(Nop Payload)");
    }

    @Test
    public void test1ms1(){
        testNextEvent(new Threads1msPayload(COUNT_PAYLOAD), Schedules.newService(CPUs), "MultiThreads(1ms Payload)");
    }

    @Test
    public void test1ms2(){
        testNextEvent(new Threads1msPayload(COUNT_PAYLOAD), Schedules.sharedThreads(), "SharedThreads(1ms Payload)");
    }

    @Test
    public void test1ms3(){
        testNextEvent(new Caller1msPayload(COUNT_PAYLOAD), Schedules.newCaller(), "CallerThread(1ms Payload)");
    }

    @Test
    public void test1ms4(){
        testGuava(new GuavaCaller1msPayload(COUNT_PAYLOAD), "GuavaEvents(1ms Payload)");
    }

    private void testNextEvent(Payload payload, Schedule schedule, String tag){
        final NextEvents events = new NextEvents(schedule);

        events.register(payload);

        final long timeBeforeEmits = NOW();
        for (int i = 0; i < payload.perEvtCount; i++) {

            final long longEvent = NOW();
            events.emit(new Event("long", longEvent));

            final String strEvent = String.valueOf(NOW());
            events.emit(new Event("str", strEvent));
        }

        final long timeAfterEmits = NOW();

        try {
            payload.await();
        } catch (InterruptedException e) {
            fail(tag + ", Wait fail");
        }

        events.unregister(payload);

        assertThat(payload.evt1Calls.get(), equalTo(payload.perEvtCount));
        assertThat(payload.evt2Calls.get(), equalTo(payload.perEvtCount));

        printStatistics(tag, timeBeforeEmits, timeAfterEmits, payload.totalCalls);
    }

    private void testGuava(Payload payload, String tag){
        EventBus events = new EventBus("guava");

        events.register(payload);

        final long timeBeforeEmits = NOW();
        for (int i = 0; i < payload.perEvtCount; i++) {

            final long longEvent = NOW();
            events.post(longEvent);

            final String strEvent = String.valueOf(NOW());
            events.post(strEvent);
        }

        final long timeAfterEmits = NOW();

        try {
            payload.await();
        } catch (InterruptedException e) {
            fail(tag + ", Wait fail");
        }

        events.unregister(payload);

        assertThat(payload.evt1Calls.get(), equalTo(payload.perEvtCount));
        assertThat(payload.evt2Calls.get(), equalTo(payload.perEvtCount));

        printStatistics(tag, timeBeforeEmits, timeAfterEmits, payload.totalCalls);
    }

    private void printStatistics(String tag, long timeBeforeEmits, long timeAfterEmits, long totalCalls){
        final long timeWhenAllFinished = NOW();
        final long emitMicros = (timeAfterEmits - timeBeforeEmits) / 1000;
        final long deliveredMicros = (timeWhenAllFinished - timeBeforeEmits) / 1000;
        int deliveryRate = (int) (totalCalls / (deliveredMicros / 1000000d));

        System.err.println(tag + "\t ### " +
                "Delivered:" + deliveryRate + "/s" +
                "\t\tEmit:" + TimeUnit.MICROSECONDS.toMillis(emitMicros) + "ms" +
                "\t\tRuns:" + TimeUnit.MICROSECONDS.toMillis(deliveredMicros) + "ms" +
                "\t\tCalls:" + totalCalls
        );
    }

}
