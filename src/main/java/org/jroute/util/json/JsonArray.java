package org.jroute.util.json;

import static java.lang.Integer.parseInt;
import static java.util.Arrays.asList;
import static org.jroute.util.json.JsonUtil.append;

import java.util.ArrayList;
import java.util.List;

public class JsonArray extends JsonContainer {

    private final List<Object> data;

    public JsonArray(final Object first, final Object... elements) {
        super('[', ']');
        data = new ArrayList<>();
        data.add(first);
        data.addAll(asList(interpret(elements)));
    }

    public JsonArray(final Object[] elements) {
        super('[', ']');
        data = new ArrayList<>(asList(interpret(elements)));
    }

    // interpret null as an array which contains one null value
    private static Object[] interpret(final Object[] elements) {
        return elements == null ? new Object[] { null } : elements;
    }

    public JsonArray() {
        super('[', ']');
        data = new ArrayList<>();
    }

    @Override
    protected void appendChildren(final StringBuilder b) {
        for (Object o : data) {
            append(b, o);
            b.append(',');
        }
    }

    public void add(final Object element) {
        data.add(element);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(final int i) {
        return (T) data.get(i);
    }

    @Override
    public <T> T get(final String path) {
        int position = 0;
        if (Character.isDigit(path.charAt(0))) {
            position = parseInt(path);
        } else if ("first".equals(path)) {
            position = 0;
        } else if ("last".equals(path)) {
            position = data.size() - 1;
        }
        return get(position);
    }
}
