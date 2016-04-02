package com.github.yoojia.events.internal;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yoojia Chen (yoojia.chen@gmail.com)
 * @since 2.0
 */
class Emitter implements Schedule0<Object, Dispatcher> {

    private static final int GUESS = 2;

    @Override
    public void submit(Object event, Dispatcher dispatcher) {
        // 快速匹配触发事件的EventHandler, 然后由调度器来处理
        final List<Handler> handlers = findMatchedHandlers(event, dispatcher);
        if (handlers.isEmpty() && !(event instanceof DeadEvent)) {
            dispatcher.emit(new DeadEvent(event));
        }else{
            final int size = dispatcher.handlers.size();
            for (int i = 0; i < size; i++) {
                final OnEventHandler handler = dispatcher.handlers.get(i);
                if (handler.handleEvent(event)) {
                    return;
                }
            }
            dispatcher.handlerScheduler.submit(event, handlers);
        }
    }

    private List<Handler> findMatchedHandlers(Object event, Dispatcher dispatcher) {
        final ArrayList<Handler> matched = new ArrayList<>(GUESS);
        final int size = dispatcher.acceptors.size();
        for (int i = 0; i < size; i++) {
            final Acceptor acceptor = dispatcher.acceptors.get(i);
            if (acceptor.accept(event)) {
                matched.add(acceptor.handler);
            }
        }
        return matched;
    }
}
