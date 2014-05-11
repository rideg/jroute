/**
 * Copyright 2013 Boroka Softwares Inc.
 */

package org.jroute.http.response;

import static com.google.common.net.HttpHeaders.CONTENT_TYPE;

import org.jroute.util.json.ObjectSerializer;

public class JsonResponse extends TextualResponse {

    {
        setHeader(CONTENT_TYPE, "application/json; charset=utf-8");
    }

    public JsonResponse() {
        super();
    }

    public JsonResponse(final Object content) {
        super();
        getBuffer().append(serializeIfNeeded(content));
    }

    private Object serializeIfNeeded(final Object content) {
        return content instanceof ObjectSerializer ? ((ObjectSerializer) content).toJson() : content;
    }
}
