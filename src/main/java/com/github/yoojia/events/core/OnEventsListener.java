package com.github.yoojia.events.core;

/**
 * Emit Miss
 *
 * @author YOOJIA.CHEN (yoojiachen@gmail.com)
 */
public interface OnEventsListener<T> {

    void onWithoutSubscriber(T input);
}
