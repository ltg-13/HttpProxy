package de.lusiardi.proxy.writers;

import de.lusiardi.proxy.data.HttpBodyChunk;
import de.lusiardi.proxy.data.HttpHeader;
import de.lusiardi.proxy.data.HttpResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 *
 * @author shing19m
 */
public class HttpResponseWriter {

    private HttpVersionWriter versionWriter = new HttpVersionWriter();
    private HttpHeaderWriter headerWriter = new HttpHeaderWriter();
    private HttpBodyChunkWriter bodyChunkWriter = new HttpBodyChunkWriter();
    private HexDump hexDump = new HexDump();

    public void writeToStream(HttpResponse response, OutputStream outputStream) throws IOException {
        OutputStreamWriter bufferedWriter = new OutputStreamWriter(outputStream);

        write(bufferedWriter, writeResultLine(response, ""));
        write(bufferedWriter, writeHeaders(response, ""));
        write(bufferedWriter, "\r\n");

        if (response.getHeaders().hasChunkedTransferEncoding()) {
            write(bufferedWriter, writeChunks(response, ""));
        } else {
            byte[] body = response.getBody();
            if (body != null) {
                outputStream.write(body, 0, body.length);
            }
        }
        bufferedWriter.flush();
    }

    void write(OutputStreamWriter bw, String out) throws IOException {
        bw.write(out, 0, out.length());
    }

    String writeResultLine(HttpResponse response, String indentation) {
        return indentation + versionWriter.write(response.getVersion()) + " " + response.getStatusCode() + " " + response.getReason() + "\n";
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

    public String writeToString(HttpResponse response, String indentation) {

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
