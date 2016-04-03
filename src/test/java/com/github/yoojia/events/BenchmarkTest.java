package com.github.yoojia.events;

import com.github.yoojia.events.internal.Scheduler;
import com.google.common.eventbus.EventBus;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

/**
 * @author 陈小锅 (yoojia.chen@gmail.com)
 * @since 1.0
 */
public class BenchmarkTest extends $TestCase {

    private final static int COUNT_NOP = 10000 * 100;
    private final static int COUNT_PAYLOAD = 1000;

    private static class ThreadsNopPayload extends TestPayload {

        protected ThreadsNopPayload(int count) {
            super(count);
        }

        @Subscribe(events = "str", schedule = On.IO_THREAD)
        void onEvents(String evt){
            hitEvt1();
        }

        @Subscribe(events = "long", schedule = On.IO_THREAD)
        void onEvents1(){
            hitEvt2();
        }

    }

    private static class CallerNopPayload extends TestPayload {

        protected CallerNopPayload(int count) {
            super(count);
        }

        @Subscribe(events = "str", schedule = On.CALLER_THREAD)
        protected void onEvents(){
            hitEvt1();
        }

        @Subscribe(events = "long", schedule = On.CALLER_THREAD)
        protected void onEvents1(long evt){
            hitEvt2();
        }

    }

    private static class Threads1msPayload extends TestPayload {

        protected Threads1msPayload(int count) {
            super(count);
        }

        @Subscribe(events = "str", schedule = On.IO_THREAD)
        public void onEvents(String evt) throws InterruptedException {
            Thread.sleep(1);
            hitEvt1();
        }

        @Subscribe(events = "long", schedule = On.IO_THREAD)
        public void onEvents1() throws InterruptedException {
            Thread.sleep(1);
            hitEvt2();
        }

    }

    private static class Caller1msPayload extends TestPayload {

        protected Caller1msPayload(int count) {
            super(count);
        }

        @Subscribe(events = "str", schedule = On.CALLER_THREAD)
        public void onEvents() throws InterruptedException {
            Thread.sleep(1);
            hitEvt1();
        }

        @Subscribe(events = "long", schedule = On.CALLER_THREAD)
        public void onEvents1(Long evt) throws InterruptedException {
            Thread.sleep(1);
            hitEvt2();
        }

    }

    private static class GuavaCallerNopPayload extends TestPayload {

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

    private static class GuavaCaller1msPayload extends TestPayload {

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

    private static final ExecutorService CPUs = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);

    @AfterClass
    public static void after(){
        CPUs.shutdownNow();
    }

    @Test
    public void testNop1(){
        testNextEvent(new ThreadsNopPayload(COUNT_NOP), Schedulers.newService(CPUs), "MultiThreads(Nop Payload)");
    }

    @Test
    public void testNop2(){
        testNextEvent(new ThreadsNopPayload(COUNT_NOP), Schedulers.sharedThreads(), "SharedThreads(Nop Payload)");
    }

    @Test
    public void testNop3(){
        testNextEvent(new CallerNopPayload(COUNT_NOP), Schedulers.newCaller(), "CallerThread(Nop Payload)");
    }

    @Test
    public void testNop4(){
        testGuava(new GuavaCallerNopPayload(COUNT_NOP), "GuavaEvents(Nop Payload)");
    }

    @Test
    public void test1ms1(){
        testNextEvent(new Threads1msPayload(COUNT_PAYLOAD), Schedulers.newService(CPUs), "MultiThreads(1ms Payload)");
    }

    @Test
    public void test1ms2(){
        testNextEvent(new Threads1msPayload(COUNT_PAYLOAD), Schedulers.sharedThreads(), "SharedThreads(1ms Payload)");
    }

    @Test
    public void test1ms3(){
        testNextEvent(new Caller1msPayload(COUNT_PAYLOAD), Schedulers.newCaller(), "CallerThread(1ms Payload)");
    }

    @Test
    public void test1ms4(){
        testGuava(new GuavaCaller1msPayload(COUNT_PAYLOAD), "GuavaEvents(1ms Payload)");
    }

    private void testNextEvent(TestPayload payload, Scheduler schedule, String tag){
        final NextEvents nextEvents = new NextEvents(schedule);

        nextEvents.register(payload);

        final long timeBeforeEmits = NOW();
        for (int i = 0; i < payload.perEvtCount; i++) {

            final long longEvent = NOW();
            nextEvents.emit("long", longEvent);

            final String strEvent = String.valueOf(NOW());
            nextEvents.emit("str", strEvent);
        }

        final long timeAfterEmits = NOW();

        try {
            payload.await();
        } catch (InterruptedException e) {
            fail(tag + ", Wait fail");
        }

        nextEvents.unregister(payload);

        assertThat(payload.evt1Calls.get(), equalTo(payload.perEvtCount));
        assertThat(payload.evt2Calls.get(), equalTo(payload.perEvtCount));

        printStatistics(tag, timeBeforeEmits, timeAfterEmits, payload.totalCalls);
    }

    private void testGuava(TestPayload payload, String tag){
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

}
