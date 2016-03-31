package com.github.yoojia.events;

import com.github.yoojia.events.internal.Scheduler0;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.0
 */
public enum Runs {

    /**
     * 由主线程执行回调
     */
    ON_MAIN_THREAD(Scheduler0.ON_MAIN_THREAD),

    /**
     * 由调用者线程执行回调
     */
    ON_CALLER_THREAD(Scheduler0.ON_CALLER_THREAD),

    /**
     * 由线程池的线程执行回调
     */
    ON_THREADS(Scheduler0.ON_THREADS);

    final int scheduleFlag;

    Runs(int flag) {
        this.scheduleFlag = flag;
    }

}
