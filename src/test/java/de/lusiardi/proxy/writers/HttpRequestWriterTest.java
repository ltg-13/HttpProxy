package de.lusiardi.proxy.writers;

import de.lusiardi.proxy.data.HttpHeader;
import de.lusiardi.proxy.data.HttpRequest;
import de.lusiardi.proxy.data.HttpVersion;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;

/**
 *
 * @author shing19m
 */
public class HttpRequestWriterTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void test_writeString_empty_request() {
        exception.expect(IllegalArgumentException.class);

        HttpRequestWriter headerWriter = new HttpRequestWriter();
        headerWriter.writeToString(null, "");
    }

    @Test
    public void test_writeStream_empty_request() throws IOException {
        exception.expect(IllegalArgumentException.class);

        HttpRequestWriter headerWriter = new HttpRequestWriter();
        OutputStream os = new ByteArrayOutputStream();
        headerWriter.writeToStream(null, os);
    }

    @Test
    public void test_writeString_missing_method() {
        exception.expect(IllegalArgumentException.class);

        HttpRequestWriter headerWriter = new HttpRequestWriter();
        final HttpRequest httpRequest = new HttpRequest();
        httpRequest.setRequestURI("/");
        httpRequest.setVersion(new HttpVersion(1, 0));
        headerWriter.writeToString(httpRequest, "");
    }

    @Test
    public void test_writeString_missing_uri() {
        exception.expect(IllegalArgumentException.class);

        HttpRequestWriter headerWriter = new HttpRequestWriter();
        final HttpRequest httpRequest = new HttpRequest();
        httpRequest.setMethod("GET");
        httpRequest.setVersion(new HttpVersion(1, 0));
        headerWriter.writeToString(httpRequest, "");
    }

    @Test
    public void test_writeString_missing_version() {
        exception.expect(IllegalArgumentException.class);

        HttpRequestWriter headerWriter = new HttpRequestWriter();
        final HttpRequest httpRequest = new HttpRequest();
        httpRequest.setMethod("GET");
        httpRequest.setRequestURI("/");
        headerWriter.writeToString(httpRequest, "");
    }

    @Test
    public void test_writeString_request_line_only() {
        HttpRequestWriter headerWriter = new HttpRequestWriter();
        final HttpRequest httpRequest = new HttpRequest();
        httpRequest.setMethod("GET");
        httpRequest.setRequestURI("/");
        httpRequest.setVersion(new HttpVersion(1, 0));
        String result = headerWriter.writeToString(httpRequest, "");
        assertEquals("GET / HTTP/1.0\r\n\r\n", result);
    }

    @Test
    public void test_writeString_request_line_body() {
        HttpRequestWriter headerWriter = new HttpRequestWriter();
        final HttpRequest httpRequest = new HttpRequest();
        httpRequest.setMethod("GET");
        httpRequest.setRequestURI("/");
        httpRequest.setVersion(new HttpVersion(1, 0));
        httpRequest.setBody("body".getBytes());
        String result = headerWriter.writeToString(httpRequest, "");
        assertEquals("GET / HTTP/1.0\r\n\r\nbody", result);
    }

    @Test
    public void test_writeString_request_line_headers() {
        HttpRequestWriter headerWriter = new HttpRequestWriter();
        final HttpRequest httpRequest = new HttpRequest();
        httpRequest.setMethod("GET");
        httpRequest.setRequestURI("/");
        httpRequest.setVersion(new HttpVersion(1, 0));
        httpRequest.getHeaders().add(new HttpHeader("name1", "value1"));
        httpRequest.getHeaders().add(new HttpHeader("name2", "value2"));
        String result = headerWriter.writeToString(httpRequest, "");
        assertEquals("GET / HTTP/1.0\r\nname1: value1\r\nname2: value2\r\n\r\n", result);
    }

    @Test
    public void test_writeStream_request_line_headers() throws IOException {
        HttpRequestWriter headerWriter = new HttpRequestWriter();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        final HttpRequest httpRequest = new HttpRequest();
        httpRequest.setMethod("GET");
        httpRequest.setRequestURI("/");
        httpRequest.setVersion(new HttpVersion(1, 0));
        httpRequest.getHeaders().add(new HttpHeader("name1", "value1"));
        httpRequest.getHeaders().add(new HttpHeader("name2", "value2"));
        headerWriter.writeToStream(httpRequest, os);
        
        assertEquals("GET / HTTP/1.0\r\nname1: value1\r\nname2: value2\r\n\r\n", os.toString());
    }
}
