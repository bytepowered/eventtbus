package com.github.yoojia.events;

import org.junit.Assert;
import org.junit.Test;

import static com.github.yoojia.events.InternalEventFilter.isTypesMatched;

/**
 * @author 陈小锅 (yoojia.chen@gmail.com)
 * @since 2.0
 */
public class TypesMatchTest {

    @Test
    public void test(){

        Class<?>[] define = new Class<?>[]{String.class, Integer.class, Integer.class, Double.class, Double.class};
        Class<?>[] pass1 = new Class<?>[]{Integer.class, Integer.class, Double.class, String.class, Double.class};
        Class<?>[] pass2 = new Class<?>[]{Integer.class, Double.class, String.class, Integer.class, Double.class};

        Class<?>[] fail0 = new Class<?>[]{String.class, Integer.class, Integer.class, Integer.class, Double.class};
        Class<?>[] fail1 = new Class<?>[]{Integer.class, Integer.class, Float.class, String.class, Double.class};
        Class<?>[] fail2 = new Class<?>[]{Integer.class, Integer.class};
        Class<?>[] fail3 = new Class<?>[]{Integer.class, Integer.class, Integer.class, Integer.class, Integer.class};
        Class<?>[] fail4 = new Class<?>[]{String.class, String.class, String.class, String.class, String.class};

        Assert.assertTrue("Should be TRUE", isTypesMatched(define, pass1));
        Assert.assertTrue("Should be TRUE", isTypesMatched(define, pass2));

        Assert.assertFalse("Should be FALSE", isTypesMatched(define, fail0));
        Assert.assertFalse("Should be FALSE", isTypesMatched(define, fail1));
        Assert.assertFalse("Should be FALSE", isTypesMatched(define, fail2));
        Assert.assertFalse("Should be FALSE", isTypesMatched(define, fail3));
        Assert.assertFalse("Should be FALSE", isTypesMatched(define, fail4));

    }
}
