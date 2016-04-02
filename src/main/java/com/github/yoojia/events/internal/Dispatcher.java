package com.github.yoojia.events.internal;

import com.github.yoojia.events.supports.Filter;

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
    private final CopyOnWriteArrayList<OnEventHandler> mHandlers = new CopyOnWriteArrayList<>();

    public Dispatcher(){
        this(new SchedulerImpl());
    }

    public Dispatcher(Scheduler schedule) {
        mSchedule = schedule;
    }

    public void addHandler(Handler handler, EventFilter filter) {
        addHandler(handler, Arrays.asList(new EventFilter[]{filter}));
    }

    public void addHandler(Handler handler, List<EventFilter> filters) {
        mAcceptors.add(new Acceptor(handler, filters));
    }

    public void removeHandler(final Handler handler) {
        mAcceptors.removeAll(filter(mAcceptors, new Filter<Acceptor>(){
            @Override
            public boolean accept(Acceptor item) {
                return item.handler.equals(handler);
            }
        }));
    }

    public void emit(Object event) {
        // 快速匹配触发事件的EventHandler, 然后由调度器来处理
        final List<Handler> handlers = findMatchedHandlers(event);
        if (handlers.isEmpty() && !(event instanceof DeadEvent)) {
            emit(new DeadEvent(event));
        }else{
            final int size = mHandlers.size();
            for (int i = 0; i < size; i++) {
                final OnEventHandler handler = mHandlers.get(i);
                if (handler.handleEvent(event)) {
                    return;
                }
            }
            mSchedule.submit(event, handlers);
        }
    }

    public void addOnEventHandler(OnEventHandler handler) {
        mHandlers.add(handler);
    }

    public void removeOnEventHandler(OnEventHandler handler) {
        mHandlers.remove(handler);
    }

    private List<Handler> findMatchedHandlers(Object event) {
        final ArrayList<Handler> matched = new ArrayList<>(GUESS);
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
