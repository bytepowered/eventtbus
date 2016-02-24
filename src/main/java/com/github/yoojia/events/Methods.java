package com.github.yoojia.events;

import com.github.yoojia.events.core.EventFilter;
import com.github.yoojia.events.supports.AnnotatedMethod;
import com.github.yoojia.events.supports.Filter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.2
 */
class Methods {

    public static final int IDX_SCHEDULE_TYPE = 0;
    public static final int IDX_FILTERS =       1;
    public static final int IDX_EVENT_NAME =    2;
    public static final int IDX_EVENT_TYPE =    3;

    private static final Filter<Method> SIGNATURE_FILTER = new SignatureFilter();

    public static List<Method> getAnnotated(Class<?> clazz, Filter<Method> filter) {
        final AnnotatedMethod methods = new AnnotatedMethod(Subscribe.class);
        methods.addResourceFilter(Methods.SIGNATURE_FILTER);
        if (filter != null) {
            methods.addResourceFilter(filter);
        }
        return methods.find(clazz);
    }

    public static Object[] parse(Method method) {
        final Subscribe annotation = method.getDeclaredAnnotation(Subscribe.class);
        final int scheduleType = annotation.run().scheduleFlag;
        final Class<? extends EventFilter>[] types = annotation.filters();
        final List<EventFilter> filters = new ArrayList<>(types.length);
        try{
            for (Class<? extends EventFilter> type: types) {
                filters.add(type.newInstance());
            }
        }catch (Exception err) {
            throw new IllegalAccessError("Filters Type in @EventHandler must has a non-params constructor method!");
        }
        final Object[] args = new Object[4];
        args[IDX_SCHEDULE_TYPE] = scheduleType;
        args[IDX_FILTERS] = filters;
        args[IDX_EVENT_NAME] = annotation.on();
        args[IDX_EVENT_TYPE] = method.getParameterTypes()[0];
        return args;
    }

    /**
     * 方法签名过滤器
     */
    private static class SignatureFilter implements Filter<Method> {

        @Override
        public boolean accept(Method method) {
            if (! Void.TYPE.equals(method.getReturnType())) {
                throw new IllegalArgumentException("Return type of @Subscribe annotated methods must be <VOID> , method: " + method);
            }
            final Class<?>[] params = method.getParameterTypes();
            if (params.length != 1) {
                throw new IllegalArgumentException("@Subscribe annotated methods must has a single param , method: " + method);
            }
            return false;
        }

    }

}
