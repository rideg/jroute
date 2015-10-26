package org.jroute.util;

import sun.misc.Unsafe;

import java.lang.reflect.Constructor;

public class UnsafeHelper {

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

}
