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
            final int size = mEmitter.eventInterceptors.size();
            for (int i = 0; i < size; i++) {
                final EventInterceptor interceptor = mEmitter.eventInterceptors.get(i);
                if (interceptor.handle(event)) {
                    return;
                }
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
        final int size = emitter.subscribers.size();
        for (int i = 0; i < size; i++) {
            final RealSubscriber subscriber = emitter.subscribers.get(i);
            if (subscriber.accept(event)) {
                out.add(subscriber.subscriber);
            }
        }
        return out;
    }
}
