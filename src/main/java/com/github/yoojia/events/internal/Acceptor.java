package com.github.yoojia.events.internal;

import com.github.yoojia.events.supports.Filter;
import com.github.yoojia.events.supports.ImmutableList;

import java.util.List;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.2
 */
public class Acceptor implements Filter<Object>{

    public final EventHandler handler;
    public final ImmutableList<EventFilter> filters;

    private final int mCount;

    public Acceptor(EventHandler handler, List<EventFilter> source) {
        this.handler = handler;
        mCount = source.size();
        this.filters = new ImmutableList<>(source.toArray(new EventFilter[mCount]));
    }

    @Override
    public boolean accept(Object event) {
        for (int i = 0; i < mCount; i++) {
            final EventFilter f = filters.get(i);
            if (! f.accept(event)) return false;
        }
        return true;
    }
}
