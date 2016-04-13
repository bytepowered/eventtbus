package com.github.yoojia.events;

import com.github.yoojia.events.emitter.Target;
import com.github.yoojia.events.supports.ImmutableList;

import java.util.Collections;
import java.util.List;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.3
 */
final class TargetList extends ImmutableList<Target> {

    public TargetList(List<Target> source) {
        super(source.toArray(new Target[source.size()]));
    }

    public static TargetList empty(){
        final List<Target> empty = Collections.emptyList();
        return new TargetList(empty);
    }
}
