package org.apache.log4j;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author shing19m
 */
public class LogManagerTest {

    @Test(timeout = 100)
    public void test_getLogger() {
        Logger scriptLogger = Logger.getLogger("com.foo");
        assertNotNull(scriptLogger);
    }
}
