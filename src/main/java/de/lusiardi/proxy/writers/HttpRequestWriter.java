package de.lusiardi.proxy.writers;

import de.lusiardi.proxy.data.HttpHeader;
import de.lusiardi.proxy.data.HttpRequest;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Class to write out HTTP requests as defined in <a
 * href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec6.html#sec6">RFC
 * 2616</a>.
 *
 * @author Joachim Lusiardi
 */
public class HttpRequestWriter {

    private HttpVersionWriter versionWriter = new HttpVersionWriter();

    /**
     * Writes the given request to the given stream. This uses {@link #writeToString(de.lusiardi.proxy.data.HttpRequest, java.lang.String)
     * } to write out the request.
     *
     * @param response the response to write
     * @param outputStream to target stream to write to
     * @throws IOException on any write error
     */
    public void writeToStream(HttpRequest request, OutputStream outputStream) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));

        String tmp = writeToString(request, "");
        bufferedWriter.write(tmp);

        bufferedWriter.flush();
    }

    /**
     * Writes the request to a string with a given optional indentation. Throws
     * {@link IllegalArgumentException} if the request is missing required
     * fields.
     *
     * @param response the response to write
     * @param indentation the indentation to prefix every line
     * @return the string containing the response
     */
    public String writeToString(HttpRequest request, String indentation) {
        if (request == null) {
            throw new IllegalArgumentException("the request must not be null");
        }
        if (request.getMethod() == null) {
            throw new IllegalArgumentException("the request method must not be null");
        }
        if (request.getRequestURI() == null) {
            throw new IllegalArgumentException("the request URI must not be null");
        }
        String result = indentation + request.getMethod() + " " + request.getRequestURI() + " " + versionWriter.write(request.getVersion()) + "\r\n";
        HttpHeaderWriter headerWriter = new HttpHeaderWriter();

        for (HttpHeader header : request.getHeaders()) {
            result += indentation + headerWriter.write(header);
        }

        result += indentation + "\r\n";

        // render body
        if (request.getBody() != null) {
            result += indentation + new String(request.getBody());
        }

        return result;
    }
}
