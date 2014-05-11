package org.jroute.route.argument;

class ConvertHelper {

    private final Converter<?> converter;
    private final int index;

    public ConvertHelper(final Converter<?> converter, final int index) {
        this.index = index;
        this.converter = converter;
    }

    public Object convert(final String data) {
        return converter.convert(data);
    }

    public int index() {
        return index;
    }

}