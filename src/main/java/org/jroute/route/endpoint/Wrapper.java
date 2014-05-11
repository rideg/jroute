package org.jroute.route.endpoint;

import org.jroute.http.request.Request;
import org.jroute.http.response.Response;

public class Wrapper implements WrapperBase {

    private WrapperBase delegate;

    public final void setDelegate(final WrapperBase delegate) {
        this.delegate = delegate;
    }

    @Override
    public Response call(final Request request) throws ReflectiveOperationException {
        return after(request, forward(before(request)));
    }

    protected Response after(final Request request, final Response response) {
        return response;
    }

    protected Response forward(final Request request) throws ReflectiveOperationException {
        return delegate.call(request);
    }

    protected Request before(final Request request) {
        return request;
    }
}
