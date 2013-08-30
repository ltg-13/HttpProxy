package de.lusiardi.proxy.data;

import de.lusiardi.proxy.data.HttpHeader;
import de.lusiardi.proxy.exceptions.HeaderParseException;
import org.hamcrest.core.IsEqual;
import static org.hamcrest.core.StringContains.containsString;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 *
 * @author shing19m
 */
public class HttpHeaderTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void test_constructor_getter() {
        HttpHeader header = new HttpHeader("name", "value");
        assertEquals("name", header.getName());
        assertEquals("value", header.getValue());
    }

    @Test
    public void test_set_value() {
        HttpHeader header = new HttpHeader("name", "value");
        header.setValue("value_1");
        assertEquals("value_1", header.getValue());
    }

    @Test
    public void test_toString() {
        HttpHeader header = new HttpHeader("name", "value");
        assertThat(header.toString(), containsString("name"));
        assertThat(header.toString(), containsString("value"));
        assertThat(header.toString(), containsString("HttpHeader"));
    }
}
