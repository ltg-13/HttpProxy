package de.lusiardi.proxy.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class to hold series of of HTTP body chunks.
 *
 * @author Joachim Lusiardi
 */
public class HttpBodyChunks implements Iterable<HttpBodyChunk> {

    private List<HttpBodyChunk> chunks = new ArrayList<HttpBodyChunk>();

    /**
     * Adds a chunk to the list of chunks. The chunk must not be {@code null}.
     *
     * @param chunk the chunk to add
     */
    public void add(HttpBodyChunk chunk) {
        if (chunk == null) {
            throw new IllegalArgumentException("the chunk to add must not be null");
        }
        chunks.add(chunk);
    }

    /**
     * Returns an iterator over the chunks.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<HttpBodyChunk> iterator() {
        return chunks.iterator();
    }
}
