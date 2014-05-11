package org.jroute.route;

import org.jroute.http.request.Request;
import org.jroute.http.response.Response;
import org.jroute.route.endpoint.CallIniter;

public class RequestHandler {

    private final CallIniter caller;
    private final UriHandler handler;

    public RequestHandler(final UriHandler handler, final CallIniter caller) {
        this.caller = caller;
        this.handler = handler;
    }

    public Response handle(final Request request) throws ReflectiveOperationException {
        return caller.call(request);
    }

    public boolean canHandle(final Request request) {
        return handler.tryHandle(request);
    }

    @Override
    public String toString() {
        return handler.toString();
    }
}
