package com.github.yoojia.events;

import com.github.yoojia.events.supports.ClassTypes;

/**
 * @author YOOJIA.CHEN (yoojia.chen@gmail.com)
 * @since 1.1
 */
public class Event {

    /**
     * Event name
     */
    public final String name;

    /**
     * Event value (payload)
     */
    public final Object value;

    /**
     * Event valueType (Wrapper type)
     */
    public final Class<?> valueType;

    Event(String name, Object value) {
        this.name = name;
        this.value = value;
        final Class<?> type = value.getClass();
        this.valueType = ClassTypes.wrap(type);
    }

    @Override
    public String toString() {
        return "{name=" + name +  ", value=" + value + "}";
    }

    static Event create(String name, Object value) {
        return new Event(name, value);
    }
}
