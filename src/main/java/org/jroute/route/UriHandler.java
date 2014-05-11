package org.jroute.route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jroute.http.request.Request;

public class UriHandler {

    private final List<PathMatcher> mathers = new ArrayList<>();
    private int limit;

    public UriHandler(final String pattern) {
        if (!isRoot(pattern)) {
            for (String path : pattern.substring(1).split("/")) {
                chekValidity(pattern, path);
                if (isWildcarded(path)) {
                    mathers.add(new PathArgumentMatcher("0"));
                    limit = mathers.size();
                    break;
                } else {
                    mathers.add(createMatcherFor(path));
                }
            }
        }
    }

    private boolean isWildcarded(final String path) {
        return "*".equals(path);
    }

    private void chekValidity(final String pattern, final String path) {
        if ("".equals(path)) {
            throw new IllegalArgumentException(pattern + " is not valid path pattern");
        }
    }

    private boolean isRoot(final String pattern) {
        return "/".equals(pattern);
    }

    private PathMatcher createMatcherFor(final String path) {
        return path.charAt(0) == ':' ? new PathArgumentMatcher(path.substring(1)) : new PathIdentityMatcher(path);
    }

    public boolean tryHandle(final Request request) {
        String[] path = split(request.getPath());
        if (sizeMatch(path)) {
            Map<String, String> mapping = new HashMap<>();
            int i = 0;
            for (String element : path) {
                PathMatcher matcher = mathers.get(i++);
                if (!matcher.isMatch(element)) {
                    return false;
                }
                matcher.addMapping(mapping, element);
            }
            request.setMappings(mapping);
            return true;
        }
        return false;
    }

    private boolean sizeMatch(final String[] path) {
        return path.length == mathers.size();
    }

    private String[] split(final String path) {
        return isRoot(path) ? new String[0] : path.substring(1).split("/", limit);
    }

    @Override
    public String toString() {
        if (mathers.isEmpty()) {
            return "/";
        }
        StringBuilder b = new StringBuilder();
        for (PathMatcher m : mathers) {
            b.append("/").append(m);
        }
        return b.toString();
    }

}
