package com.github.yoojia.events;

import com.github.yoojia.events.internal.*;

import java.util.concurrent.ExecutorService;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.2
 */
public class Schedules {

    public static Scheduler newService(ExecutorService workers) {
        return new ThreadsSchedule(workers, workers);
    }

    public static Scheduler sharedThreads(){
        return SharedSchedule.getDefault();
    }

    public static Scheduler newCaller(){
        return new Scheduler0();
    }
}
