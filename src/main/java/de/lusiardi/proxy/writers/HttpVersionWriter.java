package de.lusiardi.proxy.writers;

import de.lusiardi.proxy.data.HttpVersion;

/**
 * Writes a {@link HttpVersion} to a string. Format will follow <a
 * href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.1">RFC
 * 2616</a>
 *
 * @author Joachim Lusiardi
 */
public class HttpVersionWriter {

    /**
     * Writes the given version object into a string following <a
     * href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.1">RFC
     * 2616</a>.
     *
     * @param version the given {@link HttpVersion} object, this must not be
     * {@code null}!
     * @return the formatted string
     */
    public String write(HttpVersion version) {
        if (version == null) {
            throw new IllegalArgumentException("version must not be 'null'");
        }
        return "HTTP/" + version.getMajorVersion() + "." + version.getMinorVersion();
    }
}
