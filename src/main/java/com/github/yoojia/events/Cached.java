package com.github.yoojia.events;

import com.github.yoojia.events.emitter.Target;
import com.github.yoojia.events.emitter.EventFilter;
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

    private final Map<Object, TargetArray> mCache = new ConcurrentHashMap<>();

    /**
     * 从指定对象中查找添加了 @Subscribe 注解的方法。
     * - 如果对象中没有@Subscriber注解的方法，返回一个空列表。
     * - 否则，将Target列表缓存以重用。
     *
     * @param object 指定对象
     * @param methodFilter 处自义方法过滤
     * @return 非null对象的Target列表
     */
    @SuppressWarnings("unchecked")
    public TargetArray findTargets(Object object, Filter<Method> methodFilter) {
        final List<Method> methods = Methods.getAnnotated(object.getClass(), methodFilter);
        if (methods.isEmpty()) {
            return TargetArray.empty();
        }else{
            synchronized (mCache) {
                final TargetArray present = mCache.get(object);
                if (present != null) {
                    return present;
                }else{
                    final int size = methods.size();
                    final ArrayList<Target> array = new ArrayList<>(size);
                    for (int i = 0; i < size; i++) {
                        final Method method = methods.get(i);
                        array.add(create(object, method, Methods.parse(method)));
                    }
                    final TargetArray acceptors = new TargetArray(array);
                    mCache.put(object, acceptors);
                    return acceptors;
                }
            }
        }
    }

    public TargetArray getPresent(Object object) {
        final TargetArray present = mCache.get(object);
        if (present == null) {
            return TargetArray.empty();
        }else{
            return present;
        }
    }

    public void remove(Object object) {
        mCache.remove(object);
    }

    @SuppressWarnings("unchecked")
    private static Target create(Object object, Method method, MethodArgs args) {
        final ArrayList<EventFilter> filters = new ArrayList<>(1);
        filters.add(new MethodFilter(args));
        return new Target(MethodHandler.create(args.scheduleOn, object, method, args), filters);
    }

}
