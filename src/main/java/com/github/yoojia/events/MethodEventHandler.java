package com.github.yoojia.events;

import com.github.yoojia.events.internal.EventHandler;
import com.github.yoojia.events.internal.InternalEvent;

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
    public void onEvent(InternalEvent event) throws Exception {
        final Object host = mObjectRef.get();
        if (host == null) {
            throw new IllegalStateException("Host object is dead for method: " + mMethod);
        }
        mMethod.invoke(mObjectRef.get(), event.getValue());
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
