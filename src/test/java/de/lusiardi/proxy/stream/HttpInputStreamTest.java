package de.lusiardi.proxy.stream;

import de.lusiardi.proxy.exceptions.NotEnoughBytesAvailableException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 *
 * @author Joachim Lusiardi
 */
public class HttpInputStreamTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test(timeout = 1000)
    public void testReadBytes_1() throws IOException {
        String input = "Test123fürdenHttpInputStream";
        ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        HttpInputStream his = new HttpInputStream(is);

        byte[] target1 = new byte[4];
        int read = his.readBytes(target1, 4);
        assertEquals(4, read);

        byte[] target2 = new byte[4];
        read = his.readBytes(target2, 4);
        assertEquals(4, read);

        assertThat(target1, IsNot.not(IsEqual.equalTo(target2)));
    }

    @Test(timeout = 1000)
    public void testReadBytes_2() throws IOException {
        String input = "123456789012345678901234567890ü";
        ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        HttpInputStream his = new HttpInputStream(is);

        byte[] target1 = new byte[1000];
        int read = his.readBytes(target1, 1000);
        assertEquals(input.getBytes().length, read);

    }

    @Test(timeout = 10000)
    public void testReadBytes_3() throws IOException {
        String input = "123456789012345678901234567890ü";
        ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());

        byte[] target1 = new byte[1000];
        int read;
        do {
            read = is.read(target1, 0, 10);
            System.out.println(read);
        } while (read > 0);
        assertEquals(-1, read);
    }

    @Test(timeout = 10000)
    public void testReadFixedNumberOfBytes_1() throws Exception {
        exception.expect(NotEnoughBytesAvailableException.class);

        String input = "123456789012345678901234567890ü";
        ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        HttpInputStream his = new HttpInputStream(is);

        byte[] target1 = new byte[1000];
        int read = his.readFixedNumberOfBytes(target1, 1000);
        assertEquals(1000, read);

    }

    @Test(timeout = 1000)
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

    @Test(timeout = 1000)
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

    @Test(timeout = 1000)
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

    @Test(timeout = 1000)
    public void testReadLine_4() throws IOException {
        String input = "InputStream";
        ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        HttpInputStream his = new HttpInputStream(is);

        String line1 = his.readLine();
        assertEquals("InputStream", line1);
    }
}
