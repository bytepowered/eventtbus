package com.github.yoojia.events;

import com.github.yoojia.events.emitter.Invoker;
import com.github.yoojia.events.emitter.Handler;
import com.github.yoojia.events.emitter.Scheduler;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.2
 */
public class Schedulers {

    public static Scheduler newService(ExecutorService workers) {
        return new ThreadsScheduler(workers, workers);
    }

    public static Scheduler sharedThreads(){
        return SharedScheduler.getDefault();
    }

    public static Scheduler newCaller() {
        return new Scheduler() {

            @Override
            public void schedule(Object event, List<? extends Handler> handlers) {
                for (Handler handler : handlers) {
                    new Invoker(event, handler).run();
                }
            }
        };
    }
}
