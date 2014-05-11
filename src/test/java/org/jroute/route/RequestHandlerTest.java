
package org.jroute.route;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import org.jroute.http.request.Request;
import org.jroute.http.response.Response;
import org.jroute.route.endpoint.CallIniter;
import org.junit.Test;

public class RequestHandlerTest {

    @Test
    public void canHandleForwardsToCanHandle() {
        // given
        final UriHandler handler = mock(UriHandler.class);
        doReturn(true).when(handler).tryHandle(any(Request.class));
        final RequestHandler req = new RequestHandler(handler, null);

        // when & then
        assertThat(req.canHandle(mock(Request.class)), is(true));
    }

    @Test
    public void canHandleForwardsToCall() throws ReflectiveOperationException {
        // given
        final Response response = new Response();

        final CallIniter initer = mock(CallIniter.class);
        doReturn(response).when(initer).call(any(Request.class));

        final RequestHandler req = new RequestHandler(null, initer);

        // when & then
        assertSame(response, req.handle(mock(Request.class)));
    }

}
