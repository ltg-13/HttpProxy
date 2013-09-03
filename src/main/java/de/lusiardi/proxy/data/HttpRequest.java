package de.lusiardi.proxy.data;

import de.lusiardi.proxy.parsers.HttpRequestParser;
import de.lusiardi.proxy.writers.HttpRequestWriter;

/**
 * A class to represent a HTTP request as defined in <a
 * href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec5.html#sec5">RFC
 * 2616</a>. Can be parsed with {@link HttpRequestParser} and written back with
 * {@link HttpRequestWriter}.
 *
 * @author Joachim Lusiardi
 */
public class HttpRequest {

    private byte[] body;
    private String requestURI;
    private String method;
    private HttpVersion version;
    private HttpHeaders headers = new HttpHeaders();

    /**
     * Returns the body data of the request.
     *
     * @return the body data
     */
    public byte[] getBody() {
        return body;
    }

    /**
     * Sets the body data of the request.
     *
     * @param body the body data
     */
    public void setBody(byte[] body) {
        this.body = body;
    }

    /**
     * Returns the request's URI.
     *
     * @return the uri
     */
    public String getRequestURI() {
        return requestURI;
    }

    /**
     * Sets the request's URI.
     *
     * @param requestURI the uri
     */
    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    /**
     * Returns the method information.
     *
     * @return the method
     */
    public String getMethod() {
        return method;
    }

    /**
     * Sets the method information.
     *
     * @param method the method
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * Returns the HTTP version of this request.
     *
     * @return the version
     */
    public HttpVersion getVersion() {
        return version;
    }

    /**
     * Sets the HTTP version of this request.
     *
     * @param version the version
     */
    public void setVersion(HttpVersion version) {
        this.version = version;
    }

    /**
     * Returns the headers from his request.
     *
     * @return the headers
     */
    public HttpHeaders getHeaders() {
        return headers;
    }

    /**
     * Sets the headers for this request.
     *
     * @param headers the headers
     */
    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }
}
