package com.github.yoojia.events;

import com.github.yoojia.events.core.EventHandler;
import com.github.yoojia.events.core.EventMessage;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

/**
 * @author YOOJIA.CHEN (yoojia.chen@gmail.com)
 * @version 0.1
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
    public void onEvent(EventMessage event) throws Exception {
        final Object host = mObjectRef.get();
        if (host == null) {
            throw new IllegalStateException("Host object is dead for method: " + mMethod);
        }
        mMethod.setAccessible(true);
        mMethod.invoke(mObjectRef.get(), event.getEvent());
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
