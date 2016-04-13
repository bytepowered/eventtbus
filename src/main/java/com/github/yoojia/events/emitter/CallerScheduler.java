package com.github.yoojia.events.emitter;

import java.util.List;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.3
 */
public final class CallerScheduler implements Scheduler {

    @Override
    public void schedule(Object event, List<? extends Handler> handlers) {
        for (Handler handler : handlers) {
            new Invoker(event, handler).run();
        }
    }

}
