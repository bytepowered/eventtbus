package com.github.yoojia.events.core;

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

    private final ScheduleLooper mLooper = new ScheduleLooper() {
        @Override
        protected void step() {
            final Element element = mLoopTasks.poll();
            if (element == null) {
                await();
            }else{
                invoke(element.handler.scheduleType(), element.event, element.handler);
            }
        }
    };

    public Schedule() {
        mWorkerThreads = null;
    }

    public Schedule(ExecutorService loopThread, ExecutorService workerThreads) {
        mWorkerThreads = workerThreads;
        loopThread.submit(mLooper);
    }

    public void submit(EventMessage event, List<EventHandler> handlers) {
        for (EventHandler handler : handlers) {
            // 根据调度类型执行最终运行方式
            final int type = handler.scheduleType();
            // Caller方式,直接执行. 其它方式在线程池处理
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

    public ExecutorService getWorkerThreads(){
        return mWorkerThreads;
    }

    protected void invoke(int type, EventMessage event, EventHandler handler) {
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

    private static class Element {

        public final EventMessage event;
        public final EventHandler handler;

        private Element(EventMessage event, EventHandler handler) {
            this.event = event;
            this.handler = handler;
        }

    }

}
