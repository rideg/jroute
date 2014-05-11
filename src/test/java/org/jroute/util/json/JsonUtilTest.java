package org.jroute.util.json;

import static org.hamcrest.Matchers.is;
import static org.jroute.util.json.JsonUtil.a;
import static org.jroute.util.json.JsonUtil.o;
import static org.jroute.util.json.JsonUtil.p;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class JsonUtilTest {

    @Test
    public void jsonEmptyObjectToString() {
        assertThat(o().toString(), is("{}"));
    }

    @Test
    public void jsonPairToString() {
        assertThat(p("key", "value").toString(), is("\"key\":\"value\""));
    }

    @Test
    public void jsonPairInObjectToString() {
        assertThat(o(p("key", "value")).toString(), is("{\"key\":\"value\"}"));
    }

    @Test
    public void jsonArrayToString() {
        assertThat(a().toString(), is("[]"));
    }

    @Test
    public void jsonObjectInArray() {
        assertThat(a(o()).toString(), is("[{}]"));
    }

    @Test
    public void moreObjectsInArray() {
        assertThat(a(o(), o(), o()).toString(), is("[{},{},{}]"));
    }

    @Test
    public void escapingDataToString() {
        assertThat(a("string").toString(), is("[\"string\"]"));
        assertThat(a("\t").toString(), is("[\"\\t\"]"));
        assertThat(a("\b").toString(), is("[\"\\b\"]"));
        assertThat(a("\n").toString(), is("[\"\\n\"]"));
        assertThat(a("\r").toString(), is("[\"\\r\"]"));
        assertThat(a("\f").toString(), is("[\"\\f\"]"));
        assertThat(a("/").toString(), is("[\"\\/\"]"));
        assertThat(a("\"").toString(), is("[\"\\\"\"]"));
        assertThat(a("\\").toString(), is("[\"\\\\\"]"));
        assertThat(a("\u0888").toString(), is("[\"\\u0888\"]"));

        assertThat(a(1L).toString(), is("[1]"));
        assertThat(a(1).toString(), is("[1]"));
        assertThat(a((short) 1).toString(), is("[1]"));
        assertThat(a((byte) 1).toString(), is("[1]"));

        assertThat(a(1.1).toString(), is("[1.1]"));
        assertThat(a((float) 1.1).toString(), is("[1.1]"));

        assertThat(a(false).toString(), is("[false]"));
        assertThat(a(true).toString(), is("[true]"));
        assertThat(a(null).toString(), is("[null]"));
        assertThat(a(null, (Object[]) null).toString(), is("[null,null]"));

        assertThat(a(Long.valueOf(1L)).toString(), is("[1]"));
        assertThat(a(Integer.valueOf(1)).toString(), is("[1]"));
        assertThat(a(Short.valueOf((short) 1)).toString(), is("[1]"));
        assertThat(a(Byte.valueOf((byte) 1)).toString(), is("[1]"));

        assertThat(a(Double.valueOf(1.1)).toString(), is("[1.1]"));
        assertThat(a(Float.valueOf((float) 1.1)).toString(), is("[1.1]"));
    }

}
