package de.lusiardi.proxy.writers;

import de.lusiardi.proxy.data.HttpBodyChunk;

/**
 * Class to write HTTP chunks as defined in <a
 * href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.6.1">RFC
 * 2616</a>.
 *
 * @author Joachim Lusiardi
 */
public class HttpBodyChunkWriter {

    /**
     * Write a chunk into a string.
     *
     * @param chunk the chunk to write.
     * @return the string that contains the representation of the chunk
     */
    public String write(HttpBodyChunk chunk) {
        if (chunk == null) {
            throw new IllegalArgumentException("chunk must not be 'null'");
        }
        String result = Integer.toHexString(chunk.getSize()) + "\r\n";
        if (chunk.getData() != null) {
            result += (new String(chunk.getData())) + "\r\n";
        }
        return result;
    }
}
