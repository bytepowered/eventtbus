package com.github.yoojia.events;


/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.0
 */
public enum Runs {

    /**
     * 由主线程执行回调
     */
    ON_MAIN_THREAD(ScheduleType.ON_MAIN_THREAD),

    /**
     * 由调用者线程执行回调
     */
    ON_CALLER_THREAD(ScheduleType.ON_CALLER_THREAD),

    /**
     * 由线程池的线程执行回调
     */
    ON_THREADS(ScheduleType.ON_THREADS);

    final int scheduleFlag;

    Runs(int flag) {
        this.scheduleFlag = flag;
    }

}
