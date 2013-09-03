package de.lusiardi.proxy.scripting;

import com.google.common.base.Stopwatch;
import de.lusiardi.proxy.Configuration;
import de.lusiardi.proxy.data.HttpRequest;
import de.lusiardi.proxy.data.HttpResponse;
import de.lusiardi.proxy.data.HttpVersion;
import java.util.concurrent.TimeUnit;
import javax.script.ScriptException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo;

/**
 *
 * @author shing19m
 */
public class ScriptingTest {

    private Scripting scripting;

    @Before
    public void setup() throws Exception {
        Configuration configuration = new Configuration("src/test/resources/test_configuration.properties");
        scripting = new Scripting(null, configuration);

    }

    @Test
    public void test_wait() throws NoSuchMethodException, ScriptException {
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();
        scripting.getInvocable().invokeFunction("wait", 2000);
        stopwatch.stop();
        assertThat((int)stopwatch.elapsed(TimeUnit.MILLISECONDS), is(greaterThanOrEqualTo(2000)));
        stopwatch.reset();
        stopwatch.start();
        scripting.getInvocable().invokeFunction("wait", 1000);
        stopwatch.stop();
        assertThat((int)stopwatch.elapsed(TimeUnit.MILLISECONDS), is(greaterThanOrEqualTo(1000)));
    }

    @Test
    public void test_sendRequest() throws ScriptException, NoSuchMethodException {
        final HttpRequest httpRequest = new HttpRequest();
        httpRequest.setVersion(new HttpVersion(1, 2));
        scripting.getInvocable().invokeFunction("sendRequest", httpRequest);
    }

    @Test
    public void test_sendResponse() throws ScriptException, NoSuchMethodException {
        final HttpResponse httpResponse = new HttpResponse();
        httpResponse.setVersion(new HttpVersion(1, 2));
        scripting.getInvocable().invokeFunction("sendResponse", httpResponse);
    }

    @Test
    public void test_before() throws ScriptException, NoSuchMethodException {
        final HttpRequest httpRequest = new HttpRequest();
        httpRequest.setVersion(new HttpVersion(1, 2));
        scripting.before(httpRequest);
    }

    @Test
    public void test_logInfo() throws ScriptException, NoSuchMethodException {
        final HttpRequest httpRequest = new HttpRequest();
        httpRequest.setVersion(new HttpVersion(1, 2));
        scripting.getInvocable().invokeFunction("logRequest", httpRequest);
    }
}
