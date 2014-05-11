package org.jroute.http.response;

import java.io.File;

public class FileResponse extends ResponseWithContent<File> {

    private final File file;

    public FileResponse(final File file) {
        super();
        this.file = file;
    }

    @Override
    public File content() {
        return file;
    }

    @Override
    public String toString() {
        return super.toString() + "File: " + file;
    }

}
