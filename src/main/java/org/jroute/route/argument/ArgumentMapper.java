
package org.jroute.route.argument;

import static java.lang.String.valueOf;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.thoughtworks.paranamer.BytecodeReadingParanamer;

public class ArgumentMapper {

    private final Map<String, ConvertHelper> mapping;

    public ArgumentMapper(final Method method) {
        mapping = new HashMap<>();
        createMapping(method);
    }

    private void createMapping(final Method method) {
        final Class<?>[] types = method.getParameterTypes();
        final Annotation[][] annots = method.getParameterAnnotations();

        int i = 0;
        for (final String name : getParameterNames(method)) {
            final ConvertHelper converter = new ConvertHelper(getConverter(types[i], annots[i]), i);
            mapping.put(name, converter);
            mapping.put(valueOf(i++), converter);
        }
    }

    private Converter<?> getConverter(final Class<?> clazz, final Annotation[] annotations) {
        final Converter<?> converter = getFromAnnotations(annotations);
        return converter == null ? BasicConverter.getFor(clazz) : converter;
    }

    private Converter<?> getFromAnnotations(final Annotation[] annotations) {
        for (final Annotation annot : annotations) {
            if (annot.annotationType() == CustomConverter.class) {
                try {
                    return ((CustomConverter) annot).value().newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new IllegalStateException("Cannot instantate custom converter", e);
                }
            }
        }
        return null;
    }

    private String[] getParameterNames(final Method method) {
        return new BytecodeReadingParanamer().lookupParameterNames(method);
    }

    public Object[] translate(final Map<String, String> params) {
        final Object[] list = new Object[getMappingRealSize()];
        for (final Entry<String, String> entry : params.entrySet()) {
            updateList(list, entry);
        }
        return list;
    }

    private int getMappingRealSize() {
        return mapping.size() / 2;
    }

    private void updateList(final Object[] list, final Entry<String, String> entry) {
        final ConvertHelper helper = mapping.get(entry.getKey());
        list[helper.index()] = helper.convert(entry.getValue());
    }

}
