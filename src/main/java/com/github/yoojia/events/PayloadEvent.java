package com.github.yoojia.events;

import com.github.yoojia.events.supports.ClassTypes;

import java.util.Arrays;

import static com.github.yoojia.events.supports.Preconditions.notEmpty;
import static com.github.yoojia.events.supports.Preconditions.notNull;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.2
 */
public class PayloadEvent {

    public final String name;

    public final Object[] eventValues;

    public final Class<?>[] eventTypes;

    public PayloadEvent(String name, Object payload) {
        notEmpty(name, "name is empty");
        notNull(payload, "payload == null");
        this.name = name;
        final Class<?> payloadType = payload.getClass();
        if (payloadType.isArray()) {
            eventValues = (Object[]) payload;
            eventTypes = new Class[eventValues.length];
            for (int i = 0; i < eventValues.length; i++) {
                eventTypes[i] = ClassTypes.wrap(eventValues[i].getClass());
            }
        }else{
            eventTypes = new Class[]{ClassTypes.wrap(payloadType)};
            eventValues = new Object[]{payload};
        }
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", values=" + Arrays.toString(eventValues) +
                '}';
    }
}
