package de.lusiardi.soa.proxy;

import de.lusiardi.soa.proxy.data.HttpRequest;
import de.lusiardi.soa.proxy.data.HttpResponse;
import de.lusiardi.soa.proxy.parsers.HttpRequestParser;
import de.lusiardi.soa.proxy.parsers.HttpResponseParser;
import de.lusiardi.soa.proxy.scripting.Scripting;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import javax.script.ScriptException;
import org.apache.log4j.Logger;

public class ProxyThreadString extends Thread implements Runnable {

    private final Socket socket;

    private final Scripting scripting;

    private static Logger logger = Logger.getLogger(ProxyThreadString.class);

    private HttpRequestParser requestParser = new HttpRequestParser();

    private HttpResponseParser responseParser = new HttpResponseParser();

    public ProxyThreadString(Socket socket, Configuration config) throws ScriptException, IOException {
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
