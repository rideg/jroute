package org.jroute.route;

import static java.util.Arrays.asList;
import static org.jroute.http.HttpMethod.GET;
import static org.jroute.http.response.Responses.RESPONSE_404;
import static org.jroute.http.response.Responses.RESPONSE_500;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jroute.http.request.Request;
import org.jroute.http.response.Response;
import org.junit.Before;
import org.junit.Test;

public class RouterTest {

    final Map<String, List<RequestHandler>> chains = new HashMap<>();
    private Router router;

    @Before
    public void setUp() throws Exception {
        router = new Router(chains);
    }

    @Test
    public void handleCallsHandleIfHandlerCanHandleRequest() throws ReflectiveOperationException {
        // given
        final RequestHandler handler = mock(RequestHandler.class);
        doReturn(true).when(handler).canHandle(any(Request.class));
        chains.put("GET", asList(handler));

        // when
        final Request request = new Request(GET, "/any");
        router.route(request);

        // then
        verify(handler).handle(request);
    }

    @Test
    public void handleGoesThroughHandlerToSatisfyRequest() throws Exception {
        // given
        final RequestHandler handler = mock(RequestHandler.class);
        doReturn(false).doReturn(true).when(handler).canHandle(any(Request.class));
        chains.put("GET", asList(handler, handler));

        // when
        final Request request = new Request(GET, "/any");
        router.route(request);

        // then
        verify(handler, times(2)).canHandle(request);
        verify(handler).handle(request);
    }

    @Test
    public void handlerSearchForHandlerTheProperChains() throws Exception {
        // given
        final RequestHandler handler = mock(RequestHandler.class);
        chains.put("POST", asList(handler));

        router.route(new Request(GET, "/any"));

        // then
        verifyZeroInteractions(handler);
    }

    @Test
    public void handlerReturns404IfCannotFindProperHandler() throws Exception {
        // given
        final RequestHandler handler = mock(RequestHandler.class);
        chains.put("POST", asList(handler));

        // when
        final Response response = router.route(new Request(GET, "/any"));

        // then
        assertSame(RESPONSE_404, response);
    }

    @Test
    public void handlerReturns500IfThrowableThrown() throws Exception {
        // given
        final RequestHandler handler = mock(RequestHandler.class);
        chains.put("GET", asList(handler));

        // and
        doReturn(true).when(handler).canHandle(any(Request.class));
        doThrow(new RuntimeException("fake")).when(handler).handle(any(Request.class));

        // when
        final Response response = router.route(new Request(GET, "/any"));

        // then
        assertSame(RESPONSE_500, response);
    }
}
