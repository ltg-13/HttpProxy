package de.lusiardi.proxy;

import com.google.common.io.Files;
import de.lusiardi.proxy.data.HttpHost;
import de.lusiardi.proxy.exceptions.HeaderParseException;
import de.lusiardi.proxy.parsers.HttpHostParser;
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
 * @author Joachim Lusiardi
 */
public class Configuration {

    /**
     * Configuration key for the listening port of the proxy, its value is
     * {@value #LISTENING_PORT_KEY}.
     */
    public static final String LISTENING_PORT_KEY = "listening_port";
    private int listeningPort;
    /**
     * Configuration key for the default target host of the proxy, its value is
     * {@value #DEFAULT_TARGET_KEY}.
     */
    public static final String DEFAULT_TARGET_KEY = "default_target";
    private HttpHost defaultTarget;
    /**
     * Configuration key for the listening IP of the proxy, its value is
     * {@value #LISTENING_IP_KEY}.
     */
    public static final String LISTENING_IP_KEY = "listening_ip";
    private InetAddress listeningIp;
    /**
     * Configuration key for the script type used in the proxy, its value is
     * {@value #SCRIPT_TYPE_KEY}.
     */
    public static final String SCRIPT_TYPE_KEY = "script_type";
    private String scriptType;
    /**
     * Configuration key for the script file used in the proxy, its value is
     * {@value #SCRIPT_FILE_KEY}.
     */
    public static final String SCRIPT_FILE_KEY = "script_file";
    private String scriptFile;
    private String scriptContent;

    /**
     * Creates a configuration with values from the class path file
     * "/configuration.properties". This also loads the content of the
     * referenced script file.
     *
     * @throws IOException
     * @throws HeaderParseException
     */
    public Configuration() throws IOException, HeaderParseException {
        parseProperties(new BufferedInputStream(getClass().getResourceAsStream("/configuration.properties")));
    }

    /**
     * Creates a configuration with values from the given file. This also loads
     * the content of the referenced script file.
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

    /**
     * Returns the listening port.
     *
     * @return the configured port
     */
    public int getListeningPort() {
        return listeningPort;
    }

    /**
     * Returns the information about the default target host and port.
     *
     * @return an instance of {@link HttpHost}
     */
    public HttpHost getDefaultTarget() {
        return defaultTarget;
    }

    /**
     * Returns the listening IP.
     *
     * @return the listening IP as {@link InetAddress}
     */
    public InetAddress getListeningIp() {
        return listeningIp;
    }

    /**
     * Returns the information about the listening IP and port.
     *
     * @return an instance of {@link HttpHost}
     */
    public HttpHost getListeningHost() {
        return new HttpHost(listeningIp.getCanonicalHostName(), listeningPort);
    }

    /**
     * Returns the configured script type.
     *
     * @return the scripting language
     */
    public String getScriptType() {
        return scriptType;
    }

    /**
     * Return the name and path of the configured script file.
     *
     * @return the name
     */
    public String getScriptFile() {
        return scriptFile;
    }

    /**
     * Returns the content of the configured script file.
     *
     * @return the content
     */
    public String getScriptContent() {
        return scriptContent;
    }
}
