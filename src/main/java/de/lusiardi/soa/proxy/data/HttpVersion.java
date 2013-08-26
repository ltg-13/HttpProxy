package de.lusiardi.soa.proxy.data;

/**
 *
 * @author shing19m
 */
public class HttpVersion {

    private int majorVersion;
    private int minorVersion;

    public HttpVersion(int majorVersion, int minorVersion) {
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    public int getMinorVersion() {
        return minorVersion;
    }
}
