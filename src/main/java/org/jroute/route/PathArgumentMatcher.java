package org.jroute.route;

import java.util.Map;

class PathArgumentMatcher extends PathMatcher {

    private final String name;

    public PathArgumentMatcher(final String name) {
        this.name = name;
    }

    @Override
    public boolean isMatch(final String data) {
        return !data.isEmpty();
    }

    @Override
    public void addMapping(final Map<String, String> mapping, final String data) {
        mapping.put(name, data);
    }

    @Override
    public String toString() {
        return ":" + name;
    }
}
