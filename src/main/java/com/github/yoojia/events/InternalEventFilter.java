package com.github.yoojia.events;

import com.github.yoojia.events.internal.EventFilter;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.2
 */
class InternalEventFilter implements EventFilter {

    private final MethodArgs mArgs;

    public InternalEventFilter(MethodArgs args) {
        mArgs = args;
    }

    @Override
    public boolean accept(Object event) {
        final PayloadEvent payload = (PayloadEvent) event;
        if (mArgs.defineTypes.length == 0) {
            return isNamesMatched(payload.name);
        }else{
            return isNamesMatched(payload.name)
                    && isTypesMatched(payload.eventTypes);
        }
    }

    private boolean isNamesMatched(String eventName) {
        return mArgs.defineName.equals(eventName);
    }

    private boolean isTypesMatched(Class<?>[] eventTypes) {
        // 在@Subscriber定义了参数的情况下：
        // 如果Method中，参数数量与发送的事件负载数量不一致，则不匹配。
        if (mArgs.defineTypes.length != eventTypes.length) {
            return false;
        }else{
            // 判断方法参数类型与负载参数类型是否相同
            // FIXME 算法错误
            int flags = 0;
            for (Class<?> define : mArgs.defineTypes) {
                for (Class<?> event : eventTypes) {
                    if (define.equals(event)) {
                        flags += 1;
                        break;
                    }
                }
            }
            return flags == mArgs.defineTypes.length;
        }
    }

}
