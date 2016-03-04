package com.github.yoojia.events;

import com.github.yoojia.events.internal.EventFilter;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.2
 */
class DefaultEventFilter implements EventFilter {

    private final MethodArgs mArgs;

    public DefaultEventFilter(MethodArgs args) {
        mArgs = args;
    }

    @Override
    public boolean accept(Object internalEvent) {
        return accept(mArgs.defineTypes, mArgs.defineNames, internalEvent);
    }

    protected boolean accept(Class<?>[] defineTypes, String defineName, Object payload){
        final PayloadEvent payloadEvent = (PayloadEvent) payload;
        if (defineTypes.length == 0) {
            return defineName.equals(payloadEvent.name);
        }else{
            return defineName.equals(payloadEvent.name)
                    && defineTypes[0].equals(payloadEvent.payloadType);
        }
    }

}
