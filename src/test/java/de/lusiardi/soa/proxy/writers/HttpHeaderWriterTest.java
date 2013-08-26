package de.lusiardi.soa.proxy.writers;

import de.lusiardi.soa.proxy.data.HttpHeader;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.core.IsEqual.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 *
 * @author shing19m
 */
public class HttpHeaderWriterTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void test_write_null() {
        exception.expect(IllegalArgumentException.class);

        HttpHeaderWriter headerWriter = new HttpHeaderWriter();
        headerWriter.write(null);
    }

    @Test
    public void test_write_header() {
        HttpHeaderWriter headerWriter = new HttpHeaderWriter();
        HttpHeader header = new HttpHeader("name", "value");
        String result = headerWriter.write(header);

        assertThat(result, equalTo("name: value\r\n"));

    }

}
