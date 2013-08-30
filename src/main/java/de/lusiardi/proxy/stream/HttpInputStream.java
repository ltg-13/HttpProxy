package de.lusiardi.proxy.stream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.log4j.Logger;

/**
 *
 * @author shing19m
 */
public class HttpInputStream {

    private static Logger logger = Logger.getLogger(HttpInputStream.class);
    private BufferedInputStream bufferedInputStream;
    private byte[] buffer = new byte[0];

    public HttpInputStream(InputStream inputStream) {
        this.bufferedInputStream = new BufferedInputStream(inputStream);
    }

    public String readNonEmptyLine() throws IOException {
        String line = readLine();
        int tries = 0;
        while (line.isEmpty()) {

            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
            }
            line = readLine();
            tries++;
        }
        logger.debug("tried to read a line '" + tries + "' times");
        return line;
    }

    public String readLine() throws IOException {
        logger.debug("starting readLine");
        fillBuffer();

        int index = 0;
        while (index < buffer.length && buffer[index] != '\n' && buffer[index] != '\r') {
            index++;
            if (index == buffer.length) {
                fillBuffer();
            }
        }
        byte[] lineBytes = new byte[index];

        System.arraycopy(buffer, 0, lineBytes, 0, index);
        logger.debug("copied '" + index + "' bytes");

        if (index == buffer.length) {
        } else if (buffer[index] == '\n') {
            trimBuffer(index + 1);
        } else {
            if (buffer[index] == '\r') {
                if ((index + 1 < buffer.length) && (buffer[index + 1] == '\n')) {
                    trimBuffer(index + 2);
                } else {
                    trimBuffer(index + 1);
                }
            }
        }
        return new String(lineBytes);
    }

    public int readBytes(byte[] target, int offset, int maxLength) throws IOException {
        fillBuffer();
        int toCopy = Math.min(maxLength, buffer.length);

        System.arraycopy(buffer, 0, target, offset, toCopy);
        logger.debug("copied '" + toCopy + "' bytes");

        trimBuffer(toCopy);

        return toCopy;
    }

    private void trimBuffer(int toSkip) {
        logger.debug("trimming the buffer by '" + toSkip + "' bytes");

        byte[] newBuffer = new byte[buffer.length - toSkip];
        System.arraycopy(buffer, toSkip, newBuffer, 0, newBuffer.length);

        logger.debug("copied '" + newBuffer.length + "' bytes");

        buffer = newBuffer;
    }

    private int fillBuffer() throws IOException {
        int availiableBytes = bufferedInputStream.available();
        if (availiableBytes == 0) {
            logger.debug("no bytes available for read");
            return 0;
        }
        byte[] additionalRead = new byte[availiableBytes];
        int readBytes = bufferedInputStream.read(additionalRead, 0, availiableBytes);
        if (readBytes == -1) {
            logger.debug("hit end of stream");
            return -1;
        }

        logger.debug("extending the buffer by '" + readBytes + "' bytes");
        byte[] newBuffer = new byte[buffer.length + readBytes];

        System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
        logger.debug("copied '" + buffer.length + "' bytes");

        System.arraycopy(additionalRead, 0, newBuffer, buffer.length, readBytes);
        logger.debug("copied '" + readBytes + "' bytes");

        buffer = newBuffer;

        return readBytes;
    }
}
