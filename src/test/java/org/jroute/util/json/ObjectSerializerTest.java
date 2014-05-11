package org.jroute.util.json;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.jroute.util.json.JsonUtil.exclude;
import static org.jroute.util.json.JsonUtil.excludeNull;
import static org.jroute.util.json.JsonUtil.include;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class ObjectSerializerTest {

    public static class Empty {

    }

    @SuppressWarnings("unused")
    public static class OneField {
        private final String data = "test_data";
    }

    @SuppressWarnings("unused")
    public static class OneCompositeField {

        private static final long serialVersionUID = -1;

        private final OneField field = new OneField();
    }

    @SuppressWarnings("unused")
    public static class OneIterableField {
        private final Iterable<?> field = Arrays.asList(1, "two");
    }

    @SuppressWarnings("unused")
    public static class MultipleFields {
        private final int one = 1;
        private final int two = 2;
        private final int three = 3;
    }

    public static class NullFields {
        public String data = null;
        public String data2 = "Hello";
    }

    @Test
    public void emptyObject() {
        assertThat(new ObjectSerializer(new Empty()).toJson().toString(), is("{}"));
    }

    @Test
    public void onePrimitiveField() {
        assertThat(new ObjectSerializer(new OneField()).toJson().toString(), is("{\"data\":\"test_data\"}"));
    }

    @Test
    public void oneIterableField() {
        assertThat(new ObjectSerializer(new OneIterableField()).toJson().toString(), is("{\"field\":[1,\"two\"]}"));
    }

    @Test
    public void includeFields() {
        String data = new ObjectSerializer(new MultipleFields(), include("one", "three")).toJson().toString();
        assertThat(data, containsString("\"one\":1"));
        assertThat(data, containsString("\"three\":3"));
    }

    @Test
    public void excludeFields() {
        assertThat(new ObjectSerializer(new MultipleFields(), exclude("one", "three")).toJson().toString(),
                is("{\"two\":2}"));
    }

    @Test
    public void excludeNullFields() {
        assertThat(new ObjectSerializer(new NullFields(), excludeNull()).toJson().toString(),
                is("{\"data2\":\"Hello\"}"));
    }

    @Test
    public void serializeMapsAsJsonObject() throws Exception {
        // given
        Map<String, Integer> data = new HashMap<>();

        // and
        data.put("data1", 1);
        data.put("data2", 2);

        // when
        JsonObject ret = new ObjectSerializer(data).toJson();

        // then
        assertThat(ret.<Integer> get("data1"), is(1));
        assertThat(ret.<Integer> get("data2"), is(2));
    }
}
