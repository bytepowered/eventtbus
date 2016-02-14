package com.github.yoojia.events.core;

import java.util.concurrent.*;

/**
 * 任务调度器
 * @author YOOJIA.CHEN (yoojiachen@gmail.com)
 */
public final class Schedules {

    /**
     * 调用者调度器
     * @return Schedule
     */
    public static Schedule newCaller() {
        return new Schedule() {
            @Override
            public void invoke(Callable<Void> task, int flags) throws Exception {
                if (FLAG_ON_CALLER_THREAD != flags) {
                    throw new IllegalArgumentException("Caller schedule just accept <Schedule.FLAG_ON_CALLER_THREAD>");
                }
                task.call();
            }
        };
    }

    /**
     * 指定ExecutorService实现的高度器
     * @param service ExecutorService
     * @return Schedule
     */
    public static Schedule newService(final ExecutorService service) {
        return new Schedule() {

            private final ExecutorService mThreads = service;

            @Override
            public void invoke(Callable<Void> task, int scheduleFlags) throws Exception {
                invokeDispatch(mThreads, task, scheduleFlags);
            }

        };
    }

    /**
     * 全局共享线程池调度器
     * @return Schedule
     */
    public static Schedule sharedThreads(){
        return SharedSchedule.getDefault(InnerSharedSchedule.class);
    }

    /**
     * 关闭共享线程池。在确保不会再使用NextEvents的情况下调用。
     */
    public static void shutdownSharedThreads(){
        SharedSchedule.shutdown();
    }

    /**
     * 根据调度标记，调度各个任务。
     * @param threads 线程池
     * @param task 任务
     * @param scheduleFlags 调度标记
     * @throws Exception
     */
    private static void invokeDispatch(ExecutorService threads, final Callable<Void> task, int scheduleFlags) throws Exception{
        switch (scheduleFlags) {
            case Schedule.FLAG_ON_CALLER_THREAD:
                task.call();
                break;
            case Schedule.FLAG_ON_THREADS:
                threads.submit(task);
                break;
            case Schedule.FLAG_ON_MAIN_THREAD:
                throw new UnsupportedOperationException("Unsupported run on main thread");
            default:
                throw new IllegalArgumentException("Unsupported Schedule flags: " + scheduleFlags);
        }
    }

    static class InnerSharedSchedule extends SharedSchedule {

        @Override
        protected void invokeThreading(ThreadPoolExecutor threads, Callable<Void> task, int flags) throws Exception {
            invokeDispatch(threads, task, flags);
        }
    }

}
