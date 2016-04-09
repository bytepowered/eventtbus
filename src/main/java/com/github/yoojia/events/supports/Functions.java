package com.github.yoojia.events.supports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.2
 */
public class Functions {

    @SuppressWarnings("unchecked")
    public static <T> List<T> filter(List<T> src, Filter<T> func) {
        if (src == null || src.isEmpty()) {
            return Collections.emptyList();
        }else{
            final int size = src.size();
            final List<T> output = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                final T item = src.get(i);
                if (func.accept(item)) {
                    output.add(item);
                }
            }
            return output;
        }
    }

    public static <S, M> List<M> map(List<S> src, Transformer<S, M> func){
        if (src == null || src.isEmpty()) {
            return Collections.emptyList();
        }else{
            final int size = src.size();
            final List<M> output = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                output.add(func.transform(src.get(i)));
            }
            return output;
        }
    }
}
