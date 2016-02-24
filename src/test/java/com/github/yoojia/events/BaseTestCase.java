package com.github.yoojia.events;

import java.util.concurrent.TimeUnit;

/**
 * @author 陈小锅 (yoojia.chen@gmail.com)
 * @since 1.0
 */
public class BaseTestCase {

    public static long NOW() {
        return System.nanoTime();
    }


    public void printStatistics(String tag, long timeBeforeEmits, long timeAfterEmits, long totalCalls){
        final long timeWhenAllFinished = NOW();
        final long emitMicros = (timeAfterEmits - timeBeforeEmits) / 1000;
        final long deliveredMicros = (timeWhenAllFinished - timeBeforeEmits) / 1000;
        int deliveryRate = (int) (totalCalls / (deliveredMicros / 1000000d));

        System.err.println(tag + "\t ### " +
                "Delivered:" + deliveryRate + "/s" +
                "\t\tEmit:" + TimeUnit.MICROSECONDS.toMillis(emitMicros) + "ms" +
                "\t\tRuns:" + TimeUnit.MICROSECONDS.toMillis(deliveredMicros) + "ms" +
                "\t\tCalls:" + totalCalls
        );
    }

}
