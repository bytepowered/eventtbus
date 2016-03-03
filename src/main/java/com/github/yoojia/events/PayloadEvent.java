package com.github.yoojia.events;

import com.github.yoojia.events.supports.ClassTypes;

import static com.github.yoojia.events.supports.Preconditions.notEmpty;
import static com.github.yoojia.events.supports.Preconditions.notNull;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.2
 */
public class PayloadEvent {

    /**
     * Event name
     */
    public final String name;

    /**
     * Event payload value
     */
    public final Object payloadValue;

    /**
     * Event payload value type (Wrapper type)
     */
    public final Class<?> payloadType;

    public PayloadEvent(String name, Object payload) {
        notEmpty(name, "name is empty");
        notNull(payload, "payload value is null");
        this.name = name;
        this.payloadValue = payload;
        this.payloadType = ClassTypes.wrap(payload.getClass());
    }

    @Override
    public String toString() {
        return "{name=" + name +  ", value=" + payloadValue + "}";
    }

}
