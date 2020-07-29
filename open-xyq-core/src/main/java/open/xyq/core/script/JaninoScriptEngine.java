package open.xyq.core.script;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.janino.JavaSourceClassLoader;

import java.io.File;

@Slf4j
public class JaninoScriptEngine implements ScriptEngine {

    private final ClassLoader cl;

    public JaninoScriptEngine(File dir) {
        cl = new JavaSourceClassLoader(
                this.getClass().getClassLoader(), new File[]{dir}, "UTF-8");
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T loadClass(String className, Class<T> clazz) throws ClassNotFoundException {
        return ((T) loadClass(className));
    }

    @Override
    public Object loadClass(String className) throws ClassNotFoundException {
        try {
            return cl.loadClass(className).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
