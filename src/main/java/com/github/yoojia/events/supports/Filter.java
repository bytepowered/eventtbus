package com.github.yoojia.events.supports;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.0
 */
public interface Filter<T> {
    /**
     * @param item Item
     * @return TRUE if accept this item
     */
    boolean accept(T item);
}
