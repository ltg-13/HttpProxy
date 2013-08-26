package de.lusiardi.soa.proxy.data;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import de.lusiardi.soa.proxy.exceptions.HeaderParseException;
import de.lusiardi.soa.proxy.parsers.HttpHostParser;
import java.util.Collection;
import java.util.Iterator;

/**
 * Class to handle all different headers in a HTTP message.
 *
 * @author shing19m
 */
public class HttpHeaders implements Iterable<HttpHeader> {

    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String TRANSFER_ENCODING = "Transfer-Encoding";
    public static final String TRANSFER_ENCODING_CHUNKED = "chunked";
    public static final String HOST = "Host";
    private Multimap<String, HttpHeader> headers = ArrayListMultimap.create();
    HttpHostParser hostParser = new HttpHostParser();

    /**
     * Adds a header. The header must not be {@code null}.
     *
     * @param header the header to add.
     */
    public void add(HttpHeader header) {
        if (header == null) {
            throw new IllegalArgumentException("the header to add must not be null");
        }
        headers.put(header.getName(), header);
    }

    /**
     * Returns an iterator over the headers.
     *
     * @return an Iterator.
     */
    public Iterator<HttpHeader> iterator() {
        return headers.values().iterator();
    }

    /**
     * Checks if a header for transfer encoding is set to chunked.
     *
     * @return {@code true} if the header is set to chunked, {@code false}
     * otherwise.
     */
    public boolean hasChunkedTransferEncoding() {
        Collection<HttpHeader> transferEncodings = headers.get(TRANSFER_ENCODING);

        for (HttpHeader transferEncoding : transferEncodings) {
            if (transferEncoding.getValue().equals(TRANSFER_ENCODING_CHUNKED)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if a header for content length is set and return its value.
     *
     * @return {@code null} if no header is set or its value as number
     * @throws HeaderParseException is thrown if the header is set to a non
     * integer value
     */
    public Integer getContentLength() throws HeaderParseException {
        Collection<HttpHeader> contentLengths = headers.get(CONTENT_LENGTH);
        if (contentLengths.size() < 1) {
            return null;
        }
        HttpHeader contentLength = contentLengths.toArray(new HttpHeader[contentLengths.size()])[0];
        try {
            return Integer.parseInt(contentLength.getValue());
        } catch (NumberFormatException ex) {
            throw new HeaderParseException("'" + contentLength.getValue() + "' is no valid content length");
        }
    }

    /**
     * Extracts the value of the Host header if it exists.
     *
     * @return the host and the port
     * @throws HeaderParseException
     */
    public HttpHost getHost() throws HeaderParseException {
        Collection<HttpHeader> hostHeaders = headers.get(HOST);
        if (hostHeaders.size() < 1) {
            return null;
        }
        HttpHeader hostHeader = hostHeaders.toArray(new HttpHeader[hostHeaders.size()])[0];
        return hostParser.parse(hostHeader.getValue());
    }
}
