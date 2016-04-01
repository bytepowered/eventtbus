package com.github.yoojia.events;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.2
 */
public class SharedSchedule extends ThreadsSchedule {

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
    protected static SharedSchedule DEF_SCHEDULE;

    public static SharedSchedule getDefault() {
        return getDefault(SharedSchedule.class);
    }

    public static SharedSchedule getDefault(Class<? extends SharedSchedule> type){
        synchronized (SharedSchedule.class) {
            if (DEF_SCHEDULE == null) {
                QUEUE = new LinkedBlockingQueue<>();
                EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
                        TimeUnit.SECONDS, QUEUE, THREAD_FACTORY);
                if (SharedSchedule.class.equals(type)) {
                    DEF_SCHEDULE = new SharedSchedule();
                }else{
                    try {
                        DEF_SCHEDULE = type.newInstance();
                    } catch (Exception cause) {
                        throw new RuntimeException("When create shared schedule instance", cause);
                    }
                }
            }
            return DEF_SCHEDULE;
        }
    }

    public SharedSchedule() {
        super(EXECUTOR, EXECUTOR);
    }

    public static void shutdown(){
        EXECUTOR.shutdown();
    }

}
