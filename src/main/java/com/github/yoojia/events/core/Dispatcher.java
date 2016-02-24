package com.github.yoojia.events.core;

import com.github.yoojia.events.supports.Filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.github.yoojia.events.supports.Filters.filter;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.3
 */
public class Dispatcher {

    private static final int GUESS = 4;

    private final Schedule mSchedule;
    private final CopyOnWriteArrayList<Acceptor> mAcceptors = new CopyOnWriteArrayList<>();

    public Dispatcher(Schedule schedule) {
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

    public void emit(EventMessage event) {
        // 快速匹配触发事件的EventHandler, 然后由调度器来处理
        mSchedule.submit(event, findHandlers(event));
    }

    private List<EventHandler> findHandlers(EventMessage event) {
        final ArrayList<EventHandler> handlers = new ArrayList<>(GUESS);
        final int size = mAcceptors.size();
        for (int i = 0; i < size; i++) {
            final Acceptor acceptor = mAcceptors.get(i);
            if (acceptor.accept(event)) {
                handlers.add(acceptor.handler);
            }
        }
        return handlers;
    }

}
