/**
 * Copyright 2013 Boroka Softwares Inc.
 */

package org.jroute.http.response;

import java.io.InputStream;

public class StreamResponse extends ResponseWithContent<InputStream> {

    private final InputStream content;

    public StreamResponse(final InputStream content) {
        this.content = content;
    }

    @Override
    public InputStream content() {
        return content;
    }

}
