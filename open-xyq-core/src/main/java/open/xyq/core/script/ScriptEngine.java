package open.xyq.core.script;

public interface ScriptEngine {

    <T> T loadClass(String className, Class<T> clazz) throws ClassNotFoundException;

    Object loadClass(String className) throws ClassNotFoundException;
}
