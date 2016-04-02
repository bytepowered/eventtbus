package com.github.yoojia.events.internal;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.2
 */
public final class EventRunner implements Runnable{

    private final Object mEvent;
    private final Handler mHandler;

    public EventRunner(Object event, Handler handler) {
        this.mEvent = event;
        this.mHandler = handler;
    }

    @Override
    public void run() {
        try{
            mHandler.onEvent(mEvent);
        }catch (Exception errors) {
            try{
                mHandler.onErrors(errors);
            }catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

}
