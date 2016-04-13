package com.github.yoojia.events.internal;

import java.util.List;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.0
 */
public final class SchedulerImpl implements Scheduler {

    @Override
    public void submit(Object event, List<? extends Handler> handlers) {
        for (Handler handler : handlers) {
            new Invoker(event, handler).run();
        }
    }

}
