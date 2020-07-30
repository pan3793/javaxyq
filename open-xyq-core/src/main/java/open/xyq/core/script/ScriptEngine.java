package open.xyq.core.script;

import javax.annotation.Nullable;

public interface ScriptEngine {

    @Nullable
    @SuppressWarnings("unchecked")
    default <T> T loadClass(String className, Class<T> clazz) {
        return ((T) loadClass(className));
    }

    @Nullable
    Object loadClass(String className);
}
