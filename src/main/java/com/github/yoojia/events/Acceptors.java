package com.github.yoojia.events;

import com.github.yoojia.events.internal.Acceptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.2
 */
final class Acceptors extends ArrayList<Acceptor> {

    public Acceptors(int initialCapacity) {
        super(initialCapacity);
    }

    private Acceptors(Collection<? extends Acceptor> c) {
        super(c);
    }

    public static Acceptors empty(){
        final List<Acceptor> empty = Collections.emptyList();
        return new Acceptors(empty);
    }
}
