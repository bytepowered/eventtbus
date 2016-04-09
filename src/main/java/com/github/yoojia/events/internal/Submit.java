package com.github.yoojia.events.internal;

/**
 * @author Yoojia Chen (yoojia.chen@gmail.com)
 * @since 2.0
 */
interface Submit<A, B> {

    void submit(A a, B b);
}
