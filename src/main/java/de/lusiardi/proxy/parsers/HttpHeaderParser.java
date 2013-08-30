package de.lusiardi.proxy.parsers;

import de.lusiardi.proxy.data.HttpHeader;
import de.lusiardi.proxy.exceptions.HeaderParseException;
import org.apache.log4j.Logger;

/**
 *
 * @author shing19m
 */
public class HttpHeaderParser {

    private static Logger logger = Logger.getLogger(HttpHeaderParser.class);

    public HttpHeader parse(String line) throws HeaderParseException {
        String[] parts = line.split(": ", 2);
        if (parts.length != 2) {
            logger.error("'" + line + "' was no header definition.");
            throw new HeaderParseException("'" + line + "' is no valid header definition");
        } else {
            HttpHeader header = new HttpHeader(parts[0].trim(), parts[1].trim());
            return header;
        }
    }
}
