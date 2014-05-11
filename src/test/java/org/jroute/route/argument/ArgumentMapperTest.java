
package org.jroute.route.argument;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class ArgumentMapperTest {

    public static class ArgumentMapperTestClass {

        public void voidMethod() {
        }

        public void stringMethod(final String data) {
        }

        public void stringMethod2(final String data, final String data2, final String bogyo) {
        }

        public void intMethod(final int data) {
        }

        public void customConverterMethod(final @CustomConverter(TestConverter.class) String data) {
        }
    }

    public static class TestConverter implements Converter<String> {

        @Override
        public String convert(final String value) {
            return "testconverter";
        }

    }

    private ArgumentMapper mapper;

    @Test
    public void mapperReturnEmptyArgumentArray_InCaseOfVoidMethod() throws Exception {
        // given
        mapper = new ArgumentMapper(getMethod("voidMethod"));
        // then
        final Object[] argList = mapper.translate(Collections.<String, String>emptyMap());

        // then
        assertThat(argList.length, is(0));
    }

    @Test
    public void mapperReturnOneStringInArgumentArray_InCaseOfStringMethod() throws Exception {
        // given
        mapper = new ArgumentMapper(getMethod("stringMethod"));
        // then
        final Map<String, String> mapping = new HashMap<>();
        mapping.put("data", "test_data");
        final Object[] argList = mapper.translate(mapping);

        // then
        assertThat(argList.length, is(1));
        assertThat((String) argList[0], is("test_data"));
    }

    @Test
    public void mapperReturnCorrectOrderInArgumentArray() throws Exception {
        // given
        mapper = new ArgumentMapper(getMethod("stringMethod2"));
        // then
        final Map<String, String> mapping = new HashMap<>();
        mapping.put("data", "test_data");
        mapping.put("bogyo", "bogyo_data");
        mapping.put("data2", "test_data2");
        final Object[] argList = mapper.translate(mapping);

        // then
        assertThat(argList.length, is(3));
        assertThat((String) argList[0], is("test_data"));
        assertThat((String) argList[1], is("test_data2"));
        assertThat((String) argList[2], is("bogyo_data"));
    }

    @Test
    public void mapperConvertsIncomingDataToProperType() throws Exception {
        // given
        mapper = new ArgumentMapper(getMethod("intMethod"));
        // then
        final Map<String, String> mapping = new HashMap<>();
        mapping.put("data", "12345");
        final Object[] argList = mapper.translate(mapping);

        // then
        assertThat(argList.length, is(1));
        assertThat((int) argList[0], is(12345));
    }

    @Test
    public void mapperUsesCustonverterIfSpecified() throws Exception {
        // given
        mapper = new ArgumentMapper(getMethod("customConverterMethod"));
        // then
        final Map<String, String> mapping = new HashMap<>();
        mapping.put("data", "12345");
        final Object[] argList = mapper.translate(mapping);

        // then
        assertThat(argList.length, is(1));
        assertThat((String) argList[0], is("testconverter"));
    }

    private Method getMethod(final String name) {
        for (final Method method : ArgumentMapperTestClass.class.getMethods()) {
            if (method.getName().equals(name)) {
                return method;
            }
        }
        return null;
    }

}
