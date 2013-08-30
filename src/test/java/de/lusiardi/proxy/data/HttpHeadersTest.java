package de.lusiardi.proxy.data;

import de.lusiardi.proxy.data.HttpHeaders;
import de.lusiardi.proxy.data.HttpHeader;
import de.lusiardi.proxy.exceptions.HeaderParseException;
import org.hamcrest.core.IsEqual;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 *
 * @author shing19m
 */
public class HttpHeadersTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void test_addHeader_1() {
        exception.expect(IllegalArgumentException.class);

        HttpHeaders headers = new HttpHeaders();
        headers.add(null);
    }

    @Test
    public void test_addHeader_2() {

        HttpHeaders headers = new HttpHeaders();
        headers.add(new HttpHeader(null, null));
    }

    @Test
    public void test_hasChunkedTransferEncoding_false_1() {
        HttpHeaders headers = new HttpHeaders();
        assertFalse(headers.hasChunkedTransferEncoding());
    }

    @Test
    public void test_hasChunkedTransferEncoding_false_2() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(new HttpHeader("foo", "bar"));
        assertFalse(headers.hasChunkedTransferEncoding());
    }

    @Test
    public void test_hasChunkedTransferEncoding_false_3() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(new HttpHeader(HttpHeaders.TRANSFER_ENCODING, "bar1"));
        headers.add(new HttpHeader(HttpHeaders.TRANSFER_ENCODING, "bar2"));
        assertFalse(headers.hasChunkedTransferEncoding());
    }

    @Test
    public void test_hasChunkedTransferEncoding_true_1() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(new HttpHeader(HttpHeaders.TRANSFER_ENCODING, HttpHeaders.TRANSFER_ENCODING_CHUNKED));
        assertTrue(headers.hasChunkedTransferEncoding());
    }

    @Test
    public void test_hasChunkedTransferEncoding_true_2() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(new HttpHeader(HttpHeaders.TRANSFER_ENCODING, "something else"));
        headers.add(new HttpHeader(HttpHeaders.TRANSFER_ENCODING, HttpHeaders.TRANSFER_ENCODING_CHUNKED));
        assertTrue(headers.hasChunkedTransferEncoding());
    }

    @Test
    public void test_getContentLength_unset() throws HeaderParseException {
        HttpHeaders headers = new HttpHeaders();
        assertNull(headers.getContentLength());
    }

    @Test
    public void test_getContentLength_set_bar() throws HeaderParseException {
        exception.expect(HeaderParseException.class);

        HttpHeaders headers = new HttpHeaders();
        headers.add(new HttpHeader(HttpHeaders.CONTENT_LENGTH, "bar"));
        headers.getContentLength();
    }

    @Test
    public void test_getContentLength_set_100() throws HeaderParseException {
        HttpHeaders headers = new HttpHeaders();
        headers.add(new HttpHeader(HttpHeaders.CONTENT_LENGTH, "100"));
        assertThat(100, IsEqual.equalTo(headers.getContentLength()));
    }

    @Test
    public void test_iterator_1() {
        HttpHeaders headers = new HttpHeaders();
        assertFalse(headers.iterator().hasNext());
    }

    @Test
    public void test_iterator_2() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(new HttpHeader("foo", "bar"));
        assertTrue(headers.iterator().hasNext());
    }

    @Test
    public void test_getHost_unset() throws HeaderParseException {
        HttpHeaders headers = new HttpHeaders();
        assertNull(headers.getHost());
    }

    @Test
    public void test_getHost_set_but_invalid_1() throws HeaderParseException {
        exception.expect(HeaderParseException.class);

        HttpHeaders headers = new HttpHeaders();
        headers.add(new HttpHeader(HttpHeaders.HOST, ""));
        headers.getHost();
    }

    @Test
    public void test_getHost_set_but_invalid_2() throws HeaderParseException {
        exception.expect(HeaderParseException.class);

        HttpHeaders headers = new HttpHeaders();
        headers.add(new HttpHeader(HttpHeaders.HOST, "foo:bar"));
        headers.getHost();
    }

    @Test
    public void test_getHost_set_no_port() throws HeaderParseException {
        HttpHeaders headers = new HttpHeaders();
        headers.add(new HttpHeader(HttpHeaders.HOST, "bar"));
        assertEquals("bar", headers.getHost().getHost());
        assertEquals(80, headers.getHost().getPort());
    }

    @Test
    public void test_getHost_set() throws HeaderParseException {
        HttpHeaders headers = new HttpHeaders();
        headers.add(new HttpHeader(HttpHeaders.HOST, "bar:8080"));
        assertEquals("bar", headers.getHost().getHost());
        assertEquals(8080, headers.getHost().getPort());
    }
}
