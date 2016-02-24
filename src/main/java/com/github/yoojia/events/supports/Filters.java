package com.github.yoojia.events.supports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.3
 */
public class Filters {

    @SuppressWarnings("unchecked")
    public static <T> List<T> filter(List<T> src, Filter<T> filter) {
        if (src == null || src.isEmpty()) {
            return Collections.emptyList();
        }else{
            final int size = src.size();
            final List<T> output = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                final T item = src.get(i);
                if (filter.accept(item)) {
                    output.add(item);
                }
            }
            return output;
        }
    }
}
