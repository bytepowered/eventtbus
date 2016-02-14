package com.github.yoojia.events;

import com.github.yoojia.events.core.Schedule;

/**
 * @author YOOJIA.CHEN (yoojia.chen@gmail.com)
 */
public enum Runs {

    /**
     * 由调用者线程执行回调
     */
    ON_MAIN_THREAD(Schedule.FLAG_ON_MAIN_THREAD),

    /**
     * 由调用者线程执行回调
     */
    ON_CALLER_THREAD(Schedule.FLAG_ON_CALLER_THREAD),

    /**
     * 由线程池的线程执行回调
     */
    ON_THREADS(Schedule.FLAG_ON_THREADS);

    final int scheduleFlag;

    Runs(int flag) {
        this.scheduleFlag = flag;
    }

}
