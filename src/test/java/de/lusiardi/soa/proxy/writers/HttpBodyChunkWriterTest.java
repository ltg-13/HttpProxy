package de.lusiardi.soa.proxy.writers;

import de.lusiardi.soa.proxy.data.HttpBodyChunk;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.core.IsEqual.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 *
 * @author shing19m
 */
public class HttpBodyChunkWriterTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void test_write_null() {
        exception.expect(IllegalArgumentException.class);

        HttpBodyChunkWriter bodyChunkWriter = new HttpBodyChunkWriter();
        bodyChunkWriter.write(null);
    }

    @Test
    public void test_write_chunk_empty() {
        HttpBodyChunkWriter bodyChunkWriter = new HttpBodyChunkWriter();
        HttpBodyChunk chunk = new HttpBodyChunk();
        String result = bodyChunkWriter.write(chunk);

        assertThat(result, equalTo("0\r\n"));
    }

    @Test
    public void test_write_chunk() {
        HttpBodyChunkWriter bodyChunkWriter = new HttpBodyChunkWriter();
        HttpBodyChunk chunk = new HttpBodyChunk();
        byte[] bytes = "hello".getBytes();
        chunk.setData(bytes);
        chunk.setSize(bytes.length);
        String result = bodyChunkWriter.write(chunk);

        assertThat(result, equalTo("5\r\nhello\r\n"));
    }
}
