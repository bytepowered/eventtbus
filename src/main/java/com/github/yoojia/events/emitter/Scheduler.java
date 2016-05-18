package com.github.yoojia.events.emitter;

import java.util.List;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.0
 */
public interface Scheduler {

    void schedule(Object event, List<? extends Subscriber> handlers);

}
