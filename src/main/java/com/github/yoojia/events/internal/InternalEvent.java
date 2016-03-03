package com.github.yoojia.events.internal;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.3
 */
public class InternalEvent {

    private final Object mValue;

    public InternalEvent(Object event) {
        mValue = event;
    }

    public Object getValue() {
        return mValue;
    }

}
