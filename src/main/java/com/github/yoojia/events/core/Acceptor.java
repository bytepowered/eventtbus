package com.github.yoojia.events.core;

import com.github.yoojia.events.supports.Filter;

import java.util.List;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.2
 */
public class Acceptor implements Filter<EventMessage>{

    public final EventHandler handler;
    public final List<EventFilter> filters;

    private final int mSize;

    public Acceptor(EventHandler handler, List<EventFilter> filters) {
        this.handler = handler;
        this.filters = filters;
        mSize = filters.size();
    }

    @Override
    public boolean accept(EventMessage event) {
        for (int i = 0; i < mSize; i++) {
            final EventFilter f = filters.get(i);
            if (! f.accept(event)) return false;
        }
        return true;
    }
}
