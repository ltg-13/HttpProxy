package de.lusiardi.proxy.writers;

import de.lusiardi.proxy.writers.HttpVersionWriter;
import de.lusiardi.proxy.data.HttpVersion;
import static org.hamcrest.core.IsEqual.*;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 *
 * @author shing19m
 */
public class HttpVersionWriterTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void test_write_null() {
        exception.expect(IllegalArgumentException.class);

        HttpVersionWriter versionWriter = new HttpVersionWriter();
        versionWriter.write(null);
    }

    @Test
    public void test_write_header() {
        HttpVersionWriter versionWriter = new HttpVersionWriter();
        HttpVersion version = new HttpVersion(1, 0);
        String result = versionWriter.write(version);

        assertThat(result, equalTo("HTTP/1.0"));

    }

}
