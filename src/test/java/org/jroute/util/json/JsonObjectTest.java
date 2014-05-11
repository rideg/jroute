package org.jroute.util.json;

import static org.hamcrest.Matchers.is;
import static org.jroute.util.json.JsonUtil.o;
import static org.jroute.util.json.JsonUtil.p;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class JsonObjectTest {

    public static class A {

        public String data;
    }

    public static class B {
        public int data;
        public Integer data2;
    }

    public static class C {
        public A data;
        private String name;

        public String getName() {
            return name;
        }
    }

    @Test
    public void loadLoadsAllStringData() {
        // given
        JsonObject object = new JsonObject(new JsonPair("data", "value"));

        // when
        A a = object.load(A.class);

        // then
        assertThat(a.data, is("value"));
    }

    @Test
    public void loadLoadsAllIntegerData() {
        // given
        JsonObject object = o(p("data", 1), p("data2", 3));

        // when
        B b = object.load(B.class);

        // then
        assertThat(b.data, is(1));
        assertThat(b.data2, is(3));
    }

    @Test
    public void loadConvertsNumericData() {
        // given
        JsonObject object = o(p("data", 1L), p("data2", (short) 3));

        // when
        B b = object.load(B.class);

        // then
        assertThat(b.data, is(1));
        assertThat(b.data2, is(3));
    }

    @Test
    public void loadLoadsObjectsToo() throws Exception {
        // given
        JsonObject object = o(p("data", o(p("data", "hello"))), p("name", "hoho"));

        // when
        C c = object.load(C.class);

        // then
        assertThat(c.data.data, is("hello"));
        assertThat(c.getName(), is("hoho"));
    }

    @Test
    public void numericValuesConverte() throws Exception {
        // given

        // when
        // then
    }

}
