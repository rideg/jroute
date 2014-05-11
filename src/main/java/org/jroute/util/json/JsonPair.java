package org.jroute.util.json;

import static org.jroute.util.json.JsonUtil.append;
import static org.jroute.util.json.JsonUtil.escape;

public class JsonPair {

    private final String name;
    private final Object value;

    public JsonPair(final String name, final Object value) {
        this.name = name;
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue() {
        return (T) value;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder().append('"').append(escape(name)).append('"').append(':');
        append(b, value);
        return b.toString();
    }

    public String getName() {
        return name;
    }
}
