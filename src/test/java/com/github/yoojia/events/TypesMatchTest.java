package com.github.yoojia.events;

import org.junit.Assert;
import org.junit.Test;
import org.omg.CORBA.Object;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.github.yoojia.events.InternalFilter.isTypesMatched;

/**
 * @author 陈小锅 (yoojia.chen@gmail.com)
 * @since 2.0
 */
public class TypesMatchTest {

    static final Class<?>[] DEFINE = new Class<?>[]{String.class, Integer.class, Integer.class, Double.class, Double.class};

    @Test
    public void testPass1(){
        Class<?>[] pass = new Class<?>[]{Integer.class, Integer.class, Double.class, String.class, Double.class};
        Assert.assertTrue("Should be TRUE", isTypesMatched(DEFINE, pass));
    }

    @Test
    public void testPass2(){
        Class<?>[] pass = new Class<?>[]{Integer.class, Double.class, String.class, Integer.class, Double.class};
        Assert.assertTrue("Should be TRUE", isTypesMatched(DEFINE, pass));
    }

    @Test
    public void testPassRandomOrder(){
        // test 100 times
        Random rnd = new Random();
        for (int i = 0; i < 100; i++) {
            final List<Class<?>> rndOrderTypes = new ArrayList<>(Arrays.asList(DEFINE));
            for (int j = 0; j < DEFINE.length; j++) {
                final int rndIdx = rnd.nextInt(DEFINE.length);
                Class<?> rm = rndOrderTypes.remove(rndIdx);
                rndOrderTypes.add(rm);
            }
            Assert.assertTrue("Should be TRUE", isTypesMatched(DEFINE, rndOrderTypes.toArray(new Class<?>[DEFINE.length])));
        }
    }

    @Test
    public void testFail(){
        Class<?>[] fail = new Class<?>[]{Integer.class};
        Assert.assertFalse("Should be FALSE", isTypesMatched(DEFINE, fail));
    }

    @Test
    public void testFail1(){
        Class<?>[] fail = new Class<?>[]{String.class, Integer.class, Integer.class, Double.class, Double.class, Object.class};
        Assert.assertFalse("Should be FALSE", isTypesMatched(DEFINE, fail));
    }

    @Test
    public void testFail2(){
        Class<?>[] fail = new Class<?>[]{String.class, Integer.class, Integer.class, Integer.class, Double.class};
        Assert.assertFalse("Should be FALSE", isTypesMatched(DEFINE, fail));
    }

    @Test
    public void testFail3(){
        Class<?>[] fail = new Class<?>[]{String.class, Integer.class, Integer.class, Integer.class, Integer.class};
        Assert.assertFalse("Should be FALSE", isTypesMatched(DEFINE, fail));
    }

    @Test
    public void testFail4(){
        Class<?>[] fail = new Class<?>[]{Integer.class, Integer.class, Integer.class, Integer.class, Integer.class};
        Assert.assertFalse("Should be FALSE", isTypesMatched(DEFINE, fail));
    }

}
