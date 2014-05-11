package org.jroute.http.response;

import static org.jroute.http.HttpStatusCode.INTERNAL_SERVER_ERROR;
import static org.jroute.http.HttpStatusCode.NOT_FOUND;
import static org.jroute.http.HttpStatusCode.NOT_MODIFIED;

public final class Responses {

    private Responses() {
        //
    }

    public static final Response RESPONSE_304 = new Response(NOT_MODIFIED);
    public static final Response RESPONSE_404 = new Response(NOT_FOUND);
    public static final Response RESPONSE_500 = new Response(INTERNAL_SERVER_ERROR);

    public static JsonResponse json(final Object content) {
        return new JsonResponse(content);
    }

}
