package de.lusiardi.proxy.data;

import de.lusiardi.proxy.parsers.HttpBodyChunkParser;
import de.lusiardi.proxy.writers.HttpBodyChunkWriter;

/**
 * A class to represent a HTTP chunk as defined in <a
 * href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.6.1">RFC
 * 2616</a>. Can be parsed with {@link HttpBodyChunkParser} and written with
 * {@link HttpBodyChunkWriter}.
 *
 * @author Joachim Lusiardi
 */
public class HttpBodyChunk {

    private int size;
    private byte[] data;

    /**
     * Returns the size of the chunk.
     *
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * Sets the size of the chunk.
     *
     * @param size the new size
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Returns the data from the chunk.
     *
     * @return the data as {@code byte[]}
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Sets the data of the chunk.
     *
     * @param data the data as {@code byte[]}
     */
    public void setData(byte[] data) {
        this.data = data;
    }
}
