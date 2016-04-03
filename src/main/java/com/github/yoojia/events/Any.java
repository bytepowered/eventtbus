package com.github.yoojia.events;

import java.util.Arrays;

/**
 * @author Yoojia Chen (yoojia.chen@gmail.com)
 * @since 2.2
 */
public final class Any {

    public final Object[] values;
    public final Class[] types;

    public Any(Object[] values, Class[] types) {
        this.values = values;
        this.types = types;
    }

    @Override
    public String toString() {
        return "{" +
                "values=" + Arrays.toString(values) +
                ", types=" + Arrays.toString(types) +
                '}';
    }
}
