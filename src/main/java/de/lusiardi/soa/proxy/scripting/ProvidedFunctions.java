package de.lusiardi.soa.proxy.scripting;

import de.lusiardi.soa.proxy.data.HttpRequest;
import de.lusiardi.soa.proxy.writers.HttpRequestWriter;
import org.apache.log4j.Logger;

/**
 *
 * @author shing19m
 */
public class ProvidedFunctions {

    private static Logger scriptLogger;
    HttpRequestWriter requestWriter;

    public ProvidedFunctions() {
        scriptLogger = Logger.getLogger(getClass());
        requestWriter = new HttpRequestWriter();
    }

    public void logInfo(HttpRequest request) {
        scriptLogger.info("request: \n" +    requestWriter.writeToString(request, "      >"));
    }
}
