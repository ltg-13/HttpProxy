package de.lusiardi.soa.proxy.data;

/**
 *
 * @author shing19m
 */
public class HttpResponse {

    private HttpVersion version;
    private int statusCode;
    private String reason;
    private HttpHeaders headers = new HttpHeaders();
    private HttpBodyChunks chunks = new HttpBodyChunks();
    private byte[] body;

    public void setVersion(HttpVersion version) {
        this.version = version;
    }

    public HttpVersion getVersion() {
        return version;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getReason() {
        return reason;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public HttpBodyChunks getChunks() {
        return chunks;
    }

    public void setChunks(HttpBodyChunks chunks) {
        this.chunks = chunks;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}
