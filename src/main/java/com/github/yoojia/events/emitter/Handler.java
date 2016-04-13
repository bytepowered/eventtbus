package com.github.yoojia.events.emitter;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.2
 */
public interface Handler {

    /**
     * Process event message
     * @param event Event message
     * @throws Exception Impl can throw exceptions
     */
    void onEvent(Object event) throws Exception;

    /**
     * catch onEvent(Event) exceptions
     * @param errors Exception
     */
    void onErrors(Exception errors);

}
