package com.github.yoojia.events;

import com.github.yoojia.events.internal.Acceptor;
import com.github.yoojia.events.supports.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.2
 */
final class Acceptors extends ImmutableList<Acceptor> {

    public Acceptors(List<Acceptor> source) {
        super(source.toArray(new Acceptor[source.size()]));
    }

    public static Acceptors empty(){
        final List<Acceptor> empty = Collections.emptyList();
        return new Acceptors(empty);
    }
}
