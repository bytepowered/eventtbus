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
    final CopyOnWriteArrayList<RealSubscriber> subscribers = new CopyOnWriteArrayList<>();
    final CopyOnWriteArrayList<EventInterceptor> eventInterceptors = new CopyOnWriteArrayList<>();

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

    public void addSubscriber(Subscriber subscriber, EventFilter filter) {
        addSubscriber(subscriber, Arrays.asList(new EventFilter[]{filter}));
    }

    public void addSubscriber(Subscriber subscriber, List<EventFilter> filters) {
        addSubscriber(new RealSubscriber(subscriber, filters));
    }

    public void addSubscriber(RealSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void removeSubscriber(RealSubscriber subscriber) {
        this.removeSubscriber(subscriber.subscriber);
    }

    public void removeSubscriber(final Subscriber subscriber) {
        subscribers.removeAll(filter(subscribers, new Filter<RealSubscriber>(){
            @Override public boolean accept(RealSubscriber item) {
                return item.subscriber == (subscriber);
            }
        }));
    }

    public void addEventInterceptor(EventInterceptor interceptor) {
        eventInterceptors.add(interceptor);
    }

    public void removeEventInterceptor(EventInterceptor interceptor) {
        eventInterceptors.remove(interceptor);
    }

}
