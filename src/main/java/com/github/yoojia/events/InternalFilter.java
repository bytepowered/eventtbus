package com.github.yoojia.events;

import com.github.yoojia.events.internal.EventFilter;

import static com.github.yoojia.events.supports.ClassTypes.lenientlyEquals;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.0
 */
class InternalFilter implements EventFilter {

    private final MethodDefine mDefine;

    public InternalFilter(MethodDefine define) {
        mDefine = define;
    }

    @Override
    public boolean accept(Object event) {
        final PayloadEvent payload = (PayloadEvent) event;
        if (mDefine.types.length == 0) {
            return isNamesMatched(payload.name);
        }else{
            return isNamesMatched(payload.name)
                    && isTypesMatched(mDefine.types, payload.types);
        }
    }

    private boolean isNamesMatched(String eventName) {
        return mDefine.name.equals(eventName);
    }

    static boolean isTypesMatched(Class<?>[] defines, Class<?>[] sources) {
        // 在@Subscriber的方法定义了一个以上参数的情况下：
        // 如果Method中，参数数量与发送的事件负载数量不一致，则不匹配。
        // 判断方法参数类型数组与负载参数类型数组是否相同，顺序可以不一致。
        if (defines.length == sources.length) {
            int hits = 0;
            final boolean[] used = new boolean[sources.length];
            for (Class<?> defType : defines) {
                for (int i = 0; i < sources.length; i++) {
                    final Class<?> srcType = sources[i];
                    if ( !used[i] &&
                            (Object.class.equals(defType) || lenientlyEquals(defType, srcType))) {
                        hits += 1;
                        used[i] = true;
                        break;
                    }
                }
            }
            return hits == defines.length;
        }else{
            return false;
        }
    }

}
