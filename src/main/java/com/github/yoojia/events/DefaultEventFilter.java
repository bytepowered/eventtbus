package com.github.yoojia.events;

import com.github.yoojia.events.internal.EventFilter;

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
    public boolean accept(Object internalEvent) {
        return accept(mDefineType, mDefineName, internalEvent);
    }

    protected boolean accept(Class<?> defineType, String defineName, Object payload){
        final PayloadEvent payloadEvent = (PayloadEvent) payload;
        return defineType.equals(payloadEvent.payloadType) && defineName.equals(payloadEvent.name);
    }
}
