package com.github.yoojia.events.supports;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.0
 */
public class ObjectReference<V> {

    private volatile V mValue;

    public ObjectReference() {
    }

    public ObjectReference(V value) {
        mValue = value;
    }

    public V get(){
        return mValue;
    }

    public void set(V newValue) {
        mValue = newValue;
    }
}
