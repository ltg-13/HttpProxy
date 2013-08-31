package de.lusiardi.proxy.writers;

import de.lusiardi.proxy.data.HttpBodyChunk;

/**
 *
 * @author Joachim Lusiardi
 */
public class HttpBodyChunkWriter {

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
