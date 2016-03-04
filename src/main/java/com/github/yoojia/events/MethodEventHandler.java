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

    public MethodEventHandler(int scheduleType, Object object, Method method) {
        mScheduleType = scheduleType;
        mObjectRef = new WeakReference<>(object);
        mMethod = method;
    }

    @Override
    public void onEvent(Object internalEvent) throws Exception {
        final Object host = mObjectRef.get();
        if (host == null) {
            throw new IllegalStateException("Host object is dead for method: " + mMethod);
        }
        final PayloadEvent payloadPayloadEvent = (PayloadEvent) internalEvent;
        mMethod.setAccessible(true);
        mMethod.invoke(mObjectRef.get(), payloadPayloadEvent.payloadValue);
    }

    @Override
    public void onErrors(Exception errors) {
        throw new RuntimeException(errors);
    }

    @Override
    public int scheduleType() {
        return mScheduleType;
    }
}
