package de.lusiardi.soa.proxy.parsers;

import de.lusiardi.soa.proxy.data.HttpHeader;
import de.lusiardi.soa.proxy.data.HttpHost;
import de.lusiardi.soa.proxy.exceptions.HeaderParseException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 *
 * @author shing19m
 */
public class HttpHeaderParserTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void test_parse_empty() throws HeaderParseException {
        exception.expect(HeaderParseException.class);

        HttpHeaderParser headerParser = new HttpHeaderParser();
        headerParser.parse("");
    }

    @Test
    public void test_parse_no_colon() throws HeaderParseException {
        exception.expect(HeaderParseException.class);

        HttpHeaderParser headerParser = new HttpHeaderParser();
        headerParser.parse("foo");
    }

    @Test
    public void test_parse_set_1() throws HeaderParseException {
        HttpHeaderParser headerParser = new HttpHeaderParser();
        HttpHeader result = headerParser.parse("foo: bar");

        assertEquals("foo", result.getName());
        assertEquals("bar", result.getValue());
    }
}
