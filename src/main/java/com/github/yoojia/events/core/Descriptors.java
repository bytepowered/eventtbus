package com.github.yoojia.events.core;

import com.github.yoojia.events.Filter;

import java.util.Arrays;
import java.util.List;

/**
 * 订阅者构建帮助类
 * @author YOOJIA.CHEN (yoojiachen@gmail.com)
 */
public final class Descriptors {

    public static <T> Descriptor<T> create1(Subscriber<T> target, int scheduleFlags, Filter<T> filter1) {
        return createWithFilters(target, scheduleFlags, filter1);
    }

    public static <T> Descriptor<T> create2(Subscriber<T> target, int scheduleFlags, Filter<T> filter1, Filter<T> filter2) {
        return createWithFilters(target, scheduleFlags, filter1, filter2);
    }

    public static <T> Descriptor<T> createWithFilters(Subscriber<T> target, int scheduleFlags, Filter<T>... filters) {
        return createWithFilters(target, scheduleFlags, Arrays.asList(filters));
    }

    public static <T> Descriptor<T> createWithFilters(Subscriber<T> target, int scheduleFlags, List<Filter<T>> filters) {
        return new Descriptor<>(target, scheduleFlags, filters);
    }
}
