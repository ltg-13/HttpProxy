package de.lusiardi.proxy;

import de.lusiardi.proxy.exceptions.HeaderParseException;
import de.lusiardi.proxy.exceptions.ScriptPreparationException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.log4j.Logger;

public class Proxy {

    private static Logger logger = Logger.getLogger(Proxy.class);

    public Proxy() throws HeaderParseException, IOException, ScriptPreparationException {
        logger.debug("starting proxy");

        Configuration config = new Configuration();

        ServerSocket server = new ServerSocket(config.getListeningPort(), 0, config.getListeningIp());

        while (true) {
            Socket socket = server.accept();
            logger.debug("accepting socket '" + socket + "'");
            (new ProxyThreadString(socket, config)).start();
        }
    }

    public static void main(String[] args) throws IOException, HeaderParseException, ScriptPreparationException {
        new Proxy();
    }
}
