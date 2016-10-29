package com.github.yoojia.events.supports;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.0
 */
public class Preconditions {

    public static <T> T notNull(T obj, String message) {
        if (obj == null) {
            throw new NullPointerException(message);
        }
        return obj;
    }

    public static void notNull(Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
    }

    public static <T extends CharSequence> T notEmpty(T value, String message) {
        if (value == null || value.length() == 0) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }
}
