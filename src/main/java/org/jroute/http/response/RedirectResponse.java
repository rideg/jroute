/**
 * Copyright 2013 Boroka Softwares Inc.
 */

package org.jroute.http.response;

import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static com.google.common.net.HttpHeaders.LOCATION;

import com.google.common.net.HttpHeaders;

public class RedirectResponse extends TextualResponse {

    public RedirectResponse(final int status, final String location) {
        super(status);
        getHeader().put(HttpHeaders.LOCATION, location);
        getHeader().put(CONTENT_TYPE, "text/html");
        getHeader().put(LOCATION, location);
        write("<html><head></head><body>Redirecting to <a href=\"" + location + "\">" + location
                + "</a>.</body></html>");
    }
}
