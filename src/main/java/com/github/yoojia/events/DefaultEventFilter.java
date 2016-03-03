package com.github.yoojia.events;

import com.github.yoojia.events.internal.EventFilter;
import com.github.yoojia.events.internal.InternalEvent;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.2
 */
class DefaultEventFilter implements EventFilter {

    private final String mDefineName;
    private final Class<?> mDefineType;

    public DefaultEventFilter(String defineName, Class<?> defineType) {
        mDefineName = defineName;
        mDefineType = defineType;
    }

    @Override
    public boolean accept(InternalEvent internalEvent) {
        return accept(mDefineType, mDefineName, internalEvent);
    }

    protected boolean accept(Class<?> defineType, String defineName, InternalEvent payload){
        final PayloadEvent payloadEvent = (PayloadEvent) payload.getValue();
        return defineType.equals(payloadEvent.payloadType) && defineName.equals(payloadEvent.name);
    }
}
