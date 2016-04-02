package com.github.yoojia.events;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.0
 */
public interface ScheduleType {

    public static final int ON_MAIN_THREAD = 1000;
    public static final int ON_CALLER_THREAD = 2000;
    public static final int ON_IO_THREAD = 3000;
}
