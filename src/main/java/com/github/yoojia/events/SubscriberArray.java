package com.github.yoojia.events;

import com.github.yoojia.events.emitter.RealSubscriber;
import com.github.yoojia.events.supports.ImmutableList;

import java.util.Collections;
import java.util.List;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.4
 */
final class SubscriberArray extends ImmutableList<RealSubscriber> {

    public SubscriberArray(List<RealSubscriber> source) {
        super(source.toArray(new RealSubscriber[source.size()]));
    }

    public static SubscriberArray empty(){
        final List<RealSubscriber> empty = Collections.emptyList();
        return new SubscriberArray(empty);
    }
}
