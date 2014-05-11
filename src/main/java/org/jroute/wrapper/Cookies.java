package org.jroute.wrapper;

import org.jroute.http.Cookie;
import org.jroute.http.request.Request;
import org.jroute.route.endpoint.Wrapper;

import com.google.common.net.HttpHeaders;

public class Cookies extends Wrapper {

    @Override
    protected Request before(final Request request) {
        addCookies(request);
        return request;
    }

    private void addCookies(final Request request) {
        String cookieHeader = request.getHeaders().get(HttpHeaders.COOKIE);
        if (cookieHeader != null) {
            for (String cookie : cookieHeader.split(";")) {
                request.setCookie(parseOne(cookie));
            }
        }
    }

    private Cookie parseOne(final String cookie) {
        String[] split = cookie.split("=");
        return new Cookie(split[0].trim(), split[1].trim());
    }

}
