package com.github.yoojia.events;

/**
 * @author 陈小锅 (yoojia.chen@gmail.com)
 * @since 1.0
 */
public interface Filter<T> {
    /**
     * @param item Item
     * @return TRUE if accept this item
     */
    boolean accept(T item);
}
