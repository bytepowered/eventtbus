package com.github.yoojia.events;

import java.lang.annotation.*;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.0
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Subscribe {

    /**
     * 事件名
     * @return String
     */
    String on();

    /**
     * 回调方式。
     * - 默认方式为 Runs.ON_CALLER_THREAD；
     * @return Runs flag
     */
    Runs run() default Runs.ON_CALLER_THREAD;

}
