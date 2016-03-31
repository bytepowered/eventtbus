package com.github.yoojia.events.internal;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.2
 */
public final class Scheduler0 implements Scheduler{

    @Override
    public void submit(Object event, List<EventHandler> handlers) {
        for (EventHandler handler : handlers) {
            final int type = handler.scheduleType();
            if (Scheduler0.ON_CALLER_THREAD != type) {
                Logger.debug("Schedule0", "Ignore <ScheduleType> on Caller Schedule");
            }
            new EventRunner(event, handler).run();
        }
    }

}
