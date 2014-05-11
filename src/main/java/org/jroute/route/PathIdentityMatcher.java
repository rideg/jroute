package org.jroute.route;

class PathIdentityMatcher extends PathMatcher {

    private final String pattern;

    public PathIdentityMatcher(final String pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean isMatch(final String data) {
        return pattern.equals(data);
    }

    @Override
    public String toString() {
        return pattern;
    }
}
