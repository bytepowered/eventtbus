package com.github.yoojia.events;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.2
 */
final class MethodDefine {

    public final int scheduleType;
    public final Class<?>[] types;
    public final String name;

    MethodDefine(int scheduleType, Class<?>[] types, String name) {
        this.scheduleType = scheduleType;
        this.types = types;
        this.name = name;
    }
}
