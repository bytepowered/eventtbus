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
    String events();

    /**
     * 回调方式。
     * - 默认方式为 On.CALLER_THREAD；
     * @return On flag
     */
    On schedule() default On.CALLER_THREAD;

}
