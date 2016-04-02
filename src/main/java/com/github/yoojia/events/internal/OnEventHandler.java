package com.github.yoojia.events.internal;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.0
 */
public interface OnEventHandler {

    /**
     * 处理事件。
     * @param event
     * @return
     */
    boolean handleEvent(Object event);

}
