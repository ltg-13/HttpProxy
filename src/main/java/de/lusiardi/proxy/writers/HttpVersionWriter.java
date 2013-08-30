package de.lusiardi.proxy.writers;

import de.lusiardi.proxy.data.HttpVersion;

/**
 *
 * @author shing19m
 */
public class HttpVersionWriter {

    public String write(HttpVersion version) {
        if (version == null) {
            throw new IllegalArgumentException("header must not be 'null'");
        }
        return "HTTP/" + version.getMajorVersion() + "." + version.getMinorVersion();
    }
}
