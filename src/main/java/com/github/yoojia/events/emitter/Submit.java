package com.github.yoojia.events.emitter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yoojia Chen (yoojia.chen@gmail.com)
 * @since 2.3
 */
class Submit {

    private static final int GUESS = 4;

    private final EventEmitter mEmitter;

    public Submit(EventEmitter mEmitter) {
        this.mEmitter = mEmitter;
    }

    public void submit(Object event) {
        // 如果没有Handler处理事件，将原事件包装成DeadEvent后，再重新发射事件
        final List<Subscriber> subscribers = findSubscribers(event, mEmitter);
        if (subscribers.isEmpty() && !(event instanceof DeadEvent)) {
            mEmitter.emit(new DeadEvent(event));
        }else{
            // 1. 先处理 EventInterceptor 接口，这些接口可能会拦截事件的传递过程。
            for (EventInterceptor interceptor : mEmitter.interceptors) if (interceptor.handle(event)) {
                return;
            }
            // 2. 再调度事件处理
            mEmitter.scheduler.schedule(event, subscribers);
        }
    }

    /**
     * 查找能处理指定事件的Subscriber列表
     * @param event 指定事件
     * @param emitter EventEmitter
     * @return Handler列表
     */
    private List<Subscriber> findSubscribers(Object event, EventEmitter emitter) {
        final ArrayList<Subscriber> out = new ArrayList<>(GUESS);
        for (RealSubscriber item : emitter.subscribers) if (item.accept(event)) {
            out.add(item.subscriber);
        }
        return out;
    }
}
