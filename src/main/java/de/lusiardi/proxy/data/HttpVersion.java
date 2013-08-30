package de.lusiardi.proxy.data;

import de.lusiardi.proxy.parsers.HttpVersionParser;
import de.lusiardi.proxy.writers.HttpVersionWriter;

/**
 * A class to represent a HTTPVersion as defined in <a
 * href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.1">RFC
 * 2616</a>. Can be parsed with {@link HttpVersionParser} and written with
 * {@link HttpVersionWriter}.
 *
 * @author Joachim Lusiardi
 */
public class HttpVersion {

    private int majorVersion;
    private int minorVersion;

    /**
     * Creates a new instance with the given major and minor versions.
     *
     * @param majorVersion the major version
     * @param minorVersion the minor version
     */
    public HttpVersion(int majorVersion, int minorVersion) {
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
    }

    /**
     * Returns the major version.
     *
     * @return the major version
     */
    public int getMajorVersion() {
        return majorVersion;
    }

    /**
     * Returns the minor version.
     *
     * @return the minor version
     */
    public int getMinorVersion() {
        return minorVersion;
    }

    /**
     * {@inheritDoc }
     *
     * @return the string representation of the HTTP version
     */
    @Override
    public String toString() {
        return "HttpVersion{" + "majorVersion=" + majorVersion + ", minorVersion=" + minorVersion + '}';
    }
}
