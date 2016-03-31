package com.github.yoojia.events;

import com.github.yoojia.events.internal.Handler;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.0
 */
public interface EventHandler extends Handler {

    int scheduleType();
}
