package org.jroute.route.endpoint.base;

import java.io.File;
import java.io.IOException;

import org.jroute.http.request.Request;
import org.jroute.route.endpoint.Endpoint;

public class Files extends Endpoint<Request> {

    private final File base;

    public Files(final String baseDirPath) {
        base = new File(baseDirPath);
    }

    public Files() {
        this(".");
    }

    public void call(final String path) throws IOException {
        response(FileUtil.response(request(), new File(base, path)));
    }
}
