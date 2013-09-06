package de.lusiardi.proxy.parsers;

import de.lusiardi.proxy.data.HttpBodyChunk;
import de.lusiardi.proxy.data.HttpHeader;
import de.lusiardi.proxy.data.HttpResponse;
import de.lusiardi.proxy.exceptions.ChunkParseException;
import de.lusiardi.proxy.exceptions.HeaderParseException;
import de.lusiardi.proxy.exceptions.ResponseParseException;
import de.lusiardi.proxy.exceptions.VersionParseException;
import de.lusiardi.proxy.stream.HttpInputStream;
import de.lusiardi.proxy.writers.HexDump;
import java.io.IOException;
import java.io.InputStream;
import org.apache.log4j.Logger;

/**
 * Parser for HTTP responses as defined in <a
 * href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec6.html#sec6">RFC
 * 2616</a>.
 *
 * @author Joachim Lusiardi
 */
public class HttpResponseParser {

    private HttpHeaderParser headerParser = new HttpHeaderParser();
    private HttpVersionParser versionParser = new HttpVersionParser();
    private HttpBodyChunkParser bodyChunkParser = new HttpBodyChunkParser();
    private HexDump hexDump = new HexDump();
    private static Logger logger = Logger.getLogger(HttpResponseParser.class);

    /**
     * Parses a HTTP response as defined in <a
     * href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec6.html#sec6">RFC
     * 2616</a>.
     *
     * @param inputStream the input stream to read from
     * @return the parsed response
     * @throws ResponseParseException on any issue parsing the response
     */
    public HttpResponse parse(InputStream inputStream) throws ResponseParseException {
        try {
            HttpInputStream bufferedReader = new HttpInputStream(inputStream);
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
                bufferedReader.readBytes(content, contentLength);
                result.setBody(content);
                logger.debug("Content: " + hexDump.write(content, "      >"));
            } else if (result.getHeaders().hasChunkedTransferEncoding()) {
                parseChunkedBody(bufferedReader, result);
            }

            return result;
        } catch (IOException ex) {
            throw new ResponseParseException("problems reading the response", ex);
        } catch (HeaderParseException ex) {
            throw new ResponseParseException("could not parse all headers", ex);
        } catch (VersionParseException ex) {
            throw new ResponseParseException("could not parese the version of the response", ex);
        } catch (ChunkParseException ex) {
            throw new ResponseParseException("could not read the body correctly", ex);
        }
    }

    /**
     * Parses the status line as defined in <a
     * href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec6.html#sec6.1">RFC
     * 2616</a>.
     *
     * @param statusLine the unparsed status line
     * @param target the response object to add the parsed values to
     * @throws ResponseParseException if the status line is malformed
     * @throws VersionParseException if the version in the status line is
     * malformed
     */
    private void parseStatusLine(String statusLine, HttpResponse target) throws ResponseParseException, VersionParseException {
        String[] parts = statusLine.split(" ", 3);
        if (parts.length != 3) {
            throw new ResponseParseException("The status line is malformed '" + statusLine + "'");
        }
        versionParser = new HttpVersionParser();

        target.setVersion(versionParser.parse(parts[0]));

        target.setStatusCode(Integer.parseInt(parts[1]));

        target.setReason(parts[2]);
    }

    /**
     * Parses the body in chunks as defined in <a
     * href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.6.1">RFC
     * 2616</a>.
     *
     * @param inputStream the input stream to read from
     * @param result the response that should hold the chunks
     * @throws ChunkParseException on errors while parsing a chunk
     */
    private void parseChunkedBody(HttpInputStream inputStream, HttpResponse result) throws ChunkParseException {
        HttpBodyChunk parsed = null;
        do {
            parsed = bodyChunkParser.parse(inputStream);
            result.getChunks().add(parsed);
        } while (parsed.getSize() != 0);
    }
}
