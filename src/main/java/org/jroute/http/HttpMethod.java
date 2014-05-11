package org.jroute.http;

public enum HttpMethod {

    GET("GET"), POST("POST"), PUT("PUT"), DELETE("DELETE"), HEAD("HEAD");

    private final String string;

    private HttpMethod(final String string) {
        this.string = string;

    }

    @Override
    public String toString() {
        return string;
    }
}
