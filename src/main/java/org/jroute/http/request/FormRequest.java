/**
 * Copyright 2013 Boroka Softwares Inc.
 */

package org.jroute.http.request;

import java.util.Map;

import org.jroute.http.HttpMethod;

public class FormRequest extends RequestWithContent {

    private final Map<String, Object> content;

    public FormRequest(final HttpMethod method, final String path, final Map<String, Object> content) {
        super(method, path);
        this.content = content;

    }

    @Override
    public Map<String, Object> getContent() {
        return content;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(final String name) {
        return (T) content.get(name);
    }

}
