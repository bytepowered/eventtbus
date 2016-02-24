package com.github.yoojia.events;

import com.github.yoojia.events.core.Acceptor;
import com.github.yoojia.events.core.EventFilter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.3
 */
class Cached {

    private final Map<Object, Acceptors> mAcceptorHolder = new ConcurrentHashMap<>();
    private final Object mHolderLock = new Object();

    @SuppressWarnings("unchecked")
    public Acceptors find(Object object) {
        final List<Method> methods = Methods.getAnnotated(object.getClass());
        if (methods.isEmpty()) {
            return Acceptors.empty();
        }else{
            synchronized (mHolderLock) {
                final Acceptors cached = mAcceptorHolder.get(object);
                if (cached != null) {
                    return cached;
                }else{
                    final Acceptors acceptors = new Acceptors(methods.size());
                    for (Method method : methods) {
                        acceptors.add(create(object, method, Methods.parse(method)));
                    }
                    mAcceptorHolder.put(object, acceptors);
                    return acceptors;
                }
            }
        }
    }

    public Acceptors getPresent(Object object) {
        return mAcceptorHolder.getOrDefault(object, Acceptors.empty());
    }

    public void remove(Object object) {
        mAcceptorHolder.remove(object);
    }

    @SuppressWarnings("unchecked")
    private static Acceptor create(Object object, Method method, Object[] args) {
        final int scheduleType = (int) args[Methods.IDX_SCHEDULE_TYPE];
        final List<EventFilter> filters = (List<EventFilter>) args[Methods.IDX_FILTERS];
        final String defineName = (String) args[Methods.IDX_EVENT_NAME];
        final Class<?> defineType = (Class<?>) args[Methods.IDX_EVENT_TYPE];
        final MethodEventHandler handler = new MethodEventHandler(scheduleType, object, method);
        filters.add(new DefaultEventFilter(defineName, defineType));
        return new Acceptor(handler, filters);
    }

}
