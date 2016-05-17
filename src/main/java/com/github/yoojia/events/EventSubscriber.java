package com.github.yoojia.events;

import com.github.yoojia.events.emitter.Subscriber;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.4
 */
public interface EventSubscriber extends Subscriber {

    /**
     * 自定义事件处理Handler的线程调度类型
     * @return schedule on type
     */
    On scheduleOn();
}
