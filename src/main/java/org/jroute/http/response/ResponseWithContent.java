package org.jroute.http.response;

import static com.google.common.net.HttpHeaders.CONTENT_LENGTH;

import com.google.common.net.HttpHeaders;

public abstract class ResponseWithContent<T> extends Response {

    {
        getHeader().put(CONTENT_LENGTH, "0");
        getHeader().put(HttpHeaders.CONTENT_TYPE, "text/html; charset=UTF-8");
    }

    public ResponseWithContent() {
        super();
    }

    public ResponseWithContent(final int status) {
        super(status);
    }

    /**
     * Returns the content associated with the Response
     */
    public abstract T content();

}
