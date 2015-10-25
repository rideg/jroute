package org.jroute.http;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import static java.util.TimeZone.getTimeZone;
import static org.joda.time.DateTimeZone.forTimeZone;
import static org.joda.time.format.DateTimeFormat.forPattern;

public class Cookie {

    private final String name;
    private final String value;
    private boolean secure = false;
    public static final DateTimeFormatter HTTP_DATE_FORMATTER = forPattern("E, dd MMM YYYY HH:mm:ss z").withZone(
            forTimeZone(getTimeZone("GMT")));
    private DateTime expiry;
    private String domain;
    private boolean httpOnly;
    private String path;

    public Cookie(final String name, final String value) {
        this.name = name;
        this.value = value;
    }

    public Cookie(final String name, final String value, final DateTime expiry) {
        this.name = name;
        this.value = value;
        this.expiry = new DateTime(expiry);
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(name).append('=').append(value);
        if (domain != null) {
            b.append("; domain=").append(domain);
        }
        if (path != null) {
            b.append("; path=").append(path);
        }
        if (expiry != null) {
            b.append("; expires=");
            b.append(expiry.toString(HTTP_DATE_FORMATTER));
        }
        if (secure) {
            b.append("; secure");
        }
        if (httpOnly) {
            b.append("; HttpOnly");
        }
        return b.toString();
    }

    public void setSecure() {
        secure = true;
    }

    public void setExpiry(final DateTime expiry) {
        this.expiry = new DateTime(expiry);
    }

    public void setDomain(final String domain) {
        this.domain = domain;
    }

    public void setHttpOnly() {
        httpOnly = true;
    }

    public void setPath(final String path) {
        this.path = path;

    }
}
