package com.github.yoojia.events;

import com.github.yoojia.events.internal.EventHandler;
import com.github.yoojia.events.supports.ClassTypes;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.0
 */
class MethodHandler implements EventHandler {

    private final int mScheduleType;
    private final Method mMethod;
    private final WeakReference<Object> mObjectRef;
    private final MethodDefine mDefine;

    private MethodHandler(int scheduleType, Object object, Method method, MethodDefine args) {
        mScheduleType = scheduleType;
        mMethod = method;
        mObjectRef = new WeakReference<>(object);
        mDefine = args;
    }

    @Override
    public void onEvent(Object event) throws Exception {
        final Object host = mObjectRef.get();
        if (host == null) {
            throw new IllegalStateException("Host object is dead for method: " + mMethod);
        }
        final PayloadEvent payload = (PayloadEvent) event;
        mMethod.setAccessible(true);
        if (mDefine.types.length == 0) {
            mMethod.invoke(mObjectRef.get());
        }else{
            mMethod.invoke(mObjectRef.get(), reorder(mDefine.types, payload));
        }
    }

    @Override
    public void onErrors(Exception errors) {
        throw new RuntimeException(errors);
    }

    @Override
    public int scheduleType() {
        return mScheduleType;
    }

    /**
     * 将负载事件的数值，按回调Method的类型顺序，重新排序。
     * 相同的类型，按负载事件中的数值顺序填充
     */
    static Object[] reorder(Class<?>[] defineTypes, PayloadEvent payload) {
        final Object[] values = new Object[defineTypes.length];
        final boolean[] used = new boolean[values.length];
        for (int i = 0; i < defineTypes.length; i++) {
            for (int j = 0; j < values.length; j++) {
                if (!used[j] && ClassTypes.equalsIgnoreWrapType(defineTypes[i], payload.types[j])) {
                    values[i] = payload.values[j];
                    used[j] = true;
                    break;
                }
            }
        }
        return values;
    }

    public static MethodHandler create(int scheduleType, Object object, Method method, MethodDefine args) {
        return new MethodHandler(scheduleType, object, method, args);
    }

}
