package com.github.yoojia.events;

import com.github.yoojia.events.internal.EventFilter;

import java.util.BitSet;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.2
 */
class InternalEventFilter implements EventFilter {

    private final MethodDefine mDefine;

    public InternalEventFilter(MethodDefine define) {
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

    static boolean isTypesMatched(Class<?>[] define, Class<?>[] source) {
        // 在@Subscriber定义了参数的情况下：
        // 如果Method中，参数数量与发送的事件负载数量不一致，则不匹配。
        if (define.length != source.length) {
            return false;
        }else{
            // 判断方法参数类型与负载参数类型是否相同
            int hits = 0;
            final BitSet flags = new BitSet(source.length);
            // flags.default : false
            for (Class<?> def : define) {
                for (int i = 0; i < source.length; i++) {
                    if ( ! flags.get(i) && def.equals(source[i])) {
                        hits += 1;
                        flags.set(i, true);
                        break;
                    }
                }
            }
            return hits == define.length;
        }
    }

}
