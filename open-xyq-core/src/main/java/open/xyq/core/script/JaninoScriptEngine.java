package open.xyq.core.script;

import lombok.extern.slf4j.Slf4j;
import open.xyq.core.util.IoUtil;
import org.codehaus.janino.JavaSourceClassLoader;

import javax.annotation.Nullable;
import java.io.File;

@Slf4j
public class JaninoScriptEngine implements ScriptEngine {

    public static final JaninoScriptEngine INSTANCE = new JaninoScriptEngine(IoUtil.loadFile("classpath:script"));

    private final ClassLoader cl;

    public JaninoScriptEngine(File dir) {
        cl = new JavaSourceClassLoader(
                this.getClass().getClassLoader(), new File[]{dir}, "UTF-8");
    }

    @Nullable
    @Override
    public Object loadClass(String className) {
        try {
            return cl.loadClass(className).newInstance();
        } catch (Exception e) {
            log.error("load java script failed.", e);
            return null;
        }
    }
}
