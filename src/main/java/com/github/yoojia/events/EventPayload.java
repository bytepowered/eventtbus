package com.github.yoojia.events;

import java.util.Arrays;

import static com.github.yoojia.events.supports.Preconditions.notEmpty;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.2
 */
public class EventPayload {

    public static final String DEAD_EVENT = "next.events.dead-event";

    public final String name;
    public final String origin;

    public final Object[] values;
    public final Class<?>[] types;

    EventPayload(String name, EventPayload src) {
        this.name = notEmpty(name, "name not allow empty");
        this.origin = src.name;
        this.values = src.values;
        this.types = src.types;
    }

    public EventPayload(String name, Object payload) {
        this.name = notEmpty(name, "name not allow empty");
        this.origin = null;
        final Object[] parsed = parse(payload);
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

    private static Object[] parse(Object payloads) {
        final Object[] values;
        final Class[] types;
        if (payloads != null) {
            final Class<?> type = payloads.getClass();
            // 多个事件负载时，负载列表中不能包含null
            if (type.isArray()) {
                values = (Object[]) payloads;
                types = new Class[values.length];
                for (int i = 0; i < values.length; i++) {
                    if (null == values[i]) {
                        throw new IllegalArgumentException("When emit MULTI-PAYLOADS, values CANNOT contains <NULL> value !");
                    }
                    types[i] = values[i].getClass();
                }
            }else{ // single
                types = new Class[]{type};
                values = new Object[]{payloads};
            }
        }else{ // no args
            values = new Object[0];
            types = new Class[0];
        }
        return new Object[]{values, types};
    }
}
