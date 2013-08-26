package de.lusiardi.soa.proxy.data;

/**
 *
 * @author shing19m
 */
public class HttpHost {

    private String host;
    private int port;

    public HttpHost(String host) {
        this.host = host;
        this.port = 80;
    }

    public HttpHost(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

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
