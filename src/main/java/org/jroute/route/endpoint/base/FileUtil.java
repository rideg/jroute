package org.jroute.route.endpoint.base;

import static com.google.common.net.HttpHeaders.CACHE_CONTROL;
import static com.google.common.net.HttpHeaders.CONTENT_LENGTH;
import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static com.google.common.net.HttpHeaders.EXPIRES;
import static com.google.common.net.HttpHeaders.IF_MODIFIED_SINCE;
import static com.google.common.net.HttpHeaders.LAST_MODIFIED;
import static org.jroute.http.Cookie.HTTP_DATE_FORMATTER;
import static org.jroute.route.endpoint.base.MimeMapper.map;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.jroute.http.Cookie;
import org.jroute.http.request.Request;
import org.jroute.http.response.Response;
import org.jroute.http.response.Responses;
import org.jroute.http.response.StreamResponse;
import org.jroute.route.endpoint.base.FileCache.CacheEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FileUtil {

    private static FileCache cache = new FileCache();
    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    private FileUtil() {
        // nothing
    }

    private static String extension(final String path) {
        return path.substring(path.lastIndexOf('.') + 1, path.length());
    }

    public static Response response(final Request request, final File file) {
        if (!file.exists()) {
            return Responses.RESPONSE_404;
        }
        CacheEntry entry = getCached(file);
        if (shouldResend(entry, request)) {
            return createResponse(file, entry);
        }
        return Responses.RESPONSE_304;
    }

    private static boolean shouldResend(final CacheEntry entry, final Request request) {
        String modifiedDate = request.getHeaders().get(IF_MODIFIED_SINCE);
        if (modifiedDate != null) {
            try {
                DateTime limit = Cookie.HTTP_DATE_FORMATTER.parseDateTime(modifiedDate);
                return limit.plusSeconds(1).isBefore(entry.getLatsModified());
            } catch (IllegalArgumentException e) {
                logger.warn("Cannot parse date!", e);
            }
        }
        return true;
    }

    private static CacheEntry getCached(final File file) {
        CacheEntry entry = cache.get(file);
        if (entry == null) {
            entry = cache.load(file);
        }
        return entry;
    }

    private static StreamResponse createResponse(final File file, final CacheEntry entry) {
        final StreamResponse response = new StreamResponse(entry.getData());

        final Map<String, String> header = response.getHeader();
        header.put(CONTENT_TYPE, map(extension(file.getName())));
        header.put(CONTENT_LENGTH, String.valueOf(entry.size()));
        header.put(CACHE_CONTROL, "max-age=" + TimeUnit.DAYS.toSeconds(1));
        header.put(EXPIRES, tomorrow().toString(HTTP_DATE_FORMATTER));
        header.put(LAST_MODIFIED, entry.getLatsModified().toString(HTTP_DATE_FORMATTER));

        return response;
    }

    private static DateTime tomorrow() {
        return DateTime.now().plusDays(1);
    }

}
