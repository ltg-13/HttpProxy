package de.lusiardi.proxy.parsers;

import de.lusiardi.proxy.data.HttpHost;
import de.lusiardi.proxy.exceptions.HeaderParseException;

/**
 *
 * @author Joachim Lusiardi
 */
public class HttpHostParser {

    public HttpHost parse(String in) throws HeaderParseException {
        try {
            String parts[] = in.split(":");
            if (parts.length == 1 && !parts[0].isEmpty()) {
                return new HttpHost(parts[0]);
            } else if (parts.length == 2) {
                return new HttpHost(parts[0], Integer.parseInt(parts[1]));
            }
            throw new HeaderParseException("'" + in + "' is no valid host");
        } catch (Exception ex) {
            throw new HeaderParseException("'" + in + "' is no valid host");
        }
    }
}
