package de.lusiardi.proxy.writers;

import de.lusiardi.proxy.data.HttpHeader;

/**
 * Writer to write HTTP headers as defined <a
 * href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec4.html#sec4.2">RFC
 * 2616</a>.
 *
 * @author Joachim Lusiardi
 */
public class HttpHeaderWriter {

    /**
     * Write a header into a string.
     *
     * @param chunk the header to write.
     * @return the string that contains the representation of the header
     */
    public String write(HttpHeader header) {
        if (header == null) {
            throw new IllegalArgumentException("header must not be 'null'");
        }
        return header.getName() + ": " + header.getValue() + "\r\n";
    }
}
