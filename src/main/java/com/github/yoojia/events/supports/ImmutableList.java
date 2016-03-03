package com.github.yoojia.events.supports;

import java.util.AbstractList;
import java.util.RandomAccess;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.3
 */
public class ImmutableList<T> extends AbstractList<T> implements RandomAccess {

    private final T[] mData;

    public ImmutableList(T[] data) {
        if (data == null) {
            throw new NullPointerException();
        }
        this.mData = data;
    }

    @Override
    public T get(int index) {
        return mData[index];
    }

    @Override
    public int size() {
        return mData.length;
    }

}
