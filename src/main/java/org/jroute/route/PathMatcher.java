package org.jroute.route;

import java.util.Map;

abstract class PathMatcher {

    public abstract boolean isMatch(final String data);

    public void addMapping(final Map<String, String> mapping, final String data) {
    }
}
