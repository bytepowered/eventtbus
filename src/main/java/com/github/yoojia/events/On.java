package com.github.yoojia.events;


/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.1
 */
public enum On {

    /**
     * 由主线程执行回调
     */
    MAIN_THREAD,

    /**
     * 由调用者线程执行回调
     */
    CALLER_THREAD,

    /**
     * 由线程池的线程执行回调
     */
    IO_THREAD;

}
