package com.github.yoojia.events;

import com.github.yoojia.events.core.*;

import java.util.List;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.3
 */
public class NextEvents {

    private final Dispatcher mDispatcher;
    private final ObjectAccessor mObjectAccessor;

    public NextEvents() {
        this(SharedSchedule.getDefault());
    }

    public NextEvents(Schedule schedule) {
        mDispatcher = new Dispatcher(schedule);
        mObjectAccessor = new ObjectAccessor();
    }

    public void addHandler(EventHandler handler, EventFilter filter) {
        mDispatcher.addHandler(handler, filter);
    }

    public void addHandler(EventHandler handler, List<EventFilter> filters) {
        mDispatcher.addHandler(handler, filters);
    }

    public void register(Object object) {
        final List<Acceptor> acceptors = mObjectAccessor.find(object);
        for (Acceptor acceptor : acceptors) {
            mDispatcher.addHandler(acceptor.handler, acceptor.filters);
        }
    }

    public void unregister(Object object) {
        final List<Acceptor> acceptors = mObjectAccessor.getPresent(object);
        for (Acceptor acceptor : acceptors) {
            mDispatcher.removeHandler(acceptor.handler);
        }
    }

    public void emit(Event event) {
        mDispatcher.emit(new EventMessage(event));
    }
}
