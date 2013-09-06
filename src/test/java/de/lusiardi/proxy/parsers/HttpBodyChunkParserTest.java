package de.lusiardi.proxy.parsers;

import de.lusiardi.proxy.data.HttpBodyChunk;
import de.lusiardi.proxy.exceptions.ChunkParseException;
import de.lusiardi.proxy.stream.HttpInputStream;
import java.io.ByteArrayInputStream;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;

/**
 *
 * @author Joachim Lusiardi
 */
public class HttpBodyChunkParserTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void test_parse_empty() throws ChunkParseException {
        exception.expect(ChunkParseException.class);

        ByteArrayInputStream bais = new ByteArrayInputStream("".getBytes());
        HttpInputStream is = new HttpInputStream(bais);

        HttpBodyChunkParser bodyChunkParser = new HttpBodyChunkParser();
        HttpBodyChunk result = bodyChunkParser.parse(is);
        assertNotNull(result);
    }

    @Test
    public void test_parse_too_few_bytes() throws ChunkParseException {
        exception.expect(ChunkParseException.class);

        ByteArrayInputStream bais = new ByteArrayInputStream("15\r\n12345\r\n".getBytes());
        HttpInputStream is = new HttpInputStream(bais);

        HttpBodyChunkParser bodyChunkParser = new HttpBodyChunkParser();
        HttpBodyChunk result = bodyChunkParser.parse(is);
        assertNotNull(result);
    }

    @Test
    public void test_parse_correct() throws ChunkParseException {
        ByteArrayInputStream bais = new ByteArrayInputStream("5\r\n12345\r\n".getBytes());
        HttpInputStream is = new HttpInputStream(bais);

        HttpBodyChunkParser bodyChunkParser = new HttpBodyChunkParser();
        HttpBodyChunk result = bodyChunkParser.parse(is);
        assertEquals(5, result.getSize());
        assertEquals(5, result.getData().length);
        assertArrayEquals("12345".getBytes(), result.getData());
    }

    @Test
    public void test_parse_last_chunk() throws ChunkParseException {
        ByteArrayInputStream bais = new ByteArrayInputStream("0\r\n".getBytes());
        HttpInputStream is = new HttpInputStream(bais);

        HttpBodyChunkParser bodyChunkParser = new HttpBodyChunkParser();
        HttpBodyChunk result = bodyChunkParser.parse(is);
        assertEquals(0, result.getSize());
    }
}
