/**
 * Copyright 2013 Boroka Softwares Inc.
 */

package org.jroute.http.request;

import org.jroute.http.HttpMethod;

public abstract class RequestWithContent extends Request {

    public RequestWithContent(final HttpMethod method, final String path) {
        super(method, path);
    }

    public abstract Object getContent();

}
