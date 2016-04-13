package com.github.yoojia.events.emitter;

/**
 * @author Yoojia Chen (yoojia.chen@gmail.com)
 * @since 2.0
 */
public final class DeadEvent {

    public final Object origin;

    public DeadEvent(Object origin) {
        this.origin = origin;
    }

    @Override
    public String toString() {
        return origin == null ? "null" : origin.toString();
    }
}
