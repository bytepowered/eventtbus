package com.github.yoojia.events;

import com.github.yoojia.events.core.EventFilter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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

    /**
     * 自定义事件过滤器
     * @return Filter Types
     * @since 1.2
     */
    Class<? extends EventFilter>[] filters() default {};

}
