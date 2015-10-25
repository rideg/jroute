package org.jroute.route.endpoint;

import java.util.List;

import org.jroute.http.request.Request;
import org.jroute.http.response.Response;

public class CallIniter implements WrapperBase {

    private final MethodCaller caller;
    private final Instantiator[] chain;

    public CallIniter(final MethodCaller caller, final List<Instantiator> wrappers) {
        this.caller = caller;
        chain = wrappers.toArray(new Instantiator[wrappers.size()]);
    }

    @Override
    public Response call(final Request request) throws ReflectiveOperationException {
        return chainHead().call(request);
    }

    private Wrapper chainHead() throws ReflectiveOperationException {
        Wrapper prev = caller;
        Wrapper actual = null;

        for (int i = chain.length - 1; i >= 0; i--) {
            actual = (Wrapper) chain[i].newInstance();
            actual.setDelegate(prev);
            prev = actual;
        }
        return actual == null ? caller : actual;
    }

}
