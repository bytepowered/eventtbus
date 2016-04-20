package com.github.yoojia.events;

import com.github.yoojia.events.emitter.Target;
import com.github.yoojia.events.supports.ImmutableList;

import java.util.Collections;
import java.util.List;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.3
 */
final class TargetArray extends ImmutableList<Target> {

    public TargetArray(List<Target> source) {
        super(source.toArray(new Target[source.size()]));
    }

    public static TargetArray empty(){
        final List<Target> empty = Collections.emptyList();
        return new TargetArray(empty);
    }
}
