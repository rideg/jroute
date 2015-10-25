
package org.jroute.route.endpoint;

import static org.jroute.http.HttpMethod.GET;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.jroute.http.request.Request;
import org.jroute.http.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

public class CallerIniterTest {

    private Request request;
    private Map<String, String> headers;

    public static class TestEndpoint extends Endpoint<Request> {
        public Response call() {
            final Request request = request();
            request.getHeaders().put("call", "");
            return new Response();
        }
    }

    private static final MethodCaller ENDPOINT_CALL =
            new MethodCaller(instantatorFor(TestEndpoint.class), method());

    private static Instantiator instantatorFor(final Class<?> clazz) {
        try {
            return new Instantiator(clazz.getDeclaredConstructor(), new Object[0]);
        } catch (final ReflectiveOperationException e) {
            return null;
        }
    }

    private static Method method() {
        try {
            return TestEndpoint.class.getMethod("call");
        } catch (NoSuchMethodException | SecurityException e) {
        }
        return null;
    }

    public static class TestWrapperWithBeforeAndAfter extends Wrapper {

        Request request;

        @Override
        protected Request before(final Request request) {
            this.request = request;
            request.getHeaders().put("before", "");
            return request;
        }

        @Override
        protected Response after(final Request request, final Response response) {
            request.getHeaders().put("after", "");
            return response;
        }
    }

    public static class TestWrapperWithBefore extends Wrapper {
        @Override
        protected Request before(final Request request) {
            request.getHeaders().put("far before", "");
            return request;
        }
    }

    public static class TestWrapperWithState extends Wrapper {
        private int data = 1;

        @Override
        public Request before(final Request request) {
            request.getHeaders().put("before " + data++, "");
            return request;
        }
    }

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {
        request = new Request(GET, "");
        headers = mock(Map.class);
        request.setHeaders(headers);
        request.setMappings(Collections.<String, String>emptyMap());
    }

    @Test
    public void callerCreatesWrapperForEndpoint() throws ReflectiveOperationException {
        // when
        final CallIniter caller = new CallIniter(ENDPOINT_CALL, wrappers(TestWrapperWithBeforeAndAfter.class));

        // when
        caller.call(request);

        // then
        final InOrder order = inOrder(headers);
        order.verify(headers).put("before", "");
        order.verify(headers).put("call", "");
        order.verify(headers).put("after", "");
    }

    @Test
    public void callerCreatesMultipleWrappersForEndpoint() throws ReflectiveOperationException {
        // when

        final CallIniter caller =
                new CallIniter(ENDPOINT_CALL,
                               wrappers(TestWrapperWithBefore.class, TestWrapperWithBeforeAndAfter.class));

        // when
        caller.call(request);

        // then
        final InOrder order = inOrder(headers);
        order.verify(headers).put("far before", "");
        order.verify(headers).put("before", "");
        order.verify(headers).put("call", "");
        order.verify(headers).put("after", "");
    }

    @SafeVarargs
    private final List<Instantiator> wrappers(final Class<? extends Wrapper>... classes) {
        final List<Instantiator> wrappers = new ArrayList<>(classes.length);
        for (final Class<? extends Wrapper> wrapper : classes) {
            wrappers.add(instantatorFor(wrapper));
        }
        return wrappers;
    }

    @Test
    public void callerWrappersForEveryCall() throws ReflectiveOperationException {
        // when
        final CallIniter caller = new CallIniter(ENDPOINT_CALL, wrappers(TestWrapperWithState.class));

        // when
        caller.call(request);
        caller.call(request);

        // then
        verify(headers, never()).put("before 2", "");
        verify(headers, times(2)).put("before 1", "");
    }

    @Test
    public void callerCallsMethodDirectly() throws ReflectiveOperationException {
        // when
        final CallIniter caller = new CallIniter(ENDPOINT_CALL, wrappers());

        // when
        caller.call(request);

        // then
        verify(headers).put("call", "");
    }
}
