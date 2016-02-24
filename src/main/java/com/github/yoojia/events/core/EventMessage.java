package com.github.yoojia.events.core;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.3
 */
public class EventMessage{

    private final Object mEvent;

    public EventMessage(Object event) {
        mEvent = event;
    }

    public Object getEvent() {
        return mEvent;
    }

}
