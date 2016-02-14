package com.github.yoojia.events.core;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class SharedSchedule implements Schedule {

    protected static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    protected static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    protected static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    protected static final int KEEP_ALIVE = 1;

    protected static final ThreadFactory THREAD_FACTORY = new ThreadFactory() {

        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "SharedThread #" + mCount.getAndIncrement());
        }
    };

    protected static BlockingQueue<Runnable> QUEUE;
    protected static ThreadPoolExecutor EXECUTOR;

    private static SharedSchedule mDefaultSchedule;

    public static SharedSchedule getDefault(Class<? extends SharedSchedule> type){
        synchronized (SharedSchedule.class) {
            if (mDefaultSchedule == null) {
                QUEUE = new LinkedBlockingQueue<>();
                EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, QUEUE, THREAD_FACTORY);
                try {
                    mDefaultSchedule = type.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            return mDefaultSchedule;
        }
    }

    @Override
    final public void invoke(Callable<Void> task, int flags) throws Exception {
        invokeThreading(EXECUTOR, task, flags);
    }

    public static void shutdown(){
        EXECUTOR.shutdown();
    }

    protected abstract void invokeThreading(ThreadPoolExecutor threads, Callable<Void> task, int flags) throws Exception;
}