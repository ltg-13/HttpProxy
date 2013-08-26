package de.lusiardi.soa.proxy.stream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author shing19m
 */
public class HttpInputStreamTest {

    @Test
    public void testReadBytes_1() throws IOException {
        String input = "Test123fürdenHttpInputStream";
        ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        HttpInputStream his = new HttpInputStream(is);

        byte[] target1 = new byte[4];
        int read = his.readBytes(target1, 0, 4);
        assertEquals(4, read);

        byte[] target2 = new byte[4];
        read = his.readBytes(target2, 0, 4);
        assertEquals(4, read);

        assertThat(target1, IsNot.not(IsEqual.equalTo(target2)));
    }

    @Test
    public void testReadBytes_2() throws IOException {
        String input = "123456789012345678901234567890ü";
        ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        HttpInputStream his = new HttpInputStream(is);

        byte[] target1 = new byte[1000];
        int read = his.readBytes(target1, 0, 1000);
        assertEquals(input.getBytes().length, read);

    }

    @Test
    public void testReadLine_1() throws IOException {
        String input = "Test\n123\nfürdenHttp\nInputStream";
        ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        HttpInputStream his = new HttpInputStream(is);

        String line1 = his.readLine();
        assertEquals("Test", line1);

        String line2 = his.readLine();
        assertEquals("123", line2);

        String line3 = his.readLine();
        assertEquals("fürdenHttp", line3);
    }

    @Test
    public void testReadLine_2() throws IOException {
        String input = "Test\r123\rfürdenHttp\rInputStream";
        ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        HttpInputStream his = new HttpInputStream(is);

        String line1 = his.readLine();
        assertEquals("Test", line1);

        String line2 = his.readLine();
        assertEquals("123", line2);

        String line3 = his.readLine();
        assertEquals("fürdenHttp", line3);
    }

    @Test
    public void testReadLine_3() throws IOException {
        String input = "Test\r\n123\r\nfürdenHttp\r\nInputStream";
        ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        HttpInputStream his = new HttpInputStream(is);

        String line1 = his.readLine();
        assertEquals("Test", line1);

        String line2 = his.readLine();
        assertEquals("123", line2);

        String line3 = his.readLine();
        assertEquals("fürdenHttp", line3);
    }

    @Test
    public void testReadLine_4() throws IOException {
        String input = "InputStream";
        ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        HttpInputStream his = new HttpInputStream(is);

        String line1 = his.readLine();
        assertEquals("InputStream", line1);
    }
}
