package com.github.yoojia.events;

import com.github.yoojia.events.internal.Handler;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.0
 */
public interface EventHandler extends Handler {

    /**
     * 自定义事件处理Handler的线程调度类型
     * @return On type
     */
    On scheduleOn();
}
