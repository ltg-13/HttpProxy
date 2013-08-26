package de.lusiardi.soa.proxy.writers;

import de.lusiardi.soa.proxy.data.HttpHeader;
import de.lusiardi.soa.proxy.data.HttpRequest;
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
