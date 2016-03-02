package com.github.yoojia.events;

import com.github.yoojia.events.internal.EventFilter;
import com.github.yoojia.events.internal.EventMessage;

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
    public boolean accept(EventMessage event) {
        return accept(mDefineType, mDefineName, event);
    }

    protected boolean accept(Class<?> defineType, String defineName, EventMessage input){
        final Event event = (Event) input.getEvent();
        return ! defineType.equals(event.payloadType)
                && defineName.equals(event.name);
    }
}
