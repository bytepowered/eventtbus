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
        addOnEventHandler(new OnDeadEventHandler(this));
    }

    public void register(Object object) {
        register(object, null);
    }

    public void register(Object object, Filter<Method> methodFilter) {
        notNull(object, "object == null");
        final List<Target> targets = mObjectCached.findTargets(object, methodFilter);
        for (Target target : targets) {
            mEmitter.addTarget(target);
        }
    }

    public void unregister(Object object) {
        notNull(object, "object == null");
        final List<Target> targets = mObjectCached.getPresent(object);
        if (!targets.isEmpty()) {
            for (Target target : targets) {
                mEmitter.removeTarget(target);
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

    public void addHandler(Handler handler, EventFilter filter) {
        notNull(handler, "handler == null");
        notNull(filter, "filter == null");
        mEmitter.addHandler(handler, filter);
    }

    public void addHandler(Handler handler, List<EventFilter> filters) {
        notNull(handler, "handler == null");
        notNull(filters, "filters == null");
        mEmitter.addHandler(handler, filters);
    }

    public void removeHandler(Handler handler) {
        mEmitter.removeHandler(handler);
    }

    public void addOnEventHandler(OnEventHandler handler){
        mEmitter.addOnEventHandler(handler);
    }

    public void removeOnEventHandler(OnEventHandler handler) {
        mEmitter.removeOnEventHandler(handler);
    }

    private static class OnDeadEventHandler implements OnEventHandler {

        private final NextEvents mEvents;

        private OnDeadEventHandler(NextEvents mEvents) {
            this.mEvents = mEvents;
        }

        @Override
        public boolean handleEvent(Object event) {
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
