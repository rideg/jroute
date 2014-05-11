package org.jroute.route.argument;

public interface Converter<T> {

    T convert(final String value);

}
