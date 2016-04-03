package com.github.yoojia.events;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Yoojia Chen (yoojia.chen@gmail.com)
 * @since 2.1
 */
public class PayloadEventTest {

    @Test
    public void testObject(){
        PayloadEvent evt = new PayloadEvent("event", 123);
        Assert.assertEquals(Integer.class, evt.types[0]);
        Assert.assertEquals(123, evt.values[0]);
    }

    @Test
    public void testArray(){
        PayloadEvent evt = new PayloadEvent("event", new Object[]{123, "ABC"});
        Assert.assertEquals(Integer.class, evt.types[0]);
        Assert.assertEquals(123, evt.values[0]);
        Assert.assertEquals(String.class, evt.types[1]);
        Assert.assertEquals("ABC", evt.values[1]);
    }
}
