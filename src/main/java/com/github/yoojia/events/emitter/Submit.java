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
        final List<Handler> handlers = findHandlers(event, mEmitter);
        if (handlers.isEmpty() && !(event instanceof DeadEvent)) {
            mEmitter.emit(new DeadEvent(event));
        }else{
            // 1. 先处理OnEventHandler接口，这些接口可能会拦截事件的传递过程。
            final int size = mEmitter.handlers.size();
            for (int i = 0; i < size; i++) {
                final OnEventHandler handler = mEmitter.handlers.get(i);
                if (handler.handleEvent(event)) {
                    return;
                }
            }
            // 2. 再调度事件处理
            mEmitter.scheduler.schedule(event, handlers);
        }
    }

    /**
     * 查找能处理指定事件的Handler列表
     * @param event 指定事件
     * @param emitter EventEmitter
     * @return Handler列表
     */
    private List<Handler> findHandlers(Object event, EventEmitter emitter) {
        final ArrayList<Handler> matched = new ArrayList<>(GUESS);
        final int size = emitter.targets.size();
        for (int i = 0; i < size; i++) {
            final Target target = emitter.targets.get(i);
            if (target.accept(event)) {
                matched.add(target.handler);
            }
        }
        return matched;
    }
}
