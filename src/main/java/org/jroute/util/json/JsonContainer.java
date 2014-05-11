package org.jroute.util.json;

public abstract class JsonContainer {

    private final char startSymbol;
    private final char endSymbol;

    public JsonContainer(final char start, final char end) {
        startSymbol = start;
        endSymbol = end;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(startSymbol);
        appendChildren(b);
        removeLastCommaIfNeeded(b);
        b.append(endSymbol);
        return b.toString();
    }

    protected abstract void appendChildren(StringBuilder b);

    private void removeLastCommaIfNeeded(final StringBuilder builder) {
        if (builder.length() > 1) {
            builder.deleteCharAt(builder.length() - 1);
        }
    }

    public abstract <T> T get(String child);

    @SuppressWarnings("unchecked")
    public <T> T getPath(final String path) {
        if ("".equals(path.trim())) {
            return (T) this;
        }
        String[] segments = path.split("\\.");
        JsonContainer actual = this;
        for (int i = 0; i < segments.length - 1; i++) {
            actual = getProperty(actual, segments[i]);
        }
        return getProperty(actual, segments[segments.length - 1]);
    }

    private <T> T getProperty(final JsonContainer actual, final String segment) {
        int indicator = segment.indexOf('[');
        if (isArray(indicator)) {
            String name = segment.substring(0, indicator);
            String access = getArrayAccess(segment, indicator);
            return ((JsonContainer) actual.getPath(name)).get(access);
        }
        return actual.get(segment);
    }

    private String getArrayAccess(final String segment, final int indicator) {
        return segment.substring(indicator + 1, segment.length() - 1);
    }

    private boolean isArray(final int indicator) {
        return indicator >= 0;
    }
}
