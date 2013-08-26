package de.lusiardi.soa.proxy.parsers;

import de.lusiardi.soa.proxy.data.HttpBodyChunk;
import de.lusiardi.soa.proxy.data.HttpHeader;
import de.lusiardi.soa.proxy.data.HttpResponse;
import de.lusiardi.soa.proxy.exceptions.HeaderParseException;
import de.lusiardi.soa.proxy.exceptions.ResponseParseException;
import de.lusiardi.soa.proxy.exceptions.VersionParseException;
import de.lusiardi.soa.proxy.stream.HttpInputStream;
import de.lusiardi.soa.proxy.writers.HexDump;
import java.io.IOException;
import java.io.InputStream;
import org.apache.log4j.Logger;

/**
 *
 * @author shing19m
 */
public class HttpResponseParser {

    private HttpHeaderParser headerParser = new HttpHeaderParser();
    private HttpVersionParser versionParser = new HttpVersionParser();
    private HttpBodyChunkParser bodyChunkParser = new HttpBodyChunkParser();
    private HexDump hexDump = new HexDump();
    private static Logger logger = Logger.getLogger(HttpResponseParser.class);

    public HttpResponse parse(InputStream input) throws IOException, ResponseParseException, VersionParseException, HeaderParseException {
        HttpInputStream bufferedReader = new HttpInputStream(input);
        HttpResponse result = new HttpResponse();

        parseStatusLine(bufferedReader.readLine(), result);

        String line = bufferedReader.readLine();
        while (!line.isEmpty()) {
            HttpHeader header = headerParser.parse(line);
            result.getHeaders().add(header);

            line = bufferedReader.readLine();
        }

        Integer contentLength = result.getHeaders().getContentLength();
        if (contentLength != null) {
            logger.debug("Content-Length: '" + contentLength + "'");
            byte[] content = new byte[contentLength];
            bufferedReader.readBytes(content, 0, contentLength);
            result.setBody(content);
            logger.debug("Content: " + hexDump.write(content, "      >"));
        } else if (result.getHeaders().hasChunkedTransferEncoding()) {
            parseChunkedBody(bufferedReader, result);
        }

        return result;
    }

    private void parseStatusLine(String requestLine, HttpResponse target) throws ResponseParseException, VersionParseException {
        String[] parts = requestLine.split(" ", 3);
        if (parts.length != 3) {
            throw new ResponseParseException("The status line is malformed '" + requestLine + "'");
        }
        versionParser = new HttpVersionParser();

        target.setVersion(versionParser.parse(parts[0]));

        target.setStatusCode(Integer.parseInt(parts[1]));

        target.setReason(parts[2]);
    }

    /**
     * http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.6.1
     *
     * @param bufferedReader
     * @param result
     */
    private void parseChunkedBody(HttpInputStream bufferedReader, HttpResponse result) throws IOException {
        HttpBodyChunk parsed = null;
        do {
            parsed = bodyChunkParser.parse(bufferedReader);
            result.getChunks().add(parsed);
        } while (parsed.getSize() != 0);
    }
}
