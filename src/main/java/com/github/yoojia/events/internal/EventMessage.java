package com.github.yoojia.events.internal;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.2
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