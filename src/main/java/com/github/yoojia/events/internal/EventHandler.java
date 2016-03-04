package com.github.yoojia.events.internal;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.2
 */
public interface EventHandler {

    /**
     * Process event message
     * @param event Event message
     * @throws Exception Impl can throw exceptions
     */
    void onEvent(Object event) throws Exception;

    /**
     * try onEvent(InternalEvent) exceptions
     * @param errors Exception
     */
    void onErrors(Exception errors);

    /**
     * Tell your schedule type to Dispatcher.Schedule
     * @return Schedule Type
     */
    int scheduleType();
}
