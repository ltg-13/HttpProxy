package de.lusiardi.soa.proxy.scripting;

import com.google.common.base.Stopwatch;
import de.lusiardi.soa.proxy.Configuration;
import de.lusiardi.soa.proxy.data.HttpRequest;
import de.lusiardi.soa.proxy.data.HttpVersion;
import de.lusiardi.soa.proxy.exceptions.HeaderParseException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.script.ScriptException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author shing19m
 */
public class ScriptingTest {

    private Scripting scripting;

    @Before
    public void setup() throws IOException, HeaderParseException, ScriptException {
        Configuration configuration = new Configuration();
        scripting = new Scripting(configuration);

    }

    @Test
    public void test_wait() throws NoSuchMethodException, ScriptException {
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();
        scripting.getInvocable().invokeFunction("wait", 2000);
        stopwatch.stop();
        assertEquals(2000.0, stopwatch.elapsed(TimeUnit.MILLISECONDS), 50);
        stopwatch.reset();
        stopwatch.start();
        scripting.getInvocable().invokeFunction("wait", 1000);
        stopwatch.stop();
        assertEquals(1000.0, stopwatch.elapsed(TimeUnit.MILLISECONDS), 50);
    }

    @Test
    public void test_logInfo() throws ScriptException, NoSuchMethodException {
        ProvidedFunctions functions = new ProvidedFunctions();
        final HttpRequest httpRequest = new HttpRequest();
        httpRequest.setVersion(new HttpVersion(1, 2));
        functions.logInfo(httpRequest);
        scripting.getInvocable().invokeFunction("logRequest", httpRequest);
    }
}
