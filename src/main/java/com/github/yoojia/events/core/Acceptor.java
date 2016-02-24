package com.github.yoojia.events.core;

import com.github.yoojia.events.supports.Filter;

import java.util.List;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.3
 */
public class Acceptor implements Filter<EventMessage>{

    public final EventHandler handler;

    public final List<EventFilter> filters;

    public Acceptor(EventHandler handler, List<EventFilter> filters) {
        this.handler = handler;
        this.filters = filters;
    }

    @Override
    public boolean accept(EventMessage event) {
        for (EventFilter filter : this.filters) {
            if ( ! filter.accept(event)) {
                return false;
            }
        }
        return true;
    }
}
