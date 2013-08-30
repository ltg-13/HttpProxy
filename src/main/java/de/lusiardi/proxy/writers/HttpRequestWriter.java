package de.lusiardi.proxy.writers;

import de.lusiardi.proxy.data.HttpHeader;
import de.lusiardi.proxy.data.HttpRequest;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 *
 * @author shing19m
 */
public class HttpRequestWriter {

    private HttpVersionWriter versionWriter = new HttpVersionWriter();

    public void writeToStream(HttpRequest request, OutputStream outputStream) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));

        String tmp = writeToString(request, "");
        bufferedWriter.write(tmp);

        bufferedWriter.flush();
    }

    public String writeToString(HttpRequest request, String indentation) {
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
