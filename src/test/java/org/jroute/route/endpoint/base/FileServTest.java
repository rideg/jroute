
package org.jroute.route.endpoint.base;

import static java.nio.file.Files.write;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.Map;

import org.jroute.http.HttpMethod;
import org.jroute.http.HttpStatusCode;
import org.jroute.http.request.Request;
import org.jroute.http.response.Response;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.net.HttpHeaders;

public class FileServTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();
    private Files handler;

    @Before
    public void setUp() throws Exception {
        handler = new Files(tmp.getRoot().getAbsolutePath());
    }

    @Test
    public void callReturnsTheRequestedData() throws Exception {
        // given
        final File file = tmp.newFile("test.txt");
        final String data = "test data in this file";
        write(file.toPath(), data.getBytes());
        handler.setRequest(new Request(HttpMethod.GET, ""));

        // when
        handler.call("test.txt");

        // then
        Response response = handler.getResponse();
        final Map<String, String> headers = response.getHeader();
        assertThat(response.getStatus(), is(HttpStatusCode.OK));
        assertThat(headers.get(HttpHeaders.CONTENT_TYPE), is("text/plain"));
        assertThat(headers.get(HttpHeaders.CONTENT_LENGTH), is("" + data.length()));
    }
}
