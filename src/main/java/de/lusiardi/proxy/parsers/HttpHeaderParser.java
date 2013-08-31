package de.lusiardi.proxy.parsers;

import de.lusiardi.proxy.data.HttpHeader;
import de.lusiardi.proxy.exceptions.HeaderParseException;
import org.apache.log4j.Logger;

/**
 * Parser for HTTP Header lines as defined in <a
 * href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec4.html#sec4.2">RFC
 * 2616</a>
 *
 * @author Joachim Lusiardi
 */
public class HttpHeaderParser {

    private static Logger logger = Logger.getLogger(HttpHeaderParser.class);

    /**
     * Tries to parse a given line into a HTTP header. The form follows <a
     * href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec4.html#sec4.2">RFC
     * 2616</a>. Results will be stored in {@link HttpHeader}.
     *
     * @param line the input line
     * @return a filled HTTP header
     * @throws HeaderParseException thrown if the line does not contain a valid
     * header.
     */
    public HttpHeader parse(String line) throws HeaderParseException {
        // there must be a colon in the line to be a valid header. Also at least 
        // one char must be before the colon
        if (line.indexOf(":") <= 0) {
            logger.error("'" + line + "' was no header definition.");
            throw new HeaderParseException("'" + line + "' is no valid header definition");
        }
        String[] parts = line.split(":", 2);
        if (parts.length == 2) {
            HttpHeader header = new HttpHeader(parts[0].trim(), parts[1].trim());
            return header;
        } else {
            HttpHeader header = new HttpHeader(parts[0].trim(), "");
            return header;
        }
    }
}
