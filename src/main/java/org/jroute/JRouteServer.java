package org.jroute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.Function;

import org.jroute.http.HttpMethod;
import org.jroute.http.request.Request;
import org.jroute.http.response.Response;
import org.jroute.route.endpoint.MethodCaller;
import org.jroute.route.endpoint.Wrapper;

public class JRouteServer {

    private final Map<HttpMethod, List<String>> chains;
    private final Stack<Wrapper> wrappers;

    public JRouteServer() {
        chains = new HashMap<HttpMethod, List<String>>();
        initChains();
        wrappers = new Stack<>();
    }

    private void initChains() {
        for (HttpMethod method : HttpMethod.values()) {
            chains.put(method, new ArrayList<>());
        }
    }

    public JRouteServer wrapper(final Wrapper wrapper) {
        wrappers.push(wrapper);
        return this;
    }

    public JRouteServer wrapper(final Function<Request, Response> f) {
        return this;
    }

    public JRouteServer unwrap() {
        return this;
    }

    public JRouteServer get(final String pattern, final MethodCaller caller) {
        return this;
    }

    public JRouteServer post(final String pattern, final MethodCaller caller) {
        return this;
    }

    public JRouteServer put(final String pattern, final MethodCaller caller) {
        return this;
    }

    public JRouteServer delete(final String pattern, final MethodCaller caller) {
        return this;
    }

}
