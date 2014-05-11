package org.jroute.util.json;

import java.lang.reflect.Field;

public class ExcludeNullStrategy extends FilterStrategy {

    @Override
    public boolean shouldInclude(final Field f) {
        try {
            f.setAccessible(true);
            return f.get(getObject()) != null;
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

}
