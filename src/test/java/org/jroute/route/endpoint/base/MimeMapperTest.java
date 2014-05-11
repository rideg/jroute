package org.jroute.route.endpoint.base;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class MimeMapperTest {

    @Test
    public void mimerMapsJavaScript() {
        assertThat(MimeMapper.map("js"), is("application/javascript"));
    }

    @Test
    public void mimerReturnsOctetTypeForUnknown() {
        assertThat(MimeMapper.map("jsbtklm"), is("application/octet-stream"));
    }
}
