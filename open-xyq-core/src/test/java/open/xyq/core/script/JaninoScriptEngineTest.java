package open.xyq.core.script;

import open.xyq.core.util.IoUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JaninoScriptEngineTest {

    @Test
    void testLoadClass() throws ClassNotFoundException {
        JaninoScriptEngine scriptEngine = new JaninoScriptEngine(IoUtil.loadFile("classpath:script"));
        Object env = scriptEngine.loadClass("Env");
        System.out.println(env);
    }
}