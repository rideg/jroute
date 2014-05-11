package org.jroute.route.endpoint.base;

import static com.google.common.net.HttpHeaders.HOST;
import static org.jroute.http.HttpStatusCode.MOVED_PERMANENTLY;

import org.jroute.http.request.Request;
import org.jroute.http.response.RedirectResponse;
import org.jroute.route.endpoint.Endpoint;

public class Redirect extends Endpoint<Request> {

    private static final String HTTP = "http://";
    private final String target;
    private final boolean isAbsolute;

    public Redirect(final String target) {
        this.target = target;
        isAbsolute = target.startsWith(HTTP);
    }

    public void call() {
        response(new RedirectResponse(MOVED_PERMANENTLY, targetLocation()));
    }

    private String targetLocation() {
        return isAbsolute ? target : constructLocal();
    }

    private String constructLocal() {
        return HTTP + request().getHeaders().get(HOST) + target;
    }

}
