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

    public Acceptor(EventHandler handler, List<EventFilter> source) {
        this.handler = handler;
        this.filters = new ImmutableList<>(source.toArray(new EventFilter[source.size()]));
    }

    @Override
    public boolean accept(Object event) {
        final int size = filters.size();
        for (int i = 0; i < size; i++) {
            final EventFilter f = filters.get(i);
            if (! f.accept(event)) {
                return false;
            }
        }
        return true;
    }
}
