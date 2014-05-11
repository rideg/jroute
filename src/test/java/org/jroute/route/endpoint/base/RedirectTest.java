package org.jroute.route.endpoint.base;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.jroute.http.HttpMethod;
import org.jroute.http.HttpStatusCode;
import org.jroute.http.request.Request;
import org.jroute.http.response.Response;
import org.jroute.http.response.ResponseWithContent;
import org.junit.Test;

import com.google.common.net.HttpHeaders;

public class RedirectTest {

    @SuppressWarnings("unchecked")
    @Test
    public void redirectsAbsolut() {
        // given
        Redirect redirect = new Redirect("http://google.com");

        // when
        redirect.call();

        // then
        Response response = redirect.getResponse();

        assertThat(response.getStatus(), is(HttpStatusCode.MOVED_PERMANENTLY));
        assertThat(response.getHeader().get(HttpHeaders.LOCATION), is("http://google.com"));
        assertThat(response, is(instanceOf(ResponseWithContent.class)));
        assertThat(
                ((ResponseWithContent<StringBuilder>) response).content().toString(),
                is("<html><head></head><body>Redirecting to <a href=\"http://google.com\">http://google.com</a>.</body></html>"));
    }

    @Test
    public void redirectsRelative() {
        // given
        Request request = new Request(HttpMethod.GET, "/where/to/go");
        request.getHeaders().put(HttpHeaders.HOST, "treecreeper.io");

        // and
        Redirect redirect = new Redirect("/go/here");
        redirect.setRequest(request);

        // when
        redirect.call();

        // then
        Response response = redirect.getResponse();

        assertThat(response.getStatus(), is(HttpStatusCode.MOVED_PERMANENTLY));
        assertThat(response.getHeader().get(HttpHeaders.LOCATION), is("http://treecreeper.io/go/here"));
    }

}
