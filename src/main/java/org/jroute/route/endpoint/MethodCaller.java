package org.jroute.route.endpoint;

import java.lang.reflect.Method;
import java.util.Map;

import org.jroute.http.request.Request;
import org.jroute.http.response.Response;
import org.jroute.route.argument.ArgumentMapper;

public class MethodCaller extends Wrapper {

    private final ArgumentMapper mapper;
    private final Instantator instantator;
    private final Method method;

    public MethodCaller(final Instantator instantator, final Method method) {
        this.instantator = instantator;
        this.method = method;
        mapper = new ArgumentMapper(method);
    }

    @Override
    public Response call(final Request request) throws ReflectiveOperationException {
        return callMethod(endpointFor(request), request.getMappings());
    }

    private Endpoint<Request> endpointFor(final Request request) throws ReflectiveOperationException {
        return instantator.<Endpoint<Request>> newInstance().setRequest(request);
    }

    private Response callMethod(final Endpoint<?> instance, final Map<String, String> params)
            throws ReflectiveOperationException {
        method.invoke(instance, mapper.translate(params));
        return instance.getResponse();
    }
}
