package org.jroute.util.json;

import static java.lang.Integer.toHexString;

public final class JsonUtil {

    private static final int UNICODE_MASK = 0x10000;
    private static final int ASCII = 255;
    private static final int MULTIPLY = 4;

    private JsonUtil() {
    }

    public static JsonObject o(final JsonPair... pairs) {
        return new JsonObject(pairs);
    }

    public static JsonPair p(final String name, final Object value) {
        return new JsonPair(name, value);
    }

    public static JsonArray a(final Object first, final Object... data) {
        return new JsonArray(first, data);
    }

    public static JsonArray a(final Object[] data) {
        return new JsonArray(data);
    }

    public static JsonArray a() {
        return new JsonArray();
    }

    public static FilterStrategy include(final String field, final String... additional) {
        return new IncludeStrategy(field, additional);
    }

    public static FilterStrategy exclude(final String field, final String... additional) {
        return new ExcludeStrategy(field, additional);
    }

    public static FilterStrategy excludeNull() {
        return new ExcludeNullStrategy();
    }

    public static ObjectSerializer s(final Object obj) {
        return new ObjectSerializer(obj);
    }

    public static ObjectSerializer s(final Object obj, final FilterStrategy strategy) {
        return new ObjectSerializer(obj, strategy);
    }

    public static String toJson(final Object obj) {
        return s(obj).toJsonString();
    }

    public static String toJson(final Object obj, final FilterStrategy strategy) {
        return s(obj, strategy).toJsonString();
    }

    private static final char[] CONTROL = new char[256];

    static {
        CONTROL['\t'] = 't';
        CONTROL['\b'] = 'b';
        CONTROL['\r'] = 'r';
        CONTROL['\n'] = 'n';
        CONTROL['\f'] = 'f';
        CONTROL['\\'] = '\\';
        CONTROL['"'] = '"';
        CONTROL['/'] = '/';
    }

    static String escape(final Object data) {
        String t = String.valueOf(data);
        StringBuilder builder = new StringBuilder(t.length() * MULTIPLY);

        for (int i = 0; i < t.length(); i++) {
            char c = t.charAt(i);
            if (c > ASCII) {
                appendUnicode(builder, c);
            } else if (CONTROL[c] != 0) {
                builder.append('\\').append(CONTROL[c]);
            } else {
                builder.append(c);
            }
        }
        return builder.toString();
    }

    private static void appendUnicode(final StringBuilder builder, final char c) {
        builder.append('\\');
        builder.append('u');
        builder.append(toHexString(c | UNICODE_MASK).substring(1));
    }

    static void append(final StringBuilder b, final Object o) {
        if (shouldSerialize(o)) {
            append(b, serialize(o));
        } else if (shouldWrap(o)) {
            b.append('"');
            b.append(escape(o));
            b.append('"');
        } else {
            b.append(o);
        }
    }

    private static Object serialize(final Object o) {
        return ((ObjectSerializer) o).toJson();
    }

    private static boolean shouldSerialize(final Object o) {
        return o instanceof ObjectSerializer;
    }

    private static boolean shouldWrap(final Object o) {
        return o != null && isString(o);
    }

    private static boolean isString(final Object o) {
        return !isJsonType(o) && !isNumber(o) && !isBoolean(o);
    }

    private static boolean isJsonType(final Object o) {
        return JsonArray.class.isInstance(o) || JsonObject.class.isInstance(o) || JsonPair.class.isInstance(o);
    }

    static boolean isBoolean(final Object o) {
        return Boolean.class.isInstance(o) || boolean.class.isInstance(o);
    }

    static boolean isNumber(final Object o) {
        return Number.class.isInstance(o) || isInteger(o) || isFlotaing(o);
    }

    private static boolean isFlotaing(final Object o) {
        return double.class.isInstance(o) || float.class.isInstance(o);
    }

    private static boolean isInteger(final Object o) {
        return int.class.isInstance(o) || long.class.isInstance(o) || short.class.isInstance(o)
                || byte.class.isInstance(o);
    }

}
