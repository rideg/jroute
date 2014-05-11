package org.jroute.util.json;

import static java.lang.reflect.Modifier.isStatic;
import static org.jroute.util.json.JsonUtil.isBoolean;
import static org.jroute.util.json.JsonUtil.isNumber;
import static org.jroute.util.json.convert.JsonConverter.isKnown;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jroute.util.json.convert.JsonConverter;

public class ObjectSerializer {

    private final Object obj;
    private final FilterStrategy filter;

    public ObjectSerializer(final Object object, final FilterStrategy filter) {
        obj = object;
        this.filter = filter.setObject(object);
    }

    public ObjectSerializer(final Object object) {
        obj = object;
        filter = new ExcludeNullStrategy().setObject(object);
    }

    @SuppressWarnings("unchecked")
    public <T> T toJson() {
        if (isPrimitive(obj)) {
            return (T) obj;
        }
        if (isIterable(obj)) {
            return (T) createArray();
        }
        if (isMap(obj)) {
            return (T) convertMap();
        }
        if (isKnown(obj.getClass())) {
            return (T) JsonConverter.convert(obj);
        }
        return (T) convertObject();
    }

    private JsonObject convertObject() {
        JsonObject json = new JsonObject();
        for (Field f : obj.getClass().getDeclaredFields()) {
            if (!isStatic(f.getModifiers()) && filter.shouldInclude(f)) {
                include(json, f);
            }
        }
        return json;
    }

    private JsonObject convertMap() {
        JsonObject json = new JsonObject();
        for (Map.Entry<?, ?> entry : ((Map<?, ?>) obj).entrySet()) {
            json.add(new JsonPair(entry.getKey().toString(), new ObjectSerializer(entry.getValue()).toJson()));
        }
        return json;
    }

    private JsonArray createArray() {
        List<Object> data = new LinkedList<>();
        for (Object e : (Iterable<?>) obj) {
            data.add(new ObjectSerializer(e, filter).toJson());
        }
        return new JsonArray(data.toArray(new Object[data.size()]));
    }

    private boolean isMap(final Object object) {
        return Map.class.isInstance(object);
    }

    public String toJsonString() {
        return toJson().toString();
    }

    private void include(final JsonObject json, final Field f) {
        f.setAccessible(true);
        try {
            json.add(new JsonPair(f.getName(), new ObjectSerializer(f.get(obj)).toJson()));
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    private boolean isIterable(final Object object) {
        return Iterable.class.isInstance(object);
    }

    private boolean isPrimitive(final Object object) {
        return object == null || isBoolean(object) || isNumber(object) || object instanceof String;
    }
}
