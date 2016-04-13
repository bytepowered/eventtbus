package com.github.yoojia.events.emitter;

import com.github.yoojia.events.supports.Filter;
import com.github.yoojia.events.supports.ImmutableList;

import java.util.List;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.3
 */
public class Target implements Filter<Object>{

    final Handler handler;
    final ImmutableList<EventFilter> filters;

    public Target(Handler handler, List<EventFilter> source) {
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
