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
class ObjectCached {

    private final Map<Object, Acceptors> mAcceptorCache = new ConcurrentHashMap<>();
    private final Object mLock = new Object();

    @SuppressWarnings("unchecked")
    public Acceptors find(Object object, Filter<Method> methodFilter) {
        final List<Method> methods = Methods.getAnnotated(object.getClass(), methodFilter);
        if (methods.isEmpty()) {
            return Acceptors.empty();
        }else{
            synchronized (mLock) {
                final Acceptors present = mAcceptorCache.get(object);
                if (present != null) {
                    return present;
                }else{
                    final int size = methods.size();
                    final ArrayList<Acceptor> array = new ArrayList<>(size);
                    for (int i = 0; i < size; i++) {
                        final Method method = methods.get(i);
                        array.add(create(object, method, Methods.parse(method)));
                    }
                    final Acceptors acceptors = new Acceptors(array);
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
    private static Acceptor create(Object object, Method method, MethodDefine args) {
        final ArrayList<EventFilter> filters = new ArrayList<>(1);
        filters.add(new InternalFilter(args));
        return new Acceptor(MethodHandler.create(args.schedule, object, method, args), filters);
    }

}
