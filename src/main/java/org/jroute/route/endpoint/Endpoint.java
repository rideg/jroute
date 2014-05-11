package org.jroute.route.endpoint;

import org.jroute.http.request.Request;
import org.jroute.http.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Endpoint<T extends Request> {

    private T request;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Response response;

    public Endpoint<T> setRequest(final T request) {
        this.request = request;
        return this;
    }

    protected T request() {
        return request;
    }

    protected Logger logger() {
        return logger;
    }

    protected void response(final Response response) {
        this.response = response;
    }

    public Response getResponse() {
        if (response == null) {
            response = new Response();
        }
        return response;
    }

}
