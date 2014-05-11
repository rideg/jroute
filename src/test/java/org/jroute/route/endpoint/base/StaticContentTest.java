package org.jroute.route.endpoint.base;

import static java.nio.file.Files.write;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.jroute.http.HttpMethod;
import org.jroute.http.HttpStatusCode;
import org.jroute.http.request.Request;
import org.jroute.http.response.Response;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.net.HttpHeaders;

public class StaticContentTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();
    private Static handler;

    @Test
    public void callReturnsTheRequestedData() throws Exception {
        // given
        final String data = writeFile();

        handler = new Static(tmp.getRoot().getAbsolutePath() + "/test.html");
        handler.setRequest(new Request(HttpMethod.GET, ""));
        // when
        handler.call();

        // then
        final Response response = handler.getResponse();

        final Map<String, String> headers = response.getHeader();
        assertThat(response.getStatus(), is(HttpStatusCode.OK));
        assertThat(headers.get(HttpHeaders.CONTENT_TYPE), is("text/html"));
        assertThat(headers.get(HttpHeaders.CONTENT_LENGTH), is("" + data.length()));
    }

    private String writeFile() throws IOException {
        final String data = "<html></html>";
        final File file = tmp.newFile("test.html");
        write(file.toPath(), data.getBytes());
        return data;
    }

}
