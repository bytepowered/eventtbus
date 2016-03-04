package com.github.yoojia.events;

import com.github.yoojia.events.internal.EventHandler;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.2
 */
class MethodEventHandler implements EventHandler {

    private final int mScheduleType;
    private final Method mMethod;
    private final WeakReference<Object> mObjectRef;
    private final MethodArgs mArgs;

    private MethodEventHandler(int scheduleType, Object object, Method method, MethodArgs args) {
        mScheduleType = scheduleType;
        mMethod = method;
        mObjectRef = new WeakReference<>(object);
        mArgs = args;
    }

    @Override
    public void onEvent(Object event) throws Exception {
        final Object host = mObjectRef.get();
        if (host == null) {
            throw new IllegalStateException("Host object is dead for method: " + mMethod);
        }
        final PayloadEvent payload = (PayloadEvent) event;
        mMethod.setAccessible(true);
        if (mArgs.defineTypes.length == 0) {
            mMethod.invoke(mObjectRef.get());
        }else{
            mMethod.invoke(mObjectRef.get(), reorder(mArgs.defineTypes, payload));
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

    private static Object[] reorder(Class<?>[] defineTypes, PayloadEvent payload) {
        final Object[] output = new Object[defineTypes.length];
        for (int i = 0; i < defineTypes.length; i++) {
            for (int j = 0; j < payload.eventTypes.length; j++) {
                if (defineTypes[i].equals(payload.eventTypes[j])) {
                    output[i] = payload.eventValues[j];
                }
            }
        }
        return output;
    }

    public static MethodEventHandler create(int scheduleType, Object object, Method method, MethodArgs args) {
        return new MethodEventHandler(scheduleType, object, method, args);
    }

}
