package com.github.yoojia.events.emitter;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.4
 */
public interface EventInterceptor {

    /**
     * 处理事件。
     * @param event 事件对象
     * @return 是否中断事件传递
     */
    boolean handle(Object event);

}
