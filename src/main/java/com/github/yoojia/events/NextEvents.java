package com.github.yoojia.events;

import com.github.yoojia.events.emitter.*;
import com.github.yoojia.events.supports.Filter;

import java.lang.reflect.Method;
import java.util.List;

import static com.github.yoojia.events.supports.Preconditions.notNull;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.2
 */
public class NextEvents{

    private final EventEmitter mEmitter;
    private final Cached mObjectCached = new Cached();

    public NextEvents() {
        this(SharedScheduler.getDefault());
    }

    public NextEvents(Scheduler scheduler) {
        notNull(scheduler, "scheduler == null");
        mEmitter = new EventEmitter(scheduler);
        addEventInterceptor(new DeadEventInterceptor(this));
    }

    public void register(Object object) {
        register(object, null);
    }

    public void register(Object object, Filter<Method> methodFilter) {
        notNull(object, "object == null");
        final List<RealSubscriber> subscribers = mObjectCached.findTargets(object, methodFilter);
        for (RealSubscriber subscriber : subscribers) {
            mEmitter.addSubscriber(subscriber);
        }
    }

    public void unregister(Object object) {
        notNull(object, "object == null");
        final List<RealSubscriber> subscribers = mObjectCached.getPresent(object);
        if (!subscribers.isEmpty()) {
            for (RealSubscriber subscriber : subscribers) {
                mEmitter.removeSubscriber(subscriber);
            }
            mObjectCached.remove(object);
        }
    }

    public void just(String name) {
        mEmitter.emit(new EventPayload(name, null));
    }

    public void emit(String name, Object...payloads) {
        mEmitter.emit(new EventPayload(name, payloads));
    }

    public void emit(EventPayload event) {
        notNull(event, "event == null");
        mEmitter.emit(event);
    }

    public void addSubscriber(Subscriber subscriber, EventFilter filter) {
        notNull(subscriber, "subscriber == null");
        notNull(filter, "filter == null");
        mEmitter.addSubscriber(subscriber, filter);
    }

    public void addSubscriber(Subscriber subscriber, List<EventFilter> filters) {
        notNull(subscriber, "subscriber == null");
        notNull(filters, "filters == null");
        mEmitter.addSubscriber(subscriber, filters);
    }

    public void removeSubscriber(Subscriber subscriber) {
        mEmitter.removeSubscriber(subscriber);
    }

    public void addEventInterceptor(EventInterceptor handler){
        mEmitter.addEventInterceptor(handler);
    }

    public void removeEventInterceptor(EventInterceptor handler) {
        mEmitter.removeEventInterceptor(handler);
    }

    private static class DeadEventInterceptor implements EventInterceptor {

        private final NextEvents mEvents;

        private DeadEventInterceptor(NextEvents mEvents) {
            this.mEvents = mEvents;
        }

        @Override
        public boolean handle(Object event) {
            final boolean isDeadEvent = event instanceof DeadEvent;
            if (isDeadEvent) {
                final EventPayload payload = (EventPayload) ((DeadEvent) event).origin;
                if (EventPayload.DEAD_EVENT.equals(payload.name)) {
                    Logger.debug("NextEvents", "- Empty handlers for dead-event: " + payload);
                }else{
                    mEvents.emit(new EventPayload(EventPayload.DEAD_EVENT, payload));
                }
            }
            return isDeadEvent;
        }
    }

}
