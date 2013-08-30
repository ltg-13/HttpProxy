package de.lusiardi.proxy.data;

import de.lusiardi.proxy.parsers.HttpHostParser;

/**
 * A class to represent a HTTPHost as defined in <a
 * href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.23">RFC
 * 2616</a>. Can be parsed with {@link HttpHostParser}.
 *
 * @author Joachim Lusiardi
 */
public class HttpHost {

    private String host;
    private int port;

    /**
     * Creates an instance with only a host. The port will be set to a default
     * of 80 for HTTP connections.
     *
     * @param host the host
     */
    public HttpHost(String host) {
        this.host = host;
        this.port = 80;
    }

    /**
     * Creates an instance with host and port.
     *
     * @param host the host
     * @param port the port
     */
    public HttpHost(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Returns the host.
     *
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * Returns the port.
     *
     * @return the port
     */
    public int getPort() {
        return port;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.host != null ? this.host.hashCode() : 0);
        hash = 37 * hash + this.port;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HttpHost other = (HttpHost) obj;
        if ((this.host == null) ? (other.host != null) : !this.host.equals(other.host)) {
            return false;
        }
        if (this.port != other.port) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "HttpHost{" + "host=" + host + ", port=" + port + '}';
    }
}
