package com.github.yoojia.events;

import java.lang.reflect.Method;

import static com.github.yoojia.events.supports.ClassTypes.lenientlyEquals;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.4
 */
class MethodSubscriber implements EventSubscriber {

    private final On mScheduleOn;
    private final Method mMethod;
    private final Object mObjectRef;
    private final MethodArgs mDefine;

    private MethodSubscriber(On scheduleOn, Object object, Method method, MethodArgs define) {
        mScheduleOn = scheduleOn;
        mMethod = method;
        mObjectRef = object;
        mDefine = define;
    }

    @Override
    public void onEvent(Object event) throws Exception {
        final EventPayload payload = (EventPayload) event;
        mMethod.setAccessible(true);
        if (mDefine.isEmptyArgs) {
            mMethod.invoke(mObjectRef);
        }else if (mDefine.isAnyType) {
            mMethod.invoke(mObjectRef, new Any(payload.values, payload.types));
        }else{
            mMethod.invoke(mObjectRef, reorderArgs(mDefine.types, payload));
        }
    }

    @Override
    public void onError(Exception errors) {
        throw new RuntimeException(errors);
    }

    @Override
    public On scheduleOn() {
        return mScheduleOn;
    }

    /**
     * 将负载事件的数值，按回调Method的类型顺序，重新排序。
     * 相同的类型，按负载事件中的数值顺序填充
     */
    static Object[] reorderArgs(Class<?>[] defineTypes, EventPayload payload) {
        final Object[] values = new Object[defineTypes.length];
        final boolean[] used = new boolean[values.length];
        for (int i = 0; i < defineTypes.length; i++) {
            for (int j = 0; j < values.length; j++) {
                final Class<?> defType = defineTypes[i];
                if ( !used[j] && lenientlyEquals(defType, payload.types[j])) {
                    values[i] = payload.values[j];
                    used[j] = true;
                    break;
                }
            }
        }
        return values;
    }

    public static MethodSubscriber create(On scheduleOn, Object object, Method method, MethodArgs args) {
        return new MethodSubscriber(scheduleOn, object, method, args);
    }

}
