package com.github.yoojia.events.internal;

import java.util.List;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.0
 */
public interface Scheduler {

    void submit(Object event, List<? extends Handler> handlers);

}
