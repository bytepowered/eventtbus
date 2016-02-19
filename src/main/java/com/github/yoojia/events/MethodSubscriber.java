package com.github.yoojia.events;

import com.github.yoojia.events.core.Subscriber;

import java.lang.reflect.Method;

/**
 * @author YOOJIA.CHEN (yoojia.chen@gmail.com)
 * @version 2015-11-06
 */
public class MethodSubscriber implements Subscriber<Event> {

    private final Object mObject;
    private final Method mMethod;

    public MethodSubscriber(Object object, Method method) {
        mObject = object;
        mMethod = method;
    }

    @Override
    public void onCall(Event input) throws Exception {
        mMethod.setAccessible(true);
        mMethod.invoke(mObject, input.value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onErrors(Event input, Exception errors) {
        throw new RuntimeException(input.toString(), errors);
    }

    boolean isSameMethod(Method method) {
        return mMethod.equals(method);
    }
}
