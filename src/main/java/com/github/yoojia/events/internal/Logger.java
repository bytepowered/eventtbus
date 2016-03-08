package com.github.yoojia.events.internal;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.0
 */
public class Logger {

    public static void debug(String tag, String message) {
        System.err.println("[" + tag + "]: " + message);
    }
}
