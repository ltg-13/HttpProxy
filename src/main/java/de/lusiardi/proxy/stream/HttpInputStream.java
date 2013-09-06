package de.lusiardi.proxy.stream;

import de.lusiardi.proxy.exceptions.NotEnoughBytesAvailableException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.log4j.Logger;

/**
 * A wrapper around an {@link InputStream} to enable the reading of byte arrays
 * and strings in combination with a working buffering for both operations.
 *
 * @author Joachim Lusiardi
 */
public class HttpInputStream {

    private static Logger logger = Logger.getLogger(HttpInputStream.class);
    private InputStream bufferedInputStream;
    private byte[] buffer = new byte[0];
    private boolean endOfStream = false;

    /**
     * Constructs a new instance from the given input stream.
     *
     * @param inputStream the basic input stream to read from. Must not be
     * {@code null}.
     */
    public HttpInputStream(InputStream inputStream) {
        this.bufferedInputStream = new BufferedInputStream(inputStream);
    }

    /**
     * Reads a line from the input stream. Reading the line will be repeated
     * until a non empty line was read. As exception, if the end of the stream
     * was hit, we stop reading in to avoid an endless loop here.
     *
     * @return the read line non empty
     * @throws IOException on problems while filling the buffer
     * @see #readLine()
     */
    public String readNonEmptyLine() throws IOException {
        String line = readLine();
        int tries = 0;
        while (line.isEmpty() && !endOfStream) {

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

    /**
     * Reads a line from the input stream. Line endings are detected by '\n',
     * '\r' or '\r\n'. Line endings are returned by the function. Before it
     * tries to consume as much bytes from the underlying input stream as
     * possible. The buffer is then trimmed by the read number of bytes.
     *
     * @return the read line, may be empty
     * @throws IOException on problems while filling the buffer
     */
    public String readLine() throws IOException {
        logger.debug("starting readLine");

        // only fill buffer if neccessary. if it will be drained while looking for EOL it fill be filled.
        if (buffer.length == 0) {
            fillBuffer();
        }

        int index = 0;
        while (index < buffer.length && buffer[index] != '\n' && buffer[index] != '\r') {
            index++;
            if (index >= buffer.length) {
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

        final String result = new String(lineBytes);
        logger.debug("read line is '" + result + "'");
        return result;
    }

    /**
     * Reads bytes from the input stream. Before it tries to consume as much
     * bytes from the underlying input stream as possible. The buffer is then
     * trimmed by the read number of bytes.
     *
     * @param target the target for the operation
     * @param maxLength the number of bytes to read at maximum
     * @return the number of read bytes
     * @throws IOException on problems while filling the buffer
     */
    public int readBytes(byte[] target, int maxLength) throws IOException {
        if (buffer.length < maxLength) {
            fillBuffer();
        }
        int toCopy = Math.min(maxLength, buffer.length);

        System.arraycopy(buffer, 0, target, 0, toCopy);
        logger.debug("copied '" + toCopy + "' bytes");

        trimBuffer(toCopy);

        return toCopy;
    }

    /**
     * Reads the exact given number of bytes from the input stream. Before it
     * tries to consume as much bytes from the underlying input stream as
     * necessary. The buffer is then trimmed by the read number of bytes.
     *
     * @param target the target for the operation
     * @param length the exact number of bytes to read
     * @return the number of read bytes (must be always the same as param
     * length)
     * @throws IOException on problems while filling the buffer
     * @throws NotEnoughBytesAvailableException if the end of stream was
     * detected before the required amount of bytes were in the buffer
     */
    public int readFixedNumberOfBytes(byte[] target, int length) throws IOException, NotEnoughBytesAvailableException {
        int r;
        do {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                logger.debug("interupt");
            }
            r = fillBuffer();
            logger.debug("filled up '" + r + "' bytes");
        } while (r >= 0 && buffer.length < length);

        if (r == -1 && buffer.length < length) {
            throw new NotEnoughBytesAvailableException("Only '" + buffer.length + "' bytes of the requested '" + length + "' bytes could be read.");
        }
        System.arraycopy(buffer, 0, target, 0, length);
        logger.debug("copied '" + length + "' bytes");

        trimBuffer(length);

        return length;
    }

    /**
     * Trims the buffer by removing bytes from the front.
     *
     * @param toSkip the number of bytes to remove
     */
    private void trimBuffer(int toSkip) {
        logger.debug("trimming the buffer by '" + toSkip + "' bytes");

        byte[] newBuffer = new byte[buffer.length - toSkip];
        System.arraycopy(buffer, toSkip, newBuffer, 0, newBuffer.length);

        logger.debug("copied '" + newBuffer.length + "' bytes");

        buffer = newBuffer;
    }

    /**
     * Tries to fill up the internal buffer. Tries to read as many bytes as
     * available copies them to the internal buffer and updates
     * {@link #endOfStream}.
     *
     * @return the amount of bytes added to the internal buffer or -1 if the end
     * of stream was hit
     * @throws IOException on issues while reading the stream
     */
    private int fillBuffer() throws IOException {
        int availiableBytes = bufferedInputStream.available();
        if (availiableBytes == 0) {
            availiableBytes = 1;
        }
        logger.debug("will try to read '" + availiableBytes + "' of data " + bufferedInputStream.toString());
        byte[] additionalRead = new byte[availiableBytes];
        int readBytes = bufferedInputStream.read(additionalRead, 0, availiableBytes);
        logger.debug("read '" + readBytes + "' of data");
        if (readBytes == -1) {
            logger.debug("hit end of stream");
            endOfStream = true;
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
