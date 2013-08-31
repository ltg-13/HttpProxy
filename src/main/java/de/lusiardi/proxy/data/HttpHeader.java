package de.lusiardi.proxy.data;

import de.lusiardi.proxy.parsers.HttpHeaderParser;
import de.lusiardi.proxy.writers.HttpHeaderWriter;

/**
 * A class to represent a HTTP header as defined in <a
 * href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec4.html#sec4.2">RFC
 * 2616</a>. Form is "field-name":["field-value"]. Can be parsed with
 * {@link HttpHeaderParser} and written with {@link HttpHeaderWriter}.
 *
 * @author Joachim Lusiardi
 */
public class HttpHeader {

    private String name;
    private String value;

    public HttpHeader(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "HttpHeader{" + "name=" + name + ", value=" + value + '}';
    }
}
