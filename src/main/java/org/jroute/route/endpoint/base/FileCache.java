package org.jroute.route.endpoint.base;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.joda.time.DateTime;
import org.joda.time.base.AbstractDateTime;

import com.google.common.io.Files;

public class FileCache {

    public static class CacheEntry {

        private final DateTime storedAt;
        private final byte[] data;

        public CacheEntry(final File file) {
            try {
                storedAt = new DateTime(file.lastModified());
                data = Files.toByteArray(file);
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }

        public InputStream getData() {
            return new ByteArrayInputStream(data);
        }

        public boolean isStillValid(final long lastModified) {
            return !storedAt.isBefore(lastModified);
        }

        public int size() {
            return data.length;
        }

        public AbstractDateTime getLatsModified() {
            return new DateTime(storedAt.getMillis());
        }
    }

    private final Map<String, SoftReference<CacheEntry>> cache;

    public FileCache() {
        cache = new ConcurrentHashMap<>();
    }

    public CacheEntry get(final File file) {
        SoftReference<CacheEntry> r = cache.get(file.getAbsolutePath());
        if (r != null) {
            CacheEntry entry = r.get();
            if (entry != null && entry.isStillValid(file.lastModified())) {
                return entry;
            } else {
                cache.remove(file.getAbsolutePath());
            }
        }
        return null;
    }

    public CacheEntry load(final File file) {
        CacheEntry entry = new CacheEntry(file);
        cache.put(file.getAbsolutePath(), new SoftReference<CacheEntry>(entry));
        return entry;
    }
}
