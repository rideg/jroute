package org.jroute.util.json;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class IncludeStrategy extends FilterStrategy {

    private final Set<String> fields;

    public IncludeStrategy(final String field, final String[] additional) {
        fields = new HashSet<>(additional.length + 1);
        fields.addAll(Arrays.asList(additional));
        fields.add(field);
    }

    @Override
    public boolean shouldInclude(final Field f) {
        return fields.contains(f.getName());
    }

}
