package de.lusiardi.proxy.parsers;

import de.lusiardi.proxy.data.HttpVersion;
import de.lusiardi.proxy.exceptions.VersionParseException;


/**
 *
 * @author shing19m
 */
public class HttpVersionParser {
    
    public HttpVersion parse(String part) throws VersionParseException {
        String[] p1 = part.split("/");
        
        if (p1.length != 2) {
            throw new VersionParseException("The version information is malformed '" + part + "'");
        }
        
        if (!p1[0].equals("HTTP")) {
            throw new VersionParseException("The version information is malformed '" + part + "'");
        }
        
        String[] p2 = p1[1].split("\\.");
        if (p2.length != 2) {
            throw new VersionParseException("The version information is malformed '" + part + "'");
        }
        
        int minor = 0;
        int major = 0;
        try {
            major = Integer.parseInt(p2[0]);
            minor = Integer.parseInt(p2[1]);
        } catch (NumberFormatException ex) {
            throw new VersionParseException("The version information is malformed '" + part + "'");
        }
        HttpVersion target = new HttpVersion(major, minor);
        return target;
    }
}
