package org.jroute.route.endpoint.base;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.io.Resources.readLines;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class MimeMapper {

    private static final String UNKNOWN = "application/octet-stream";
    private static final Map<String, String> MIMES = new HashMap<>();

    static {
        try {
            readLines(MimeMapper.class.getResource("mimes"), UTF_8).stream().forEach(MimeMapper::parse);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private MimeMapper() {
        // nothing
    }

    private static void parse(final String line) {
        String[] splitted = line.split("=");
        for (String extension : extensions(splitted)) {
            MIMES.put(extension, splitted[0]);
        }
    }

    private static String[] extensions(final String[] splitted) {
        return splitted[splitted.length - 1].split(",");
    }

    public static String map(final String extension) {
        String mime = MIMES.get(extension);
        return mime != null ? mime : UNKNOWN;
    }
}
