package de.lusiardi.proxy.writers;

import de.lusiardi.proxy.data.HttpBodyChunk;
import de.lusiardi.proxy.data.HttpHeader;
import de.lusiardi.proxy.data.HttpResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Class to write out HTTP Responses as defined in <a
 * href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec6.html#sec6">RFC
 * 2616</a>.
 *
 * @author Joachim Lusiardi
 */
public class HttpResponseWriter {

    private HttpVersionWriter versionWriter = new HttpVersionWriter();

    private HttpHeaderWriter headerWriter = new HttpHeaderWriter();

    private HttpBodyChunkWriter bodyChunkWriter = new HttpBodyChunkWriter();

    private HexDump hexDump = new HexDump();

    /**
     * Writes the given response to the given stream.
     *
     * @param response the response to write
     * @param outputStream to target stream to write to
     * @throws IOException on any write error
     */
    public void writeToStream(HttpResponse response, OutputStream outputStream) throws IOException {
        if (response == null) {
            throw new IllegalArgumentException("the response must not be null");
        }
        if (response.getReason() == null) {
            throw new IllegalArgumentException("the request reason must not be null");
        }
        if (response.getVersion() == null) {
            throw new IllegalArgumentException("the request version must not be null");
        }
        OutputStreamWriter bufferedWriter = new OutputStreamWriter(outputStream);

        write(bufferedWriter, writeResultLine(response, ""));
        write(bufferedWriter, writeHeaders(response, ""));
        write(bufferedWriter, "\r\n");

        if (response.getHeaders().hasChunkedTransferEncoding()) {
            write(bufferedWriter, writeChunks(response, ""));
        } else {
            byte[] body = response.getBody();
            if (body != null) {
                bufferedWriter.flush();
                outputStream.write(body, 0, body.length);
            }
        }
        bufferedWriter.flush();
    }

    private void write(OutputStreamWriter bw, String out) throws IOException {
        bw.write(out, 0, out.length());
    }

    String writeResultLine(HttpResponse response, String indentation) {
        return indentation + versionWriter.write(response.getVersion()) + " " + response.getStatusCode() + " " + response.getReason() + "\r\n";
    }

    String writeHeaders(HttpResponse response, String indentation) {
        String result = "";
        for (HttpHeader header : response.getHeaders()) {
            result += indentation + headerWriter.write(header);
        }
        return result;
    }

    String writeChunks(HttpResponse response, String indentation) {
        String result = "";
        for (HttpBodyChunk chunk : response.getChunks()) {
            result += indentation + bodyChunkWriter.write(chunk);
        }
        return result;
    }

    /**
     * Writes the response to a string with a given optional indentation.
     *
     * @param response the response to write
     * @param indentation the indentation to prefix every line
     * @return the string containing the response
     */
    public String writeToString(HttpResponse response, String indentation) {
        if (response == null) {
            throw new IllegalArgumentException("the response must not be null");
        }
        if (response.getReason() == null) {
            throw new IllegalArgumentException("the request reason must not be null");
        }
        if (response.getVersion() == null) {
            throw new IllegalArgumentException("the request version must not be null");
        }

        // render the request line
        String result = writeResultLine(response, indentation);

        // render the headers
        result += writeHeaders(response, indentation);

        // render the single newline
        result += indentation + "\r\n";

        if (response.getHeaders().hasChunkedTransferEncoding()) {
            result += writeChunks(response, indentation);
        } else {
            byte[] body = response.getBody();
            if (body != null) {
                result += hexDump.write(body, indentation);
            }
        }
        return result;
    }
}
