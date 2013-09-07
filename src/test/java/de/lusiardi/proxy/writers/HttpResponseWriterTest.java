package de.lusiardi.proxy.writers;

import de.lusiardi.proxy.data.HttpBodyChunk;
import de.lusiardi.proxy.data.HttpHeader;
import de.lusiardi.proxy.data.HttpResponse;
import de.lusiardi.proxy.data.HttpVersion;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;

/**
 *
 * @author Joachim Lusiardi
 */
public class HttpResponseWriterTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void test_writeString_empty_response() {
        exception.expect(IllegalArgumentException.class);

        HttpResponseWriter writer = new HttpResponseWriter();
        writer.writeToString(null, "");
    }

    @Test
    public void test_writeStream_empty_response() throws IOException {
        exception.expect(IllegalArgumentException.class);

        HttpResponseWriter writer = new HttpResponseWriter();
        writer.writeToStream(null, new ByteArrayOutputStream());
    }

    @Test
    public void test_writeString_missing_reason() {
        exception.expect(IllegalArgumentException.class);

        HttpResponseWriter writer = new HttpResponseWriter();
        HttpResponse response = new HttpResponse();
        writer.writeToString(response, "");
    }

    @Test
    public void test_writeStream_missing_reason() throws IOException {
        exception.expect(IllegalArgumentException.class);

        HttpResponseWriter writer = new HttpResponseWriter();
        HttpResponse response = new HttpResponse();
        writer.writeToStream(response, new ByteArrayOutputStream());
    }

    @Test
    public void test_writeString_missing_version() {
        exception.expect(IllegalArgumentException.class);

        HttpResponseWriter writer = new HttpResponseWriter();
        HttpResponse response = new HttpResponse();
        response.setReason("OK");
        response.setStatusCode(200);
        writer.writeToString(response, "");
    }

    @Test
    public void test_writeStream_missing_version() throws IOException {
        exception.expect(IllegalArgumentException.class);

        HttpResponseWriter writer = new HttpResponseWriter();
        HttpResponse response = new HttpResponse();
        response.setReason("OK");
        response.setStatusCode(200);
        writer.writeToStream(response, new ByteArrayOutputStream());
    }

    @Test
    public void test_writeString_status_line_only() {
        HttpResponseWriter writer = new HttpResponseWriter();
        HttpResponse response = new HttpResponse();
        response.setReason("OK");
        response.setVersion(new HttpVersion(1, 0));
        response.setStatusCode(200);
        String result = writer.writeToString(response, "");
        assertEquals("HTTP/1.0 200 OK\r\n\r\n", result);
    }

    @Test
    public void test_writeStream_status_line_only() throws IOException {
        HttpResponseWriter writer = new HttpResponseWriter();
        HttpResponse response = new HttpResponse();
        response.setReason("OK");
        response.setVersion(new HttpVersion(1, 0));
        response.setStatusCode(200);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        writer.writeToStream(response, baos);
        assertEquals("HTTP/1.0 200 OK\r\n\r\n", baos.toString());
    }

    @Test
    public void test_writeString_status_line_headers() {
        HttpResponseWriter writer = new HttpResponseWriter();
        HttpResponse response = new HttpResponse();
        response.setReason("OK");
        response.setVersion(new HttpVersion(1, 0));
        response.setStatusCode(200);
        response.getHeaders().add(new HttpHeader("name", "val"));
        String result = writer.writeToString(response, "");
        assertEquals("HTTP/1.0 200 OK\r\nname: val\r\n\r\n", result);
    }

    @Test
    public void test_writeStream_status_line_headers() throws IOException {
        HttpResponseWriter writer = new HttpResponseWriter();
        HttpResponse response = new HttpResponse();
        response.setReason("OK");
        response.setVersion(new HttpVersion(1, 0));
        response.setStatusCode(200);
        response.getHeaders().add(new HttpHeader("name", "val"));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        writer.writeToStream(response, baos);
        assertEquals("HTTP/1.0 200 OK\r\nname: val\r\n\r\n", baos.toString());
    }

    @Test
    public void test_writeString_status_line_header_chunks() {
        HttpResponseWriter writer = new HttpResponseWriter();
        HttpResponse response = new HttpResponse();
        response.setReason("OK");
        response.setVersion(new HttpVersion(1, 0));
        response.setStatusCode(200);
        response.getHeaders().add(new HttpHeader("Transfer-Encoding", "chunked"));
        response.getChunks().add(new HttpBodyChunk(4, "data".getBytes()));
        response.getChunks().add(new HttpBodyChunk(0, null));
        String result = writer.writeToString(response, "");
        System.out.println(result);
        assertEquals("HTTP/1.0 200 OK\r\nTransfer-Encoding: chunked\r\n\r\n4\r\ndata\r\n0\r\n", result);
    }

    @Test
    public void test_writeStream_status_line_header_chunks() throws IOException {
        HttpResponseWriter writer = new HttpResponseWriter();
        HttpResponse response = new HttpResponse();
        response.setReason("OK");
        response.setVersion(new HttpVersion(1, 0));
        response.setStatusCode(200);
        response.getHeaders().add(new HttpHeader("Transfer-Encoding", "chunked"));
        response.getChunks().add(new HttpBodyChunk(4, "data".getBytes()));
        response.getChunks().add(new HttpBodyChunk(0, null));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        writer.writeToStream(response, baos);
        assertEquals("HTTP/1.0 200 OK\r\nTransfer-Encoding: chunked\r\n\r\n4\r\ndata\r\n0\r\n", baos.toString());
    }

    @Test
    public void test_writeString_status_line_body() {
        HttpResponseWriter writer = new HttpResponseWriter();
        HttpResponse response = new HttpResponse();
        response.setReason("OK");
        response.setVersion(new HttpVersion(1, 0));
        response.setStatusCode(200);
        response.setBody("0123456789ABCDEF".getBytes());
        String result = writer.writeToString(response, "");
        assertEquals("HTTP/1.0 200 OK\r\n\r\n00000000  30 31 32 33 34 35 36 37  38 39 41 42 43 44 45 46  |0123456789ABCDEF\n", result);
    }

    @Test
    public void test_writeStream_status_line_body() throws IOException {
        HttpResponseWriter writer = new HttpResponseWriter();
        HttpResponse response = new HttpResponse();
        response.setReason("OK");
        response.setVersion(new HttpVersion(1, 0));
        response.setStatusCode(200);
        response.setBody("0123456789ABCDEF".getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        writer.writeToStream(response, baos);
        System.err.println(baos.toString());
        assertEquals("HTTP/1.0 200 OK\r\n\r\n0123456789ABCDEF", baos.toString());
    }
}
