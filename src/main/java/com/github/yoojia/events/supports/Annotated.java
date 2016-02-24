package com.github.yoojia.events.supports;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;

import static com.github.yoojia.events.supports.Preconditions.notNull;

/**
 * @author 陈小锅 (yoojia.chen@gmail.com)
 * @since 1.0
 */
public abstract class Annotated<T extends AnnotatedElement> {

    protected List<Filter<T>> mResourceFilters = new ArrayList<>();
    protected Filter<Class<?>> mTypeFilter;

    public Annotated() {
        // Ignore java,javax,android objects
        setTypesFilter(new Filter<Class<?>>() {
            @Override
            public boolean accept(Class<?> type) {
                final String name = type.getName();
                if (name.startsWith("java.")
                        || name.startsWith("javax.")
                        || name.startsWith("android.")
                        || name.startsWith("com.android.")) {
                    return false;
                }else{
                    return true;
                }
            }
        });
    }

    /**
     * 添加注解目标对象的过滤处理接口
     * @param filter 过滤处理接口
     * @return Annotated
     */
    public Annotated<T> addResourceFilter(Filter<T> filter) {
        notNull(filter, "filter == null");
        mResourceFilters.add(filter);
        return this;
    }

    /**
     * 设置注解目标类型过滤的处理接口
     * @param filter 过滤处理接口
     * @return Annotated
     */
    public Annotated<T> setTypesFilter(Filter<Class<?>> filter) {
        notNull(filter, "filter == null");
        mTypeFilter = filter;
        return this;
    }

    /**
     * 从目标类型中查找注解内容
     * @param targetType 目标类型
     * @return 注解内容列表
     */
    public List<T> find(Class<?> targetType) {
        notNull(targetType, "Class target type == null");
        final List<T> output = new ArrayList<>();
        Class<?> type = targetType;
        while (! Object.class.equals(type) && mTypeFilter.accept(type)){
            final T[] resources = getResource(type);
            resources:
            for (T res : resources){
                for (Filter<T> filter : mResourceFilters) {
                    if (filter.accept(res)) {
                        output.add(res);
                    }else{
                        continue resources;
                    }
                }
            }
            type = type.getSuperclass();
        }
        return output;
    }

    protected abstract T[] getResource(Class<?> type);

}
