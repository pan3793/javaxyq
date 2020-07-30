package open.xyq.core.script;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JaninoScriptEngineTest {

    private final JaninoScriptEngine scriptEngine = JaninoScriptEngine.INSTANCE;

    @Test
    void testLoadClass() {
        Object obj = scriptEngine.loadClass("JavaScript");
        assertNotNull(obj);
        Object notExistObj = scriptEngine.loadClass("NotExistScript");
        assertNull(notExistObj);
    }
}