package de.lusiardi.soa.proxy.writers;

import de.lusiardi.soa.proxy.data.HttpBodyChunk;

/**
 *
 * @author shing19m
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
