package com.github.yoojia.events.internal;

import com.github.yoojia.events.supports.Filter;
import com.github.yoojia.events.supports.ObjectReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import static com.github.yoojia.events.supports.Filters.filter;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.2
 */
public class Dispatcher {

    private static final int GUESS = 2;

    private final Scheduler mSchedule;
    private final CopyOnWriteArrayList<Acceptor> mAcceptors = new CopyOnWriteArrayList<>();
    private final ObjectReference<OnEventMissedListener> mEventMissedListener = new ObjectReference<OnEventMissedListener>(new OnEventMissedListener() {
        @Override
        public void onEvent(Object event) {
            Logger.debug("Dispatcher", "- Dead event: " + event);
        }
    });

    public Dispatcher(){
        this(new Scheduler0());
    }

    public Dispatcher(Scheduler schedule) {
        mSchedule = schedule;
    }

    public void addHandler(EventHandler handler, EventFilter filter) {
        addHandler(handler, Arrays.asList(new EventFilter[]{filter}));
    }

    public void addHandler(EventHandler handler, List<EventFilter> filters) {
        mAcceptors.add(new Acceptor(handler, filters));
    }

    public void removeHandler(final EventHandler handler) {
        mAcceptors.removeAll(filter(mAcceptors, new Filter<Acceptor>(){
            @Override
            public boolean accept(Acceptor item) {
                return item.handler.equals(handler);
            }
        }));
    }

    public void emit(Object event) {
        // 快速匹配触发事件的EventHandler, 然后由调度器来处理
        final List<EventHandler> handlers = findMatchedHandlers(event);
        if (handlers.isEmpty()) {
            final OnEventMissedListener missed = mEventMissedListener.get();
            missed.onEvent(event);
        }else{
            mSchedule.submit(event, handlers);
        }
    }

    public void setOnEventMissedListener(OnEventMissedListener listener) {
        mEventMissedListener.set(listener);
    }

    private List<EventHandler> findMatchedHandlers(Object event) {
        final ArrayList<EventHandler> matched = new ArrayList<>(GUESS);
        final int size = mAcceptors.size();
        for (int i = 0; i < size; i++) {
            final Acceptor acceptor = mAcceptors.get(i);
            if (acceptor.accept(event)) {
                matched.add(acceptor.handler);
            }
        }
        return matched;
    }

}
