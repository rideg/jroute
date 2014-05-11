package org.jroute.http.request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jroute.http.Cookie;
import org.jroute.http.HttpMethod;

public class Request {

    private Map<String, String> mappings = new HashMap<>();
    private Map<String, String> headers = new HashMap<>();
    private final Map<String, Cookie> cookies = new HashMap<>();

    private final HttpMethod method;
    private final String path;
    private Map<String, List<String>> parameters;

    public Request(final HttpMethod method, final String path) {
        this.method = method;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public Map<String, String> queryParams() {
        return null;
    }

    public void setMappings(final Map<String, String> mappings) {
        this.mappings = mappings;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getMappings() {
        return mappings;
    }

    public void setHeaders(final Map<String, String> headers) {
        this.headers = headers;
    }

    public void setCookie(final Cookie cookie) {
        cookies.put(cookie.getName(), cookie);
    }

    public Map<String, Cookie> getCookies() {
        return cookies;
    }

    public Cookie getCookie(final String name) {
        return cookies.get(name);
    }

    public void setQueryParameters(final Map<String, List<String>> parameters) {
        this.parameters = parameters;
    }

    public Map<String, List<String>> getQueryParameters() {
        return parameters;
    }

}
