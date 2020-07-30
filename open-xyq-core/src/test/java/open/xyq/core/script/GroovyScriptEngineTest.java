package open.xyq.core.script;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class GroovyScriptEngineTest {

    private final GroovyScriptEngine scriptEngine = GroovyScriptEngine.INSTANCE;

    @Test
    void testLoadClass() {
        Object obj = scriptEngine.loadClass("GroovyScript");
        assertNotNull(obj);
        Object notExistObj = scriptEngine.loadClass("NotExistScript");
        assertNull(notExistObj);
    }
}