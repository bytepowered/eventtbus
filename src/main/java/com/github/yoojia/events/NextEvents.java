package com.github.yoojia.events;

import com.github.yoojia.events.core.*;

import java.util.List;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.3
 */
public class NextEvents {

    private final Dispatcher mDispatcher;
    private final Cached mCached;

    public NextEvents() {
        this(SharedSchedule.getDefault());
    }

    public NextEvents(Schedule schedule) {
        mDispatcher = new Dispatcher(schedule);
        mCached = new Cached();
    }

    public void addHandler(EventHandler handler, EventFilter filter) {
        mDispatcher.addHandler(handler, filter);
    }

    public void addHandler(EventHandler handler, List<EventFilter> filters) {
        mDispatcher.addHandler(handler, filters);
    }

    public void register(Object object) {
        final List<Acceptor> acceptors = mCached.find(object);
        for (Acceptor acceptor : acceptors) {
            mDispatcher.addHandler(acceptor.handler, acceptor.filters);
        }
    }

    public void unregister(Object object) {
        final List<Acceptor> acceptors = mCached.getPresent(object);
        for (Acceptor acceptor : acceptors) {
            mDispatcher.removeHandler(acceptor.handler);
        }
    }

    public void emit(Event event) {
        mDispatcher.emit(new EventMessage(event));
    }
}
