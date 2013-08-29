package de.lusiardi.soa.proxy.scripting;

import de.lusiardi.soa.proxy.Configuration;
import de.lusiardi.soa.proxy.data.HttpHost;
import de.lusiardi.soa.proxy.data.HttpRequest;
import de.lusiardi.soa.proxy.data.HttpResponse;
import de.lusiardi.soa.proxy.writers.HttpRequestWriter;
import de.lusiardi.soa.proxy.writers.HttpResponseWriter;
import java.io.OutputStream;
import java.net.Socket;
import org.apache.log4j.Logger;

/**
 *
 * @author shing19m
 */
public class ProvidedFunctions {

    private static Logger logger = Logger.getLogger(ProvidedFunctions.class);
    private static Logger scriptLogger;
    private HttpRequestWriter requestWriter;
    private HttpResponseWriter responseWriter;
    private Configuration config;
    private Socket target = null;
    private Socket source;

    public ProvidedFunctions(Socket source, Configuration config) {
        scriptLogger = Logger.getLogger("SCRIPT");
        requestWriter = new HttpRequestWriter();
        responseWriter = new HttpResponseWriter();
        this.config = config;
        this.source = source;
    }

    /**
     * Return the socket to the target host. This will non-null after
     * {@link #sendRequest(de.lusiardi.soa.proxy.data.HttpRequest)} was called.
     *
     * @return the open socket
     */
    Socket getTargetSocket() {
        return target;
    }

    public void logRequest(HttpRequest request) {
        scriptLogger.info("\n" + requestWriter.writeToString(request, "      "));
    }

   public void logResponse(HttpResponse response) {
        scriptLogger.info("\n" + responseWriter.writeToString(response, "      "));
    }

    public void debug(String message) {
        scriptLogger.debug(message);
    }

    public void sendRequest(HttpRequest request) {
        try {
            HttpHost targetHost = request.getHeaders().getHost();
            if (targetHost != null && !targetHost.equals(config.getListeningHost())) {
                logger.debug("using '" + targetHost + "' as target");
                target = new Socket(targetHost.getHost(), targetHost.getPort());
            } else {
                logger.debug("using '" + config.getDefaultTarget() + "' as target");
                target = new Socket(config.getDefaultTarget().getHost(), config.getDefaultTarget().getPort());
            }
            final OutputStream targetOutput = target.getOutputStream();
            requestWriter.writeToStream(request, targetOutput);
            targetOutput.flush();
        } catch (Exception ex) {
            logger.error("Fail", ex);
        }
    }

    public void sendResponse(HttpResponse response) {
        try {
            final OutputStream sourceOutput = source.getOutputStream();
            responseWriter.writeToStream(response, sourceOutput);
            sourceOutput.flush();
        } catch (Exception ex) {
            logger.error("Fail", ex);
        }
    }
}
