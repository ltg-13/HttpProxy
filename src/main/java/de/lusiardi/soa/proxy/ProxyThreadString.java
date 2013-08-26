package de.lusiardi.soa.proxy;

import de.lusiardi.soa.proxy.data.HttpHost;
import de.lusiardi.soa.proxy.data.HttpRequest;
import de.lusiardi.soa.proxy.data.HttpResponse;
import de.lusiardi.soa.proxy.exceptions.HeaderParseException;
import de.lusiardi.soa.proxy.exceptions.RequestParseException;
import de.lusiardi.soa.proxy.exceptions.ResponseParseException;
import de.lusiardi.soa.proxy.exceptions.VersionParseException;
import de.lusiardi.soa.proxy.parsers.HttpRequestParser;
import de.lusiardi.soa.proxy.parsers.HttpResponseParser;
import de.lusiardi.soa.proxy.writers.HttpRequestWriter;
import de.lusiardi.soa.proxy.writers.HttpResponseWriter;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class ProxyThreadString extends Thread implements Runnable {

    private final Socket socket;
    private final Configuration config;
    private static Logger logger = Logger.getLogger(ProxyThreadString.class);
    private static Logger scriptLogger = Logger.getLogger("scriptLogger");
    private HttpRequestWriter requestWriter = new HttpRequestWriter();
    private HttpRequestParser requestParser = new HttpRequestParser();
    private HttpResponseParser responseParser = new HttpResponseParser();
    private HttpResponseWriter responseWriter = new HttpResponseWriter();

    public ProxyThreadString(Socket socket, Configuration config) {
        this.socket = socket;
        this.config = config;
    }

    @Override
    public void run() {
        try {
            logger.debug("starting thread with socket '" + socket.hashCode() + "' in '" + this + "'");

            // create script engine
            ScriptEngineManager factory = new ScriptEngineManager();
            ScriptEngine engine = factory.getEngineByName(config.getScriptType());
            engine.put("logger", scriptLogger);
            engine.put("requestWriter", requestWriter);
            Object result = engine.eval(config.getScriptContent());

            BufferedInputStream stream = new BufferedInputStream(getClass().getResourceAsStream("/ProvidedFunctions.js"));
            engine.eval(IOUtils.toString(stream, "UTF-8"));
            logger.info(result);
            Invocable inv = (Invocable) engine;

            // read the request from the browser
            HttpRequest request = requestParser.parse(socket.getInputStream());
//            logger.info("request: \n{}", requestWriter.writeToString(request, "      >"));

            Socket target = null;
            HttpHost targetHost = request.getHeaders().getHost();
            if (targetHost != null && !targetHost.equals(config.getListeningHost())) {
                logger.debug("using '" + targetHost + "' as target");
                target = new Socket(targetHost.getHost(), targetHost.getPort());
            } else {
                logger.debug("using '" + config.getDefaultTarget() + "' as target");
                target = new Socket(config.getDefaultTarget().getHost(), config.getDefaultTarget().getPort());
            }

            final OutputStream targetOutput = target.getOutputStream();
            inv.invokeFunction("beforeRequest", request);
            requestWriter.writeToStream(request, targetOutput);
            targetOutput.flush();

            final InputStream targetInput = target.getInputStream();


            try {
                TimeUnit.MILLISECONDS.sleep(10);
                while (targetInput.available() == 0) {
                    TimeUnit.MILLISECONDS.sleep(10);
                }
            } catch (InterruptedException ex) {
                logger.error("problem: ", ex);
            }

            HttpResponse response = responseParser.parse(targetInput);
//            logger.info("response: \n{}", responseWriter.writeToString(response, "      >"));
//            logger.info("response has chucked transfer encoding '{}'", response.getHeaders().hasChunkedTransferEncoding());

            final OutputStream sourceOutput = socket.getOutputStream();
            responseWriter.writeToStream(response, sourceOutput);
            sourceOutput.flush();

            socket.close();
            target.close();
        } catch (IOException ex) {
            logger.error("problem: ", ex);
        } catch (RequestParseException ex) {
            logger.error("problem: ", ex);
        } catch (VersionParseException ex) {
            logger.error("problem: ", ex);
        } catch (HeaderParseException ex) {
            logger.error("problem: ", ex);
        } catch (ResponseParseException ex) {
            logger.error("problem: ", ex);
        } catch (ScriptException ex) {
            logger.error("problem: ", ex);
        } catch (NoSuchMethodException ex) {
            logger.error("problem: ", ex);
        }
    }

    private String readStringFromStream(final InputStream sourceInput) throws IOException {
        String input = "";
        while (sourceInput.available() > 0) {
            final int read = sourceInput.read();
            input += (char) read;
        }
        return input;
    }
}
