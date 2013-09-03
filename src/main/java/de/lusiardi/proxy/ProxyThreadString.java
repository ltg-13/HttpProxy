package de.lusiardi.proxy;

import de.lusiardi.proxy.data.HttpRequest;
import de.lusiardi.proxy.data.HttpResponse;
import de.lusiardi.proxy.exceptions.ScriptPreparationException;
import de.lusiardi.proxy.parsers.HttpRequestParser;
import de.lusiardi.proxy.parsers.HttpResponseParser;
import de.lusiardi.proxy.scripting.Scripting;
import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

public class ProxyThreadString extends Thread implements Runnable {

    private final Socket socket;

    private final Scripting scripting;

    private static Logger logger = Logger.getLogger(ProxyThreadString.class);

    private HttpRequestParser requestParser = new HttpRequestParser();

    private HttpResponseParser responseParser = new HttpResponseParser();

    public ProxyThreadString(Socket socket, Configuration config) throws ScriptPreparationException {
        this.socket = socket;
        this.scripting = new Scripting(socket, config);
    }

    @Override
    public void run() {
        try {
            logger.debug("starting thread with socket '" + socket.hashCode() + "' in '" + this + "'");

            HttpRequest request = requestParser.parse(socket.getInputStream());
            scripting.before(request);

            Socket target = scripting.getTargetSocket();
            InputStream targetInput = target.getInputStream();
            
            try {
                TimeUnit.MILLISECONDS.sleep(10);
                while (targetInput.available() == 0) {
                    TimeUnit.MILLISECONDS.sleep(10);
                }
            } catch (InterruptedException ex) {
                logger.error("problem: ", ex);
            }
            
            HttpResponse response = responseParser.parse(targetInput);
            scripting.after(request, response);

            socket.close();
            target.close();
        } catch (Exception ex) {
            logger.error("problem: ", ex);
        }
    }
}
