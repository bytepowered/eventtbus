package com.github.yoojia.events.emitter;

import com.github.yoojia.events.supports.Filter;
import com.github.yoojia.events.supports.ImmutableList;

import java.util.List;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.4
 */
public class RealSubscriber implements Filter<Object>{

    final Subscriber subscriber;
    final ImmutableList<EventFilter> filters;

    public RealSubscriber(Subscriber subscriber, List<EventFilter> source) {
        this.subscriber = subscriber;
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
