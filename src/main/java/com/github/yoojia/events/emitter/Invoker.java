package com.github.yoojia.events.emitter;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.2
 */
public final class Invoker implements Runnable{

    private final Object mEvent;
    private final Subscriber mSubscriber;

    public Invoker(Object event, Subscriber subscriber) {
        mEvent = event;
        mSubscriber = subscriber;
    }

    @Override
    public void run() {
        try{
            mSubscriber.onEvent(mEvent);
        }catch (Exception errors) {
            try{
                mSubscriber.onError(errors);
            }catch (Throwable throwable) {
                throw new InvokeException(throwable);
            }
        }
    }

}
