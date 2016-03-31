package com.github.yoojia.events;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.0
 */
class Element {

    public final Object event;
    public final EventHandler handler;
    public final int scheduleType;

    public Element(Object event, EventHandler handler, int scheduleType) {
        this.event = event;
        this.handler = handler;
        this.scheduleType = scheduleType;
    }

}
