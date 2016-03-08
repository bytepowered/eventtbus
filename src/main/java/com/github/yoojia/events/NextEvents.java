package com.github.yoojia.events;

import com.github.yoojia.events.internal.*;
import com.github.yoojia.events.supports.Filter;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.github.yoojia.events.supports.Preconditions.notNull;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.2
 */
public class NextEvents extends Dispatcher{

    private final ObjectCached mObjectCached = new ObjectCached();
    private final AtomicReference<OnEventMissedListener> mLocalEventMissedListener = new AtomicReference<>();

    public NextEvents() {
        this(SharedSchedule.getDefault());
    }

    public NextEvents(Schedule schedule) {
        super(schedule);
        notNull(schedule, "schedule == null");
        setOnEventMissedListener(new OnEventMissedListener() {
            @Override
            public void onEvent(Object event) {
                final PayloadEvent payload = (PayloadEvent) event;
                // 非DeadEvent事件，包装为DeadEvent继续处理
                if (PayloadEvent.DEAD_EVENT.equals(payload.name)) {
                    final OnEventMissedListener listener = mLocalEventMissedListener.get();
                    if (listener != null) {
                        listener.onEvent(event);
                    }else{
                        Logger.debug("NextEvents", "- Dead event: " + event);
                    }
                }else{
                    emit(new PayloadEvent(PayloadEvent.DEAD_EVENT, event));
                }
            }
        });
    }

    public void register(Object object) {
        register(object, null);
    }

    public void register(Object object, Filter<Method> customMethodFilter) {
        notNull(object, "object == null");
        final List<Acceptor> acceptors = mObjectCached.find(object, customMethodFilter);
        for (Acceptor acceptor : acceptors) {
            super.addHandler(acceptor.handler, acceptor.filters);
        }
    }

    public void unregister(Object object) {
        notNull(object, "object == null");
        final List<Acceptor> acceptors = mObjectCached.getSafety(object);
        for (Acceptor acceptor : acceptors) {
            super.removeHandler(acceptor.handler);
        }
        mObjectCached.remove(object);
    }

    public void emit(String name, Object...payloads) {
        if (payloads == null || payloads.length == 0){
            throw new IllegalArgumentException("payloads is empty");
        }
        super.emit(new PayloadEvent(name, payloads));
    }

    public void emit(PayloadEvent event) {
        notNull(event, "event == null");
        super.emit(event);
    }

    @Override
    public void emit(Object event) {
        if (event instanceof PayloadEvent) {
            super.emit(event);
        }else {
            throw new IllegalArgumentException("Call emit(PayloadEvent) instead");
        }
    }

    @Override
    public void setOnEventMissedListener(OnEventMissedListener listener) {
        notNull(listener, "listener == null");
        mLocalEventMissedListener.set(listener);
    }

    @Override
    public void addHandler(EventHandler handler, EventFilter filter) {
        notNull(handler, "handler == null");
        notNull(filter, "filter == null");
        super.addHandler(handler, filter);
    }

    @Override
    public void addHandler(EventHandler handler, List<EventFilter> filters) {
        notNull(handler, "handler == null");
        notNull(filters, "filters == null");
        super.addHandler(handler, filters);
    }

}
