package com.github.yoojia.events;

import com.github.yoojia.events.supports.ClassTypes;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.2
 */
public class Event {

    /**
     * Event name
     */
    public final String name;

    /**
     * Event payload
     */
    public final Object payload;

    /**
     * Event valueType (Wrapper type)
     */
    public final Class<?> valueType;

    Event(String name, Object payload) {
        this.name = name;
        this.payload = payload;
        this.valueType = ClassTypes.wrap(payload.getClass());
    }

    @Override
    public String toString() {
        return "{name=" + name +  ", value=" + payload + "}";
    }

}
