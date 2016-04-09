package com.github.yoojia.events.internal;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yoojia Chen (yoojia.chen@gmail.com)
 * @since 2.0
 */
class Emitter implements Submit<Object, Void> {

    private static final int GUESS = 2;

    private final Dispatcher mDispatcher;

    public Emitter(Dispatcher mDispatcher) {
        this.mDispatcher = mDispatcher;
    }

    @Override
    public void submit(Object event, Void _null) {
        final List<Handler> handlers = findMatched(event, mDispatcher);
        if (handlers.isEmpty() && !(event instanceof DeadEvent)) {
            mDispatcher.emit(new DeadEvent(event));
        }else{
            final int size = mDispatcher.handlers.size();
            for (int i = 0; i < size; i++) {
                final OnEventHandler handler = mDispatcher.handlers.get(i);
                if (handler.handleEvent(event)) {
                    return;
                }
            }
            mDispatcher.handlerScheduler.submit(event, handlers);
        }
    }

    private List<Handler> findMatched(Object event, Dispatcher dispatcher) {
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
