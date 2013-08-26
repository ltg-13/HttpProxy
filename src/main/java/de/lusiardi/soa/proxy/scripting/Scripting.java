package de.lusiardi.soa.proxy.scripting;

import de.lusiardi.soa.proxy.Configuration;
import java.io.BufferedInputStream;
import java.io.IOException;
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

    public Scripting(Configuration config) throws ScriptException, IOException {
        engine = factory.getEngineByName(config.getScriptType());
        engine.put("providedFunctions", new ProvidedFunctions());
        BufferedInputStream stream = new BufferedInputStream(getClass().getResourceAsStream("/ProvidedFunctions.js"));
        engine.eval(IOUtils.toString(stream, "UTF-8"));
    }

    public Invocable getInvocable() {
        return (Invocable) engine;
    }
}
