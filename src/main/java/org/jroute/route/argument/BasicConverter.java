package org.jroute.route.argument;

import java.util.HashMap;
import java.util.Map;

public abstract class BasicConverter {

    private static final Converter<Integer> INETEGER = new Converter<Integer>() {
        @Override
        public Integer convert(final String value) {
            return Integer.parseInt(value);
        }
    };

    private static final Converter<Long> LONG = new Converter<Long>() {
        @Override
        public Long convert(final String value) {
            return Long.parseLong(value);
        }
    };

    private static final Converter<Short> SHORT = new Converter<Short>() {
        @Override
        public Short convert(final String value) {
            return Short.parseShort(value);
        }
    };

    private static final Converter<Byte> BYTE = new Converter<Byte>() {
        @Override
        public Byte convert(final String value) {
            return Byte.parseByte(value);
        }
    };

    private static final Converter<Boolean> BOOLEAN = new Converter<Boolean>() {
        @Override
        public Boolean convert(final String value) {
            return Boolean.parseBoolean(value);
        }
    };

    private static final Converter<String> STRING = new Converter<String>() {
        @Override
        public String convert(final String value) {
            return value;
        }
    };

    private static final Map<Class<?>, Converter<?>> MAPPING = new HashMap<>();

    static {
        MAPPING.put(Integer.class, INETEGER);
        MAPPING.put(int.class, INETEGER);
        MAPPING.put(Long.class, LONG);
        MAPPING.put(long.class, LONG);
        MAPPING.put(Short.class, SHORT);
        MAPPING.put(short.class, SHORT);
        MAPPING.put(Byte.class, BYTE);
        MAPPING.put(byte.class, BYTE);
        MAPPING.put(Boolean.class, BOOLEAN);
        MAPPING.put(boolean.class, BOOLEAN);
        MAPPING.put(String.class, STRING);
    }

    public static Converter<?> getFor(final Class<?> clazz) {
        return MAPPING.get(clazz);
    }

}
