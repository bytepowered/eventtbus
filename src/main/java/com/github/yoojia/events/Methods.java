package com.github.yoojia.events;

import com.github.yoojia.events.supports.AnnotatedMethod;
import com.github.yoojia.events.supports.ClassTypes;
import com.github.yoojia.events.supports.Filter;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.2
 */
class Methods {

    private static final Filter<Method> SIGNATURE_FILTER = new MethodSignFilter();

    public static List<Method> getAnnotated(Class<?> clazz, Filter<Method> customMethodFilter) {
        final AnnotatedMethod methods = new AnnotatedMethod(Subscribe.class);
        methods.addResourceFilter(Methods.SIGNATURE_FILTER);
        if (customMethodFilter != null) {
            methods.addResourceFilter(customMethodFilter);
        }
        return methods.find(clazz);
    }

    public static MethodDefine parse(Method method) {
        final Subscribe annotation = method.getAnnotation(Subscribe.class);
        final int schedule = annotation.run().scheduleFlag;
        final String names = annotation.on();
        final Class<?>[] types = method.getParameterTypes();
        // 参数列表中，如果参数数量大于1, 则列表中不允许存在Object类型
        if (types.length > 1) {
            for (Class<?> type : types) {
                if (Object.class.equals(type)) {
                    throw new IllegalArgumentException("Args array contains <Object> type");
                }
            }
        }
        return new MethodDefine(schedule, types, names);
    }

    private static class MethodSignFilter implements Filter<Method> {

        @Override
        public boolean accept(Method method) {
            if (! Void.TYPE.equals(method.getReturnType())) {
                throw new IllegalArgumentException("Return type of @Subscribe annotated methods must be <VOID>, method: " + method);
            }
            return false;
        }

    }

}
