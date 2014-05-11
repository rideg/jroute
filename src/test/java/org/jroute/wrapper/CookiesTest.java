package org.jroute.wrapper;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;

import org.jroute.http.HttpMethod;
import org.jroute.http.request.Request;
import org.jroute.http.response.Response;
import org.jroute.route.endpoint.WrapperBase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.net.HttpHeaders;

public class CookiesTest {

    private Request request;
    private WrapperBase mock;
    private Cookies cookies;

    @Before
    public void setUp() throws Exception {
        request = new Request(HttpMethod.GET, "/");
        mock = Mockito.mock(WrapperBase.class);
        doReturn(new Response()).when(mock).call(request);
        cookies = new Cookies();
        cookies.setDelegate(mock);
    }

    @Test
    public void noCookieHeader() throws ReflectiveOperationException {
        // when
        cookies.call(request);

        // then
        assertThat(request.getCookies().size(), is(0));
    }

    @Test
    public void oneCookieInHeader() throws ReflectiveOperationException {
        // given
        request.getHeaders().put(HttpHeaders.COOKIE, "cookie=value");
        // when
        cookies.call(request);

        // then
        assertThat(request.getCookies().size(), is(1));
        assertThat(request.getCookie("cookie").getValue(), is("value"));
    }

    @Test
    public void twoCookiesInHeader() throws ReflectiveOperationException {
        // given
        request.getHeaders().put(HttpHeaders.COOKIE, "cookie=value; cookie2=value2");
        // when
        cookies.call(request);

        // then
        assertThat(request.getCookies().size(), is(2));
        assertThat(request.getCookie("cookie").getValue(), is("value"));
        assertThat(request.getCookie("cookie2").getValue(), is("value2"));
    }
}
