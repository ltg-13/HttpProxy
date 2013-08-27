package de.lusiardi.soa.proxy;

import com.google.common.io.Files;
import de.lusiardi.soa.proxy.data.HttpHost;
import de.lusiardi.soa.proxy.exceptions.HeaderParseException;
import de.lusiardi.soa.proxy.parsers.HttpHostParser;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Properties;

/**
 * Handle configuration of the Proxy.
 *
 * @author shing19m
 */
public class Configuration {

    public static final String LISTENING_PORT_KEY = "listening_port";

    private int listeningPort;

    public static final String DEFAULT_TARGET_KEY = "default_target";

    private HttpHost defaultTarget;

    public static final String LISTENING_IP_KEY = "listening_ip";

    private InetAddress listeningIp;

    public static final String SCRIPT_TYPE_KEY = "script_type";

    private String scriptType;

    public static final String SCRIPT_FILE_KEY = "script_file";

    private String scriptFile;

    private String scriptContent;

    /**
     * Creates a configuration with values from the class path file
     * "/configuration.properties".
     *
     * @throws IOException
     * @throws HeaderParseException
     */
    public Configuration() throws IOException, HeaderParseException {
        parseProperties(new BufferedInputStream(getClass().getResourceAsStream("/configuration.properties")));
    }

    /**
     * Creates a configuration with values from the given file.
     *
     * @param file the file to open
     * @throws IOException
     * @throws HeaderParseException
     */
    public Configuration(String file) throws IOException, HeaderParseException {
        parseProperties(new FileInputStream(file));
    }

    private void parseProperties(InputStream is) throws IOException, HeaderParseException {
        Properties properties = new Properties();
        properties.load(is);
        is.close();

        // get listening port
        listeningPort = Integer.parseInt(properties.getProperty(LISTENING_PORT_KEY));

        // get listening ip
        listeningIp = InetAddress.getByName(properties.getProperty(LISTENING_IP_KEY));

        // get default target
        HttpHostParser hostParser = new HttpHostParser();
        defaultTarget = hostParser.parse(properties.getProperty(DEFAULT_TARGET_KEY));

        scriptType = properties.getProperty(SCRIPT_TYPE_KEY);

        scriptFile = properties.getProperty(SCRIPT_FILE_KEY);

        File f = new File(scriptFile);
        byte[] tmp = Files.toByteArray(f);
        scriptContent = new String(tmp);
    }

    public int getListeningPort() {
        return listeningPort;
    }

    public HttpHost getDefaultTarget() {
        return defaultTarget;
    }

    public InetAddress getListeningIp() {
        return listeningIp;
    }

    public HttpHost getListeningHost() {
        return new HttpHost(listeningIp.getCanonicalHostName(), listeningPort);
    }

    public String getScriptType() {
        return scriptType;
    }

    public String getScriptFile() {
        return scriptFile;
    }

    public String getScriptContent() {
        return scriptContent;
    }
}
