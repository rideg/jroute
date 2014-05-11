package org.jroute.util.json;

import java.lang.reflect.Field;

public abstract class FilterStrategy {

    public static final FilterStrategy INCLUDE_ALL = new FilterStrategy() {
        @Override
        public boolean shouldInclude(final Field f) {
            return true;
        }
    };

    private Object object;

    public abstract boolean shouldInclude(final Field f);

    public FilterStrategy setObject(final Object object) {
        this.object = object;
        return this;
    }

    protected Object getObject() {
        return object;
    }

}
