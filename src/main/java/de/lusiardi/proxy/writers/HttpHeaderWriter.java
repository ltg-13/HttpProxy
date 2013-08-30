package de.lusiardi.proxy.writers;

import de.lusiardi.proxy.data.HttpHeader;

/**
 *
 * @author shing19m
 */
public class HttpHeaderWriter {

    public String write(HttpHeader header) {
        if (header == null) {
            throw new IllegalArgumentException("header must not be 'null'");
        }
        return header.getName() + ": " + header.getValue() + "\r\n";
    }
}
