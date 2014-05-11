package org.jroute.util.json;

import static org.jroute.util.json.JsonUtil.append;
import static org.jroute.util.json.JsonUtil.escape;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class JsonObject extends JsonContainer {

    private final Map<String, Object> data;

    public JsonObject(final JsonPair... members) {
        super('{', '}');
        data = new HashMap<>(members.length);
        for (JsonPair p : members) {
            add(p);
        }
    }

    @Override
    protected void appendChildren(final StringBuilder b) {
        for (Map.Entry<String, Object> o : data.entrySet()) {
            b.append('"').append(escape(o.getKey())).append('"').append(':');
            append(b, o.getValue());
            b.append(',');
        }
    }

    public final void add(final JsonPair pair) {
        data.put(pair.getName(), pair.getValue());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(final String name) {
        return (T) data.get(name);
    }

    public String getString(final String name) {
        return get(name);
    }

    public <T> T load(final Class<T> clazz) {
        T instance;
        try {
            instance = clazz.newInstance();
            for (Entry<String, Object> e : data.entrySet()) {
                Field f = clazz.getDeclaredField(e.getKey());
                f.setAccessible(true);
                Object value = e.getValue();
                if (value instanceof JsonObject) {
                    value = ((JsonObject) value).load(f.getType());
                }

                f.set(instance, convert(f, value));
            }
            return instance;
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    private Object convert(final Field f, final Object value) throws ReflectiveOperationException {
        if (value instanceof Number && isNumber(f)) {
            return propertConvsionMethod(f).invoke(value, new Object[0]);
        }
        return value;
    }

    private boolean isNumber(final Field f) {
        return Number.class.isAssignableFrom(f.getType()) || f.getType().isPrimitive() && f.getType() != boolean.class;
    }

    private Method propertConvsionMethod(final Field f) throws NoSuchMethodException {
        return Number.class.getMethod(getProperName(f) + "Value", new Class[0]);
    }

    private String getProperName(final Field f) {
        if (f.getType() == Integer.class) {
            return "int";
        }
        return f.getType().getSimpleName().toLowerCase();
    }
}
