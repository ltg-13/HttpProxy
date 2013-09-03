package de.lusiardi.proxy.data;

import de.lusiardi.proxy.parsers.HttpResponseParser;
import de.lusiardi.proxy.writers.HttpResponseWriter;

/**
 * A class to represent a HTTP response as defined in <a
 * href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec6.html#sec6">RFC
 * 2616</a>. Can be parsed with {@link HttpResponseParser} and written back with
 * {@link HttpResponseWriter}.
 *
 * @author Joachim Lusiardi
 */
public class HttpResponse {

    private HttpVersion version;
    private int statusCode;
    private String reason;
    private HttpHeaders headers = new HttpHeaders();
    private HttpBodyChunks chunks = new HttpBodyChunks();
    private byte[] body;

    /**
     * Sets the version of the response.
     *
     * @param version the version
     */
    public void setVersion(HttpVersion version) {
        this.version = version;
    }

    /**
     * Returns the version of the response.
     *
     * @return the version
     */
    public HttpVersion getVersion() {
        return version;
    }

    /**
     * Returns the status code of the response.
     *
     * @return the status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Returns the reason for the status code.
     *
     * @return the reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * Sets the status code of the response.
     *
     * @param statusCode the status code
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Sets the reason for the status code of the response.
     *
     * @param reason the reason.
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Sets the headers for the response.
     *
     * @return the headers
     */
    public HttpHeaders getHeaders() {
        return headers;
    }

    /**
     * Returns the chunks of the response.
     *
     * @return the chunks
     */
    public HttpBodyChunks getChunks() {
        return chunks;
    }

    /**
     * Sets the chunks of the response.
     *
     * @param chunks the chunks
     */
    public void setChunks(HttpBodyChunks chunks) {
        this.chunks = chunks;
    }

    /**
     * Returns the body of the respnse.
     *
     * @return the body
     */
    public byte[] getBody() {
        return body;
    }

    /**
     * Sets the body of the response.
     *
     * @param body the body
     */
    public void setBody(byte[] body) {
        this.body = body;
    }
}
