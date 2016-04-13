package com.github.yoojia.events;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.3
 */
final class MethodArgs {

    public final On scheduleOn;
    public final Class<?>[] types;
    public final String name;
    public final boolean isAny;
    public final boolean isNoArgs;

    MethodArgs(On scheduleOn, Class<?>[] types, String name) {
        this.scheduleOn = scheduleOn;
        this.types = types;
        this.name = name;
        this.isNoArgs = types.length == 0;
        this.isAny = types.length == 1 && Any.class.equals(types[0]);
    }
}
