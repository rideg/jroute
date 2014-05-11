package org.jroute.route.endpoint;

import org.jroute.http.request.Request;
import org.jroute.http.response.Response;

public interface WrapperBase {

    Response call(Request request) throws ReflectiveOperationException;

}
