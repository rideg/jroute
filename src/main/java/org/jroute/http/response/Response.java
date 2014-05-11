package org.jroute.http.response;

import static com.google.common.net.HttpHeaders.CONNECTION;
import static com.google.common.net.HttpHeaders.CONTENT_LENGTH;
import static com.google.common.net.HttpHeaders.DATE;
import static org.jroute.http.Cookie.HTTP_DATE_FORMATTER;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.joda.time.DateTime;
import org.jroute.http.Cookie;
import org.jroute.http.HttpStatusCode;

public class Response {

    private final Map<String, String> header = new HashMap<>();
    private final List<Cookie> cookies = new ArrayList<>();
    private int status;

    {
        header.put(CONNECTION, "keep-alive");
        header.put(CONTENT_LENGTH, "0");
        header.put(DATE, DateTime.now().toString(HTTP_DATE_FORMATTER));
    }

    public Response() {
        status = HttpStatusCode.OK;
    }

    public Response(final int status) {
        this.status = status;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    @SuppressWarnings("unchecked")
    public <T extends Response> T setHeader(final String name, final String value) {
        header.put(name, value);
        return (T) this;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public String toString() {
        final StringBuilder line = new StringBuilder("HTTP/1.1 " + status + "\n");

        for (final Entry<String, String> e : header.entrySet()) {
            line.append(e.getKey()).append(": ").append(e.getValue()).append("\n");
        }
        line.append("\n");
        return line.toString();
    }

    public List<Cookie> getCookies() {
        return cookies;
    }

    public Response addCookie(final Cookie cookie) {
        cookies.add(cookie);
        return this;
    }

    public Response setStatus(final int status) {
        this.status = status;
        return this;
    }
}
