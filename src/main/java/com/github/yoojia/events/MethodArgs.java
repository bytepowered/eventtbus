package com.github.yoojia.events;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.3
 */
final class MethodArgs {

    public final On scheduleOn;
    public final Class<?>[] types;
    public final String name;
    public final boolean isAnyType;
    public final boolean isEmptyArgs;

    MethodArgs(On scheduleOn, Class<?>[] types, String name) {
        this.scheduleOn = scheduleOn;
        this.types = types;
        this.name = name;
        this.isEmptyArgs = types.length == 0;
        this.isAnyType = types.length == 1 && Any.class.equals(types[0]);
    }
}
