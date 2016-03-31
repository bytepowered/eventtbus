package com.github.yoojia.events.supports;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.0
 */
public class ClassTypes {

    /**
     * 获取指定类型的包装类型。包装类型只对Java基础类型生效，其它类型返回本身类型。
     * @param type 指定类型
     * @return 包装类型或者本身类型
     */
    public static Class<?> wrap(Class<?> type){
        if(byte.class.equals(type) || Byte.class.equals(type)) return Byte.class;
        else if(short.class.equals(type) || Short.class.equals(type)) return Short.class;
        else if(int.class.equals(type) || Integer.class.equals(type)) return Integer.class;
        else if(long.class.equals(type) || Long.class.equals(type)) return Long.class;
        else if(float.class.equals(type) || Float.class.equals(type)) return Float.class;
        else if(double.class.equals(type) || Double.class.equals(type)) return Double.class;
        else if(char.class.equals(type) || Character.class.equals(type)) return Character.class;
        else if(boolean.class.equals(type) || Boolean.class.equals(type)) return Boolean.class;
        else if(String.class.equals(type)) return String.class;
        else return type;
    }

    /**
     * 宽泛的比较类型是否相等。如果是Java基础类型，则包装类型与原类型也相等。
     * @param a 需要比较的类型
     * @param b 需要比较的类型
     * @return
     */
    public static boolean lenientlyEquals(Class<?> a, Class<?> b) {
        final Class<?> _a = a.isPrimitive() ? wrap(a): a;
        final Class<?> _b = b.isPrimitive() ? wrap(b) : b;
        return _a.equals(_b);
    }
}
