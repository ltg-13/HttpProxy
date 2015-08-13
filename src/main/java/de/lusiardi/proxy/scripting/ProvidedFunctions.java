package de.lusiardi.proxy.scripting;

import de.lusiardi.proxy.Configuration;
import de.lusiardi.proxy.data.HttpHost;
import de.lusiardi.proxy.data.HttpRequest;
import de.lusiardi.proxy.data.HttpResponse;
import de.lusiardi.proxy.writers.HttpRequestWriter;
import de.lusiardi.proxy.writers.HttpResponseWriter;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;

import org.apache.log4j.Logger;

/**
 * Class that contains functions that are provided to the javascript part of the
 * proxy.
 *
 * @author Joachim Lusiardi
 */
public class ProvidedFunctions {

    private static Logger logger = Logger.getLogger(ProvidedFunctions.class);

    private static Logger scriptLogger;

    private HttpRequestWriter requestWriter;

    private HttpResponseWriter responseWriter;

    private Configuration config;

    private Socket target = null;

    private Socket source;

    /**
     * Constructs with the given source socket and configuration.
     *
     * @param source the socket between proxy and client
     * @param config the configuration of the proxy
     */
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

    /**
     * Logs a request to the script logger.
     *
     * @param request the request
     */
    public void logRequest(HttpRequest request, int maxLength) {
        String requestString = requestWriter.writeToString(request, "      ");
        if (maxLength != -1 && requestString.length() > maxLength) {
            requestString = requestString.substring(0, maxLength);
            requestString += "…";
        }
        scriptLogger.info("\n" + requestString);
    }

    /**
     * Logs a response to the script logger.
     *
     * @param response the response
     * @param maxLength the maximum length to log, {@code -1} means unlimited
     */
    public void logResponse(HttpResponse response, int maxLength) {
        String responseString = responseWriter.writeToString(response, "      ");
        if (maxLength != -1 && responseString.length() > maxLength) {
            responseString = responseString.substring(0, maxLength);
            responseString += "…";
        }
        scriptLogger.info("\n" + responseString);
    }

    /**
     * Writes a debug message to the script logger.
     *
     * @param message the string to write
     */
    public void debug(String message) {
        scriptLogger.debug(message);
    }

    /**
     * Analyses a request and send it to the correct target host. If the request
     * has a host http header, the value from the header is used as target, else
     * the configured default target is used.
     *
     * @param request the request to send
     * @see Configuration#getDefaultTarget()
     */
    public void sendRequest(HttpRequest request) {
        try {
            HttpHost targetHost = request.getHeaders().getHost();
            if (targetHost != null && !targetHost.equals(config.getListeningHost())) {
                logger.debug("using '" + targetHost + "' as target");
                URI uri = URI.create(request.getRequestURI());
                int port = uri.getPort();
                if (port < 0) {
                    if (uri.isAbsolute() && uri.isOpaque()) {
                        try {
                            port = Integer.parseInt(uri.getSchemeSpecificPart());
                        } catch (NumberFormatException e) {
                            port = targetHost.getPort();
                        }
                    }
                }
                target = new Socket(targetHost.getHost(), port);
            } else {
                logger.debug("using '" + config.getDefaultTarget() + "' as target");
                target = new Socket(config.getDefaultTarget().getHost(), config.getDefaultTarget().getPort());
            }
            final OutputStream targetOutput = target.getOutputStream();
            if ("CONNECT".equalsIgnoreCase(request.getMethod())) {
                source.getOutputStream().write("HTTP/1.0 200 Connection Established\r\n\r\n".getBytes());
                source.getOutputStream().flush();
                logger.debug("send request by ssl");
                TransferThread transferThread = new TransferThread(source.getInputStream(), target.getOutputStream());
                TransferThread thread = new TransferThread(target.getInputStream(), source.getOutputStream());
                transferThread.start();
                thread.start();
                transferThread.join();
                thread.join();
                logger.debug("send end by ssl");
                source.close();
                target.close();

            } else {
                requestWriter.writeToStream(request, targetOutput);
            }
            targetOutput.flush();
        } catch (Exception ex) {
            logger.error("Fail", ex);
        }
    }

    /**
     * Sends the given response object to the client of the proxy.
     *
     * @param response the response to send
     */
    public void sendResponse(HttpResponse response) {
        try {
            final OutputStream sourceOutput = source.getOutputStream();
            responseWriter.writeToStream(response, sourceOutput);
            sourceOutput.flush();
        } catch (Exception ex) {
            logger.error("Fail", ex);
        }
    }

    /**
     * Removes all entries for the given header from the request.
     *
     * @param header the name of the header as string
     * @param request the request to remove the header from
     */
    public void removeHeaderFrom(String header, HttpRequest request) {
        request.getHeaders().removeHeader(header);
    }
}
