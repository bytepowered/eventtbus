package com.github.yoojia.events.internal;

import com.github.yoojia.events.supports.Filter;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import static com.github.yoojia.events.supports.Functions.filter;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.2
 */
public class Dispatcher {

    private final Emitter mEmitter;

    final Scheduler handlerScheduler;
    final CopyOnWriteArrayList<Acceptor> acceptors = new CopyOnWriteArrayList<>();
    final CopyOnWriteArrayList<OnEventHandler> handlers = new CopyOnWriteArrayList<>();

    public Dispatcher(){
        this(new SchedulerImpl());
    }

    public Dispatcher(Scheduler schedule) {
        handlerScheduler = schedule;
        mEmitter = new Emitter(this);
    }

    public void emit(Object event) {
        mEmitter.submit(event, null);
    }

    public void addHandler(Handler handler, EventFilter filter) {
        addHandler(handler, Arrays.asList(new EventFilter[]{filter}));
    }

    public void addHandler(Handler handler, List<EventFilter> filters) {
        acceptors.add(new Acceptor(handler, filters));
    }

    public void removeHandler(final Handler handler) {
        acceptors.removeAll(filter(acceptors, new Filter<Acceptor>(){
            @Override
            public boolean accept(Acceptor item) {
                return item.handler.equals(handler);
            }
        }));
    }

    public void addOnEventHandler(OnEventHandler handler) {
        handlers.add(handler);
    }

    public void removeOnEventHandler(OnEventHandler handler) {
        handlers.remove(handler);
    }

}
