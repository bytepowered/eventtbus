package com.github.yoojia.events;

import com.github.yoojia.events.emitter.EventFilter;
import com.github.yoojia.events.emitter.RealSubscriber;
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

    private final Map<Object, SubscriberArray> mCache = new ConcurrentHashMap<>();

    /**
     * 从指定对象中查找添加了 @Subscribe 注解的方法。
     * - 如果对象中没有@Subscriber注解的方法，返回一个空列表。
     * - 否则，将Target列表缓存以重用。
     *
     * @param object 指定对象
     * @param filter 自义方法过滤
     * @return 非null对象的Target列表
     */
    @SuppressWarnings("unchecked")
    public SubscriberArray findTargets(Object object, Filter<Method> filter) {
        final List<Method> methods = Methods.getAnnotated(object.getClass(), filter);
        if (methods.isEmpty()) {
            return SubscriberArray.empty();
        }else{
            synchronized (mCache) {
                final SubscriberArray present = mCache.get(object);
                if (present != null) {
                    return present;
                }else{
                    final ArrayList<RealSubscriber> array = new ArrayList<>(methods.size());
                    for (Method method: methods) {
                        array.add(create(object, method, Methods.parse(method)));
                    }
                    final SubscriberArray acceptors = new SubscriberArray(array);
                    mCache.put(object, acceptors);
                    return acceptors;
                }
            }
        }
    }

    public SubscriberArray getPresent(Object object) {
        final SubscriberArray present = mCache.get(object);
        if (present == null) {
            return SubscriberArray.empty();
        }else{
            return present;
        }
    }

    public void remove(Object object) {
        mCache.remove(object);
    }

    @SuppressWarnings("unchecked")
    private static RealSubscriber create(Object object, Method method, MethodArgs args) {
        final ArrayList<EventFilter> filters = new ArrayList<>(1);
        filters.add(new MethodFilter(args));
        return new RealSubscriber(MethodSubscriber.create(args.scheduleOn, object, method, args), filters);
    }

}
