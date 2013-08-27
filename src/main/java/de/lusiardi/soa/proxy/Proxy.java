package de.lusiardi.soa.proxy;

import de.lusiardi.soa.proxy.exceptions.HeaderParseException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.script.ScriptException;
import org.apache.log4j.Logger;

public class Proxy {

    private static Logger logger = Logger.getLogger(Proxy.class);

    public Proxy() throws IOException, HeaderParseException, ScriptException {
        logger.debug("starting proxy");

        Configuration config = new Configuration();

        ServerSocket server = new ServerSocket(config.getListeningPort(), 0, config.getListeningIp());

        while (true) {
            Socket socket = server.accept();
            logger.debug("accepting socket '" + socket + "'");
            (new ProxyThreadString(socket, config)).start();
        }
    }

    public static void main(String[] args) throws IOException, HeaderParseException, ScriptException {
        new Proxy();
    }
}
