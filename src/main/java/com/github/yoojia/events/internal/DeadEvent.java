package com.github.yoojia.events.internal;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.0
 */
public final class DeadEvent {

    public static final String NAME = "next-event.dead-event";

    public final Object rawEvent;

    public DeadEvent(Object rawEvent) {
        this.rawEvent = rawEvent;
    }

    @Override
    public String toString() {
        return "{" +
                "rawEvent=" + rawEvent +
                '}';
    }
}
