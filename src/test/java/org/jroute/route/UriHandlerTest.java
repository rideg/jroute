
package org.jroute.route;

import static org.hamcrest.Matchers.is;
import static org.jroute.http.HttpMethod.GET;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.jroute.http.request.Request;
import org.junit.Test;

public class UriHandlerTest {

    @Test
    public void handlerHandlesStaticUri() {
        assertTrue(new UriHandler("/test/data").tryHandle(new Request(GET, "/test/data")));
        assertFalse(new UriHandler("/test/data").tryHandle(new Request(GET, "/test/data2")));
        assertTrue(new UriHandler("/").tryHandle(new Request(GET, "/")));
    }

    @Test
    public void handlerHandlesDynamicUri() {
        assertTrue(new UriHandler("/test/:data/hello").tryHandle(new Request(GET, "/test/data/hello")));
        assertTrue(new UriHandler("/test/:data/hello").tryHandle(new Request(GET, "/test/data3/hello")));
        assertTrue(new UriHandler("/test/:data/hello").tryHandle(new Request(GET, "/test/other/hello")));
        assertTrue(new UriHandler("/test/:data/hello").tryHandle(new Request(GET, "/test/oBigyo/hello")));

        assertTrue(new UriHandler("/test/:data/hello/:id").tryHandle(new Request(GET, "/test/oBigyo/hello/12")));
        assertTrue(new UriHandler("/test/:data/hello/:id").tryHandle(new Request(GET, "/test/oBigyo/hello/cool")));

        assertFalse(new UriHandler("/test/:data/hello").tryHandle(new Request(GET, "/test//hello")));
        assertFalse(new UriHandler("/test/:data/hello").tryHandle(new Request(GET, "/test2/data/hello")));
    }

    @Test
    public void handlerHandlesWildecardedUri() {
        assertTrue(new UriHandler("/test/*").tryHandle(new Request(GET, "/test/data/hello")));
        assertTrue(new UriHandler("/test/*").tryHandle(new Request(GET, "/test/data3/hello")));
        assertTrue(new UriHandler("/test/*").tryHandle(new Request(GET, "/test/other/hello")));
        assertTrue(new UriHandler("/test/*").tryHandle(new Request(GET, "/test/oBigyo/hello")));
        assertTrue(new UriHandler("/test/*").tryHandle(new Request(GET, "/test//hello")));

        assertFalse(new UriHandler("/test/*").tryHandle(new Request(GET, "/test2/data/hello")));
    }

    @Test
    public void handlerSetsMappingForRequestWithDynamicUri() throws Exception {
        // given
        final Request request = new Request(GET, "/test/data/hello/12");

        // when
        new UriHandler("/test/:data/hello/:id").tryHandle(request);

        // then
        assertThat(request.getMappings().get("data"), is("data"));
        assertThat(request.getMappings().get("id"), is("12"));
    }

    @Test
    public void handlerSetsMappingForRequestWithWildcardUri() throws Exception {
        // given
        final Request request = new Request(GET, "/test/data/hello/12");

        // when
        new UriHandler("/test/data/*").tryHandle(request);

        // then
        assertThat(request.getMappings().get("0"), is("hello/12"));
    }

    @Test
    public void handlerSetsMappingForRequestWithMoreThanOneIdentifier() throws Exception {
        // given
        final Request request = new Request(GET, "/test/14/hello/12");

        // when
        new UriHandler("/test/:id/hello/:data").tryHandle(request);

        // then
        assertThat(request.getMappings().get("id"), is("14"));
        assertThat(request.getMappings().get("data"), is("12"));
    }

    @Test
    public void handlerSetsMappingForRequestWithMoreThanOneIdentifierRightEachAfter() throws Exception {
        // given
        final Request request = new Request(GET, "/test/1/2");

        // when
        new UriHandler("/test/:id/:uid").tryHandle(request);

        // then
        assertThat(request.getMappings().get("id"), is("1"));
        assertThat(request.getMappings().get("uid"), is("2"));
    }
}
