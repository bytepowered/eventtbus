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

    public final Object[] values;

    public final Class<?>[] types;

    public PayloadEvent(String name, Object payload) {
        notEmpty(name, "name is empty");
        notNull(payload, "payload == null");
        this.name = name;
        final Class<?> payloadType = payload.getClass();
        if (payloadType.isArray()) {
            values = (Object[]) payload;
            types = new Class[values.length];
            for (int i = 0; i < values.length; i++) {
                types[i] = ClassTypes.wrap(values[i].getClass());
            }
        }else{
            types = new Class[]{ClassTypes.wrap(payloadType)};
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
