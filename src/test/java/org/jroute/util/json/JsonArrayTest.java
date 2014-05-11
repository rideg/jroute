package org.jroute.util.json;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class JsonArrayTest {

    @Test
    public void getByNumber() {
        assertThat((int) new JsonArray(1, 2, 3, 4).get(0), is(1));
        assertThat((int) new JsonArray(1, 2, 3, 4).get(1), is(2));
    }

    @Test
    public void getBySelector() throws Exception {
        assertThat((int) new JsonArray(1, 2, 3, 4).get("0"), is(1));
        assertThat((int) new JsonArray(1, 2, 3, 4).get("1"), is(2));
        assertThat((int) new JsonArray(1, 2, 3, 4).get("first"), is(1));
        assertThat((int) new JsonArray(1, 2, 3, 4).get("last"), is(4));
    }

}
