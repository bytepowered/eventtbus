package com.github.yoojia.events;

import com.github.yoojia.events.internal.Acceptor;
import com.github.yoojia.events.internal.EventFilter;
import com.github.yoojia.events.supports.Filter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.2
 */
class Cached {

    private final Map<Object, Acceptors> mAcceptorCache = new ConcurrentHashMap<>();
    private final Object mLock = new Object();

    @SuppressWarnings("unchecked")
    public Acceptors find(Object object, Filter<Method> filter) {
        final List<Method> methods = Methods.getAnnotated(object.getClass(), filter);
        if (methods.isEmpty()) {
            return Acceptors.empty();
        }else{
            synchronized (mLock) {
                final Acceptors present = mAcceptorCache.get(object);
                if (present != null) {
                    return present;
                }else{
                    final int size = methods.size();
                    final Acceptors acceptors = new Acceptors(size);
                    for (int i = 0; i < size; i++) {
                        final Method method = methods.get(i);
                        acceptors.add(create(object, method, Methods.parse(method)));
                    }
                    mAcceptorCache.put(object, acceptors);
                    return acceptors;
                }
            }
        }
    }

    public Acceptors getSafety(Object object) {
        final Acceptors present = mAcceptorCache.get(object);
        if (present == null) {
            return Acceptors.empty();
        }else{
            return present;
        }
    }

    public void remove(Object object) {
        mAcceptorCache.remove(object);
    }

    @SuppressWarnings("unchecked")
    private static Acceptor create(Object object, Method method, Object[] args) {
        final int scheduleType = (int) args[Methods.IDX_SCHEDULE_TYPE];
        final ArrayList<EventFilter> filters = (ArrayList<EventFilter>) args[Methods.IDX_FILTERS];
        final String defineName = (String) args[Methods.IDX_EVENT_NAME];
        final Class<?> defineType = (Class<?>) args[Methods.IDX_EVENT_TYPE];
        final MethodEventHandler handler = new MethodEventHandler(scheduleType, object, method);
        // 将默认的EventFilter设置在Filters列表的首位
        filters.add(0, new DefaultEventFilter(defineName, defineType));
        return new Acceptor(handler, filters);
    }

}
