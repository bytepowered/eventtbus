package com.github.yoojia.events.internal;

/**
 * @author Yoojia Chen (yoojia.chen@gmail.com)
 * @since 2.0
 */
public final class DeadEvent {

    public final Object raw;

    public DeadEvent(Object raw) {
        this.raw = raw;
    }

    @Override
    public String toString() {
        return raw == null ? "null" : raw.toString();
    }
}
