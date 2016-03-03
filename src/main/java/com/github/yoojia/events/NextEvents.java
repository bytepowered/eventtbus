package com.github.yoojia.events;

import com.github.yoojia.events.internal.*;
import com.github.yoojia.events.supports.Filter;

import java.lang.reflect.Method;
import java.util.List;

import static com.github.yoojia.events.supports.Preconditions.notNull;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.2
 */
public class NextEvents {

    private final Dispatcher mDispatcher;
    private final Cached mCached = new Cached();

    public NextEvents() {
        this(SharedSchedule.getDefault());
    }

    public NextEvents(Schedule schedule) {
        notNull(schedule, "schedule == null");
        mDispatcher = new Dispatcher(schedule);
    }

    public void register(Object object) {
        register(object, null);
    }

    public void register(Object object, Filter<Method> filter) {
        notNull(object, "object == null");
        final List<Acceptor> acceptors = mCached.find(object, filter);
        for (Acceptor acceptor : acceptors) {
            mDispatcher.addHandler(acceptor.handler, acceptor.filters);
        }
    }

    public void unregister(Object object) {
        notNull(object, "object == null");
        final List<Acceptor> acceptors = mCached.getSafety(object);
        for (Acceptor acceptor : acceptors) {
            mDispatcher.removeHandler(acceptor.handler);
        }
        mCached.remove(object);
    }

    public void emit(Event event) {
        notNull(event, "event == null");
        mDispatcher.emit(new EventMessage(event));
    }

    public void addHandler(EventHandler handler, EventFilter filter) {
        notNull(handler, "handler == null");
        notNull(filter, "filter == null");
        mDispatcher.addHandler(handler, filter);
    }

    public void addHandler(EventHandler handler, List<EventFilter> filters) {
        notNull(handler, "handler == null");
        notNull(filters, "filters == null");
        mDispatcher.addHandler(handler, filters);
    }

}
