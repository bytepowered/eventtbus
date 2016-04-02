package com.github.yoojia.events.internal;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.0
 */
public interface OnEventHandler {

    /**
     * 处理事件。
     * @param event 事件对象
     * @return 是否中断事件传递
     */
    boolean handleEvent(Object event);

}
