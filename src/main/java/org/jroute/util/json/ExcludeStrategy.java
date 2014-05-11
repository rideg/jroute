package org.jroute.util.json;

import java.lang.reflect.Field;

public class ExcludeStrategy extends IncludeStrategy {

    public ExcludeStrategy(final String field, final String[] additional) {
        super(field, additional);
    }

    @Override
    public boolean shouldInclude(final Field f) {
        return !super.shouldInclude(f);
    }

}
