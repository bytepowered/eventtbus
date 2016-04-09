package com.github.yoojia.events.supports;

/**
 * @author Yoojia Chen (yoojia.chen@gmail.com)
 * @since 2.0
 */
public interface Transformer<S, M> {

    M transform(S value);
}
