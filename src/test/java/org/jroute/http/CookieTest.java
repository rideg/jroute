package org.jroute.http;

import static java.lang.System.currentTimeMillis;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.jroute.http.Cookie;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CookieTest {

    @Before
    public void setUp() {
        DateTimeUtils.setCurrentMillisFixed(currentTimeMillis());
    }

    @After
    public void tearDown() {
        DateTimeUtils.setCurrentMillisSystem();
    }

    @Test
    public void keyValueOnlyCookie() {
        assertThat(new Cookie("key", "value").toString(), is("key=value"));
    }

    @Test
    public void secure() {
        // given
        Cookie cookie = new Cookie("key", "value");

        // and
        cookie.setSecure();

        // when then
        assertThat(cookie.toString(), is("key=value; secure"));
    }

    @Test
    public void expiresAndSecure() {
        // given
        Cookie cookie = new Cookie("key", "value");

        // and
        DateTime now = DateTime.now();
        cookie.setExpiry(now);
        cookie.setSecure();

        // when then
        assertThat(cookie.toString(), is("key=value; expires=" + now.toString(Cookie.HTTP_DATE_FORMATTER) + "; secure"));
    }

    @Test
    public void domain() {
        // given
        Cookie cookie = new Cookie("key", "value");

        // and
        cookie.setDomain("localhost");

        // when then
        assertThat(cookie.toString(), is("key=value; domain=localhost"));
    }

    @Test
    public void httpOnly() {
        // given
        Cookie cookie = new Cookie("key", "value");

        // and
        cookie.setDomain("localhost");
        cookie.setHttpOnly();

        // when then
        assertThat(cookie.toString(), is("key=value; domain=localhost; HttpOnly"));
    }

    @Test
    public void path() {
        // given
        Cookie cookie = new Cookie("key", "value");

        // and
        cookie.setDomain("localhost");
        cookie.setPath("/test");
        cookie.setHttpOnly();

        // when then
        assertThat(cookie.toString(), is("key=value; domain=localhost; path=/test; HttpOnly"));
    }

}
