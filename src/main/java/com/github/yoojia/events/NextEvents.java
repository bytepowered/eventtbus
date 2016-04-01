package com.github.yoojia.events;

import com.github.yoojia.events.internal.*;
import com.github.yoojia.events.supports.Filter;
import com.github.yoojia.events.supports.ObjectReference;

import java.lang.reflect.Method;
import java.util.List;

import static com.github.yoojia.events.supports.Preconditions.notNull;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.2
 */
public class NextEvents{

    private final Dispatcher mDispatcher;
    private final ObjectCached mObjectCached = new ObjectCached();
    private final ObjectReference<OnEventMissedListener> mThisEventMissedListener = new ObjectReference<>();

    public NextEvents() {
        this(SharedSchedule.getDefault());
    }

    public NextEvents(Scheduler schedule) {
        notNull(schedule, "schedule == null");
        mDispatcher = new Dispatcher(schedule);
        mDispatcher.setOnEventMissedListener(new OnEventMissedListener() {
            @Override
            public void onEvent(Object missedEvent) {
                final PayloadEvent payload = (PayloadEvent) missedEvent;
                // 非DeadEvent事件，包装为DeadEvent继续处理
                if (PayloadEvent.DEAD_EVENT.equals(payload.name)) {
                    final OnEventMissedListener listener = mThisEventMissedListener.get();
                    if (listener != null) {
                        listener.onEvent(missedEvent);
                    }else{
                        Logger.debug("NextEvents", "- Dead event missed: " + missedEvent);
                    }
                }else{
                    emit(new PayloadEvent(PayloadEvent.DEAD_EVENT, payload.values));
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
            mDispatcher.addHandler(acceptor.handler, acceptor.filters);
        }
    }

    public void unregister(Object object) {
        notNull(object, "object == null");
        final List<Acceptor> acceptors = mObjectCached.getSafety(object);
        for (Acceptor acceptor : acceptors) {
            mDispatcher.removeHandler(acceptor.handler);
        }
        mObjectCached.remove(object);
    }

    public void emit(String name, Object...payloads) {
        if (payloads == null || payloads.length == 0){
            throw new IllegalArgumentException("payloads is empty");
        }
        mDispatcher.emit(new PayloadEvent(name, payloads));
    }

    public void emit(PayloadEvent event) {
        notNull(event, "event == null");
        mDispatcher.emit(event);
    }

    public void emit(Object event) {
        if (event instanceof PayloadEvent) {
            mDispatcher.emit(event);
        }else {
            throw new IllegalArgumentException("Call emit(PayloadEvent) instead");
        }
    }

    public void setOnEventMissedListener(OnEventMissedListener listener) {
        notNull(listener, "listener == null");
        mThisEventMissedListener.set(listener);
    }

    public void addHandler(Handler handler, EventFilter filter) {
        notNull(handler, "handler == null");
        notNull(filter, "filter == null");
        mDispatcher.addHandler(handler, filter);
    }

    public void addHandler(Handler handler, List<EventFilter> filters) {
        notNull(handler, "handler == null");
        notNull(filters, "filters == null");
        mDispatcher.addHandler(handler, filters);
    }

    public void removeHandler(Handler handler) {
        mDispatcher.removeHandler(handler);
    }

}
