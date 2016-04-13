package com.github.yoojia.events;

import com.github.yoojia.events.emitter.Invoker;
import com.github.yoojia.events.emitter.Handler;
import com.github.yoojia.events.emitter.Scheduler;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.0
 */
public class ThreadsScheduler implements Scheduler {

    private final Queue<Element> mLoopTasks = new ConcurrentLinkedQueue<>();

    private final ExecutorService mWorkerThreads;
    private final ExecutorService mLoopThread;

    private final ScheduleLooper mLooper = new ScheduleLooper() {
        @Override
        protected void step() {
            final Element el = mLoopTasks.poll();
            if (el == null) {
                await();
            }else{
                invoke(el.scheduleOn, el.event, el.handler);
            }
        }
    };

    public ThreadsScheduler(ExecutorService workerThreads, ExecutorService loopThread) {
        mWorkerThreads = workerThreads;
        mLoopThread = loopThread;
        mLoopThread.submit(mLooper);
    }

    @Override
    public void schedule(Object event, List<? extends Handler> handlers) {
        // 如果是 CALLER 方式, 直接在此处执行.
        // 其它方式在线程池处理再做处理
        for (Handler item : handlers) {
            final EventHandler handler = (EventHandler) item;
            final On scheduleOn = handler.scheduleOn();
            if (On.CALLER_THREAD.equals(scheduleOn)) {
                new Invoker(event, handler).run();
            }else{
                mLoopTasks.offer(new Element(event, handler, scheduleOn));
                synchronized (mLooper) {
                    mLooper.notify();
                }
            }
        }
    }

    public final ExecutorService getWorkerThreads(){
        return mWorkerThreads;
    }

    public final ExecutorService getLoopThread() {
        return mLoopThread;
    }

    protected void invoke(On type, Object event, Handler handler) {
        switch (type) {
            case IO_THREAD:
                mWorkerThreads.submit(new Invoker(event, handler));
                break;
            case MAIN_THREAD:
                handler.onErrors(new IllegalArgumentException("Unsupported <ON_MAIN_THREAD> schedule type! " ));
                break;
            default:
                handler.onErrors(new IllegalArgumentException("Unsupported schedule type: " + type));
                break;
        }
    }

}
