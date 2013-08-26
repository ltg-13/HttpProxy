package de.lusiardi.soa.proxy.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.6.1
 *
 * @author shing19m
 */
public class HttpBodyChunks implements Iterable<HttpBodyChunk>{

    private List<HttpBodyChunk> chunks = new ArrayList<HttpBodyChunk>();

    public void add(HttpBodyChunk chunk) {
        chunks.add(chunk);
    }

    public Iterator<HttpBodyChunk> iterator() {
        return chunks.iterator();
    }
}
