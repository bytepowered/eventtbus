package com.github.yoojia.events.internal;

import java.util.List;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.0
 */
public interface Scheduler {

    public static final int ON_MAIN_THREAD = 1000;
    public static final int ON_CALLER_THREAD = 2000;
    public static final int ON_THREADS = 3000;

    void submit(Object event, List<EventHandler> handlers);

}
