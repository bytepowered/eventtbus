package com.github.yoojia.events;

import java.util.Arrays;

import static com.github.yoojia.events.supports.Preconditions.notEmpty;
import static com.github.yoojia.events.supports.Preconditions.notNull;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.2
 */
public class EventPayload {

    public static final String DEAD_EVENT = "next.events.<dead-event>";

    public final String name;
    public final String origin;

    public final Object[] values;
    public final Class<?>[] types;

    EventPayload(String name, EventPayload src) {
        this.name = name;
        this.origin = src.name;
        this.values = src.values;
        this.types = src.types;
    }

    public EventPayload(String name, Object payload) {
        notEmpty(name, "name not allow empty");
        this.name = name;
        this.origin = null;
        final Object[] parsed = checkParse(payload);
        this.values = (Object[]) parsed[0];
        this.types = (Class[]) parsed[1];
    }

    @Override
    public String toString() {
        return "{ name='" + name + '\'' +
                (origin == null ? "" : ", origin='" + origin + "'") +
                ", values=" + Arrays.toString(values) +
                '}';
    }

    public static Object[] checkParse(Object payloads) {
        final Object[] values;
        final Class[] types;
        if (payloads != null) {
            final Class<?> type = payloads.getClass();
            if (type.isArray()) {
                values = (Object[]) payloads;
                types = new Class[values.length];
                for (int i = 0; i < values.length; i++) {
                    if (null == values[i]) {
                        throw new IllegalArgumentException("Payloads values CANNOT contains <null> value !");
                    }
                    types[i] = values[i].getClass();
                }
            }else{ // single
                types = new Class[]{type};
                values = new Object[]{payloads};
            }
        }else{
            values = new Object[0];
            types = new Class[0];
        }
        return new Object[]{values, types};
    }
}
