package com.github.yoojia.events;

import java.lang.reflect.Method;

import static com.github.yoojia.events.supports.ClassTypes.lenientlyEquals;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.0
 */
class MethodHandler implements EventHandler {

    private final On mScheduleOn;
    private final Method mMethod;
    private final Object mObjectRef;
    private final MethodDefine mDefine;

    private MethodHandler(On scheduleOn, Object object, Method method, MethodDefine define) {
        mScheduleOn = scheduleOn;
        mMethod = method;
        mObjectRef = object;
        mDefine = define;
    }

    @Override
    public void onEvent(Object event) throws Exception {
        final EventPayload payload = (EventPayload) event;
        mMethod.setAccessible(true);
        if (mDefine.isNoArgs) {
            mMethod.invoke(mObjectRef);
        }else if (mDefine.isAny) {
            mMethod.invoke(mObjectRef, new Any(payload.values, payload.types));
        }else{
            mMethod.invoke(mObjectRef, reorderArgs(mDefine.types, payload));
        }
    }

    @Override
    public void onErrors(Exception errors) {
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

    public static MethodHandler create(On scheduleOn, Object object, Method method, MethodDefine args) {
        return new MethodHandler(scheduleOn, object, method, args);
    }

}
