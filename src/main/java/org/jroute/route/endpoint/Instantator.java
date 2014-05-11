package org.jroute.route.endpoint;

import static java.util.Arrays.copyOf;

import java.lang.reflect.Constructor;

public class Instantator {

    private final Constructor<?> constructor;
    private final Object[] args;

    public Instantator(final Constructor<?> c, final Object[] args) {
        constructor = c;
        this.args = copyOf(args, args.length);
    }

    @SuppressWarnings("unchecked")
    public <T> T newInstance() throws ReflectiveOperationException {
        return (T) constructor.newInstance(args);
    }

}
