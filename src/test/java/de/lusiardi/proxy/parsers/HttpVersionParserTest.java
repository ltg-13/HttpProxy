package de.lusiardi.proxy.parsers;

import de.lusiardi.proxy.parsers.HttpVersionParser;
import de.lusiardi.proxy.data.HttpVersion;
import de.lusiardi.proxy.exceptions.VersionParseException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 *
 * @author shing19m
 */
public class HttpVersionParserTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void test_parse_empty() throws VersionParseException {
        exception.expect(VersionParseException.class);

        HttpVersionParser versionParser = new HttpVersionParser();
        versionParser.parse("");
    }

    @Test
    public void test_parse_no_slash() throws VersionParseException {
        exception.expect(VersionParseException.class);

        HttpVersionParser versionParser = new HttpVersionParser();
        versionParser.parse("foo");
    }

    @Test
    public void test_parse_slash_but_no_dot() throws VersionParseException {
        exception.expect(VersionParseException.class);

        HttpVersionParser versionParser = new HttpVersionParser();
        versionParser.parse("HTTP/bar");
    }

    @Test
    public void test_parse_slash_dot_but_only_one_part() throws VersionParseException {
        exception.expect(VersionParseException.class);

        HttpVersionParser versionParser = new HttpVersionParser();
        versionParser.parse("HTTP/bar.");
    }

    @Test
    public void test_parse_slash_dot_but_no_int() throws VersionParseException {
        exception.expect(VersionParseException.class);

        HttpVersionParser versionParser = new HttpVersionParser();
        versionParser.parse("HTTP/bar.baz");
    }

    @Test
    public void test_parse_slash_dot_int_no_HTTP() throws VersionParseException {
        exception.expect(VersionParseException.class);

        HttpVersionParser versionParser = new HttpVersionParser();
        versionParser.parse("foo/1.1");
    }

    @Test
    public void test_parse_slash_dot_int() throws VersionParseException {
        HttpVersionParser versionParser = new HttpVersionParser();
        HttpVersion result = versionParser.parse("HTTP/1.0");

        assertEquals(1, result.getMajorVersion());
        assertEquals(0, result.getMinorVersion());
    }
}
