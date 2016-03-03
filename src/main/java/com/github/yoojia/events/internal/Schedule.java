package com.github.yoojia.events.internal;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.2
 */
public class Schedule {

    public static final int ON_MAIN_THREAD = 1000;
    public static final int ON_CALLER_THREAD = 2000;
    public static final int ON_THREADS = 3000;

    private final Queue<Element> mLoopTasks = new ConcurrentLinkedQueue<>();

    private final ExecutorService mWorkerThreads;
    private final ExecutorService mLoopThread;

    private final ScheduleLooper mLooper = new ScheduleLooper() {
        @Override
        protected void step() {
            final Element e = mLoopTasks.poll();
            if (e == null) {
                await();
            }else{
                invoke(e.handler.scheduleType(), e.event, e.handler);
            }
        }
    };

    public Schedule() {
        mWorkerThreads = null;
        mLoopThread = null;
    }

    public Schedule(ExecutorService loopThread, ExecutorService workerThreads) {
        mWorkerThreads = workerThreads;
        mLoopThread = loopThread;
        mLoopThread.submit(mLooper);
    }

    public void submit(InternalEvent event, List<EventHandler> handlers) {
        // 如果是 CALLER 方式, 直接在此处执行.
        // 其它方式在线程池处理再做处理
        for (EventHandler handler : handlers) {
            final int type = handler.scheduleType();
            if (Schedule.ON_CALLER_THREAD == type) {
                new EventRunner(event, handler).run();
            }else{
                mLoopTasks.offer(new Element(event, handler));
                synchronized (mLooper) {
                    mLooper.notify();
                }
            }
        }
    }

    public final ExecutorService getWorkerThreads(){
        return mWorkerThreads;
    }

    public final ExecutorService getLoopThread() {
        return mLoopThread;
    }

    protected void invoke(int type, InternalEvent event, EventHandler handler) {
        switch (type) {
            case Schedule.ON_THREADS:
                mWorkerThreads.submit(new EventRunner(event, handler));
                break;
            case Schedule.ON_MAIN_THREAD:
                handler.onErrors(new IllegalArgumentException("Unsupported <ON_MAIN_THREAD> schedule type! " ));
                break;
            default:
                handler.onErrors(new IllegalArgumentException("Unsupported schedule type: " + type));
                break;
        }
    }

    private final static class Element {

        public final InternalEvent event;
        public final EventHandler handler;

        private Element(InternalEvent event, EventHandler handler) {
            this.event = event;
            this.handler = handler;
        }

    }

}
