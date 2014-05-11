/**
 * Copyright 2013 Boroka Softwares Inc.
 */

package org.jroute.http.request;

import org.jroute.http.HttpMethod;
import org.jroute.util.json.JsonObject;

public class JsonRequest extends RequestWithContent {

    private JsonObject content;

    public JsonRequest(final HttpMethod method, final String path) {
        super(method, path);
    }

    public JsonRequest(final HttpMethod method, final String path, final JsonObject content) {
        super(method, path);
        this.content = content;
    }

    @Override
    public JsonObject getContent() {
        return content;
    }

    public <T> T load(final Class<T> clazz) {
        return content.load(clazz);
    }

}
