package com.github.yoojia.events.supports;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.0
 */
public class Preconditions {

    public static Object checkNull(Object obj, String message) {
        notNull(obj, message);
        return obj;
    }

    public static void notNull(Object obj, String message) {
        if (obj == null) {
            throw new NullPointerException(message);
        }
    }

    public static Object checkNull(Object obj) {
        notNull(obj);
        return obj;
    }

    public static void notNull(Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
    }

    public static void notEmpty(CharSequence value, String message) {
        if (value == null || value.length() == 0) {
            throw new IllegalArgumentException(message);
        }
    }
}
