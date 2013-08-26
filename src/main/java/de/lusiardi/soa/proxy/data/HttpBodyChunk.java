package de.lusiardi.soa.proxy.data;

/**
 * http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.6.1
 *
 * @author shing19m
 */
public class HttpBodyChunk {
    
    private int size;
    private byte[] data;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
