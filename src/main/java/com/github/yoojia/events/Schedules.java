package com.github.yoojia.events;

import com.github.yoojia.events.core.*;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.3
 */
public class Schedules {

    public static Schedule newService(ExecutorService workers) {
        return new Schedule(workers, workers);
    }

    public static Schedule sharedThreads(){
        return SharedSchedule.getDefault();
    }

    public static Schedule newCaller(){
        return new Schedule() {
            @Override
            public void submit(EventMessage event, List<EventHandler> handlers) {
                for (EventHandler handler : handlers) {
                    final int type = handler.scheduleType();
                    if (Schedule.ON_CALLER_THREAD != type) {
                        System.err.println("Ignore <ScheduleType> on Caller Schedule");
                    }
                    new EventRunner(event, handler).run();
                }
            }
        };
    }
}
