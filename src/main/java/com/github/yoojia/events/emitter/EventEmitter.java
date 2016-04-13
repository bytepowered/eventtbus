package com.github.yoojia.events.emitter;

import com.github.yoojia.events.supports.Filter;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import static com.github.yoojia.events.supports.Functions.filter;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.3
 */
public class EventEmitter {

    private final Submit mSubmit;

    final Scheduler scheduler;
    final CopyOnWriteArrayList<Target> targets = new CopyOnWriteArrayList<>();
    final CopyOnWriteArrayList<OnEventHandler> handlers = new CopyOnWriteArrayList<>();

    public EventEmitter(){
        this(new CallerScheduler());
    }

    public EventEmitter(Scheduler schedule) {
        scheduler = schedule;
        mSubmit = new Submit(this);
    }

    public void emit(Object event) {
        mSubmit.submit(event);
    }

    public void addHandler(Handler handler, EventFilter filter) {
        this.addHandler(handler, Arrays.asList(new EventFilter[]{filter}));
    }

    public void addHandler(Handler handler, List<EventFilter> filters) {
        this.addTarget(new Target(handler, filters));
    }

    public void addTarget(Target target) {
        targets.add(target);
    }

    public void removeTarget(Target target) {
        this.removeHandler(target.handler);
    }

    public void removeHandler(final Handler handler) {
        targets.removeAll(filter(targets, new Filter<Target>(){
            @Override
            public boolean accept(Target item) {
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
