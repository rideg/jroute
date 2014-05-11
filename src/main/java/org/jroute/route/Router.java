package org.jroute.route;

import static org.jroute.http.response.Responses.RESPONSE_404;
import static org.jroute.http.response.Responses.RESPONSE_500;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.jroute.http.HttpMethod;
import org.jroute.http.request.Request;
import org.jroute.http.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Router {

    private final Map<String, List<RequestHandler>> chains;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public Router(final Map<String, List<RequestHandler>> chains) {
        this.chains = chains;
    }

    public Response route(final Request request) {
        try {
            for (final RequestHandler handler : getMethodHandlers(request)) {
                if (handler.canHandle(request)) {
                    return handler.handle(request);
                }
            }
            logger.debug("Cannot find match for: \"{} {}\"", getMethod(request), request.getPath());
            return RESPONSE_404;
        } catch (final Exception e) {
            logger.error("Excpetion happened during process \"{} {}\"", getMethod(request), request.getPath());
            logger.error("", getCause(e));
            return RESPONSE_500;
        }
    }

    private Throwable getCause(final Exception e) {
        Throwable t = e;
        if (e instanceof InvocationTargetException) {
            t = t.getCause();
        }
        return t;
    }

    private List<RequestHandler> getMethodHandlers(final Request request) {
        final List<RequestHandler> handlers = chains.get(getMethod(request).toString());
        return handlers == null ? Collections.<RequestHandler> emptyList() : handlers;
    }

    private HttpMethod getMethod(final Request request) {
        return request.getMethod() == HttpMethod.HEAD ? HttpMethod.GET : request.getMethod();
    }

}
