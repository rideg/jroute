package org.jroute.route.endpoint.base;

import java.io.File;
import java.io.IOException;

import org.jroute.http.request.Request;
import org.jroute.route.endpoint.Endpoint;

public class Static extends Endpoint<Request> {

    private final String path;

    public Static(final String path) {
        this.path = path;
    }

    public void call() throws IOException {
        response(FileUtil.response(request(), new File(path)));
    }
}
