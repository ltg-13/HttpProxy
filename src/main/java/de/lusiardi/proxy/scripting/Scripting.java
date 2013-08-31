package de.lusiardi.proxy.scripting;

import de.lusiardi.proxy.Configuration;
import de.lusiardi.proxy.data.HttpRequest;
import de.lusiardi.proxy.data.HttpResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author shing19m
 */
public class Scripting {

    private static final ScriptEngineManager factory = new ScriptEngineManager();

    private ScriptEngine engine;

    public final ProvidedFunctions providedFunctions;

    public Scripting(Socket source, Configuration config) throws ScriptException, IOException {
        engine = factory.getEngineByName(config.getScriptType());
        providedFunctions = new ProvidedFunctions(source, config);
        engine.put("providedFunctions", providedFunctions);
        BufferedInputStream stream = new BufferedInputStream(getClass().getResourceAsStream("/ProvidedFunctions.js"));
        engine.eval(IOUtils.toString(stream, "UTF-8"));

        // load user script from configuration
        engine.eval(config.getScriptContent());
    }

    /**
     * Returns the socket to the target host. This is a delegate to {@link
     * ProvidedFunctions#getTargetSocket()}.
     *
     * @return the socket to communicate with the target
     */
    public Socket getTargetSocket() {
        return providedFunctions.getTargetSocket();
    }

    /**
     * returns the prepared engine.
     *
     * @return the engine to execute all functions.
     */
    Invocable getInvocable() {
        return (Invocable) engine;
    }

    /**
     * This calls the java script function {@code beforeRequest(request)}.
     *
     * @param request the HTTP request that sould be send to the target
     * @throws ScriptException
     * @throws NoSuchMethodException
     */
    public void before(HttpRequest request) throws ScriptException, NoSuchMethodException {
        getInvocable().invokeFunction("beforeRequest", request);
    }

    /**
     * This calls the java script function {@code beforeRequest(request)}.
     *
     * @param request the HTTP request that was sent to the target to retrieve
     * the response
     * @param response the HTTP response that should be send to the client of
     * the proxy
     * @throws ScriptException
     * @throws NoSuchMethodException
     */
    public void after(HttpRequest request, HttpResponse response) throws ScriptException, NoSuchMethodException {
        getInvocable().invokeFunction("afterRequest", request, response);
    }
}
