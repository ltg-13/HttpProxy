package de.lusiardi.soa.proxy.parsers;

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
public class HttpHostParserTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void test_parse_empty() throws HeaderParseException {
        exception.expect(HeaderParseException.class);
        
        HttpHostParser hostParser = new HttpHostParser();
        hostParser.parse("");
    }

    @Test
    public void test_parse_no_colon() throws HeaderParseException {
        HttpHostParser hostParser = new HttpHostParser();
        HttpHost result = hostParser.parse("foo");
        assertEquals("foo", result.getHost());
        assertEquals(80, result.getPort());
    }

    @Test
    public void test_parse_colon() throws HeaderParseException {
        HttpHostParser hostParser = new HttpHostParser();
        HttpHost result = hostParser.parse("foo:31");
        assertEquals("foo", result.getHost());
        assertEquals(31, result.getPort());
    }
}
