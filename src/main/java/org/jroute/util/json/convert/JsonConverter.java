package org.jroute.util.json.convert;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public abstract class JsonConverter<T> {

    private static final Map<Class<?>, JsonConverter<?>> CONVERTERS = new HashMap<>();

    static {
        CONVERTERS.put(Timestamp.class, new TimeStampConverter());
    }

    public static boolean isKnown(final Class<? extends Object> clazz) {
        return CONVERTERS.containsKey(clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T> Object convert(final T obj) {
        return ((JsonConverter<T>) CONVERTERS.get(obj.getClass())).convertIntance(obj);
    }

    protected abstract Object convertIntance(T object);

}
