package de.lusiardi.soa.proxy.parsers;

import de.lusiardi.soa.proxy.data.HttpHeader;
import de.lusiardi.soa.proxy.data.HttpRequest;
import de.lusiardi.soa.proxy.exceptions.HeaderParseException;
import de.lusiardi.soa.proxy.exceptions.RequestParseException;
import de.lusiardi.soa.proxy.exceptions.VersionParseException;
import de.lusiardi.soa.proxy.stream.HttpInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 *
 * @author shing19m
 */
public class HttpRequestParser {

    private static Logger logger = Logger.getLogger(HttpRequestParser.class);
    private static final Set<String> standardHttpMethods = new HashSet<String>();
    private HttpHeaderParser headerParser = new HttpHeaderParser();
    private HttpVersionParser versionParser = new HttpVersionParser();

    static {
        standardHttpMethods.add("OPTIONS");
        standardHttpMethods.add("GET");
        standardHttpMethods.add("HEAD");
        standardHttpMethods.add("POST");
        standardHttpMethods.add("PUT");
        standardHttpMethods.add("DELETE");
        standardHttpMethods.add("TRACE");
        standardHttpMethods.add("CONNECT");
    }

    public HttpRequest parse(InputStream input) throws IOException, RequestParseException, VersionParseException, HeaderParseException {
        HttpInputStream bufferedReader = new HttpInputStream(input);
        HttpRequest result = new HttpRequest();

        parseRequestLine(bufferedReader.readNonEmptyLine(), result);

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
            logger.debug("Content: '" + new String(content) + "'");
        }

        return result;
    }

    private void parseRequestLine(String requestLine, HttpRequest target) throws RequestParseException, VersionParseException {
        String[] parts = requestLine.split(" ");
        if (parts.length != 3) {
            throw new RequestParseException("The request line is malformed '" + requestLine + "'");
        }

        if (!standardHttpMethods.contains(parts[0])) {
            logger.warn("Setting non standard HTTP method '" + parts[0] + "' on request");
        }
        target.setMethod(parts[0]);
        target.setRequestURI(parts[1]);
        versionParser = (new HttpVersionParser());

        target.setVersion(versionParser.parse(parts[2]));
    }
}
