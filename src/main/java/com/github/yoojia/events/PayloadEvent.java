package com.github.yoojia.events;

import java.util.Arrays;

import static com.github.yoojia.events.supports.Preconditions.notEmpty;
import static com.github.yoojia.events.supports.Preconditions.notNull;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.2
 */
public class PayloadEvent {

    public static final String DEAD_EVENT = "next.events.<DEAD-EVENT>";

    public final String name;

    public final Object[] values;

    public final Class<?>[] types;

    public PayloadEvent(String name, Object payload) {
        notEmpty(name, "name is empty");
        notNull(payload, "payload == null");
        this.name = name;
        final Class<?> type = payload.getClass();
        if (type.isArray()) {
            values = (Object[]) payload;
            types = new Class[values.length];
            for (int i = 0; i < values.length; i++) {
                types[i] = values[i].getClass();
            }
        }else{
            types = new Class[]{type};
            values = new Object[]{payload};
        }
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", values=" + Arrays.toString(values) +
                '}';
    }
}
