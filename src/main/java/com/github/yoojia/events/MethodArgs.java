package com.github.yoojia.events;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.2
 */
final class MethodArgs {

    public final int scheduleType;
    public final Class<?>[] defineTypes;
    public final String defineName;

    MethodArgs(int scheduleType, Class<?>[] defineTypes, String defineName) {
        this.scheduleType = scheduleType;
        this.defineTypes = defineTypes;
        this.defineName = defineName;
    }
}
