package de.lusiardi.proxy.writers;

import de.lusiardi.proxy.writers.HttpRequestWriter;
import de.lusiardi.proxy.data.HttpHeader;
import de.lusiardi.proxy.data.HttpRequest;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 *
 * @author shing19m
 */
public class HttpRequestWriterTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void test_write_empty_request() {
        exception.expect(IllegalArgumentException.class);

        HttpRequestWriter headerWriter = new HttpRequestWriter();
        headerWriter.writeToString(new HttpRequest(), "");
    }

}
