package org.jroute.util.json;

import static org.hamcrest.Matchers.is;
import static org.jroute.util.json.JsonUtil.a;
import static org.jroute.util.json.JsonUtil.o;
import static org.jroute.util.json.JsonUtil.p;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class JsonContainerTest {

    @Test
    public void getPropertyOfObject() {
        // given
        JsonContainer p = o(p("data", 1));

        // when
        Integer value = p.getPath("data");

        // then
        assertThat(value, is(1));

    }

    @Test
    public void getObjectChildsProprty() {
        // given
        JsonContainer p = o(p("data", o(p("d", 1))));

        // when
        Integer value = p.getPath("data.d");

        // then
        assertThat(value, is(1));
    }

    @Test
    public void getPathReturnsTheObjectForEmptyString() {
        // given
        JsonContainer p = o(p("data", o(p("d", 1))));

        // then
        assertThat(p.<JsonContainer> getPath(""), is(p));

    }

    @Test
    public void getArrayElement() {
        // given
        JsonContainer p = a(1, 2, 3, 4);

        // when
        Integer value = p.getPath("[2]");

        // then
        assertThat(value, is(3));
    }

    @Test
    public void getPathNavigates() {
        // given
        JsonContainer p = o(p("data", a(1, o(p("dodoka", "daru")), 3, 4)));

        // when
        String value = p.getPath("data[1].dodoka");

        // then
        assertThat(value, is("daru"));
    }
}
