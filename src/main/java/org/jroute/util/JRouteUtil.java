package org.jroute.util;

import java.lang.reflect.Constructor;

import sun.misc.Unsafe;

public class JRouteUtil {

    private static final Unsafe UNSAFE;

    static {
        try {
            Constructor<Unsafe> unsafeConstructor = Unsafe.class.getDeclaredConstructor();
            unsafeConstructor.setAccessible(true);
            UNSAFE = unsafeConstructor.newInstance();
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    public static Unsafe getUnsafe() {
        return UNSAFE;
    }

    public static long getOffset(final Class<?> clazz, final String field) {
        try {
            return UNSAFE.objectFieldOffset(clazz.getDeclaredField(field));
        } catch (Exception e) {
            throw new Error(e);
        }
    }
}
