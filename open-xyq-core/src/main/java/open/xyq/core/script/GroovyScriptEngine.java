package open.xyq.core.script;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import lombok.extern.slf4j.Slf4j;
import open.xyq.core.util.IoUtil;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileNotFoundException;

@Slf4j
public class GroovyScriptEngine implements ScriptEngine {

    public static final GroovyScriptEngine INSTANCE = new GroovyScriptEngine("classpath:script");

    private final GroovyClassLoader groovyCl;

    private final String scriptDir;

    public GroovyScriptEngine(String dir) {
        groovyCl = new GroovyClassLoader(this.getClass().getClassLoader());
        scriptDir = dir;
    }

    @Nullable
    @Override
    public Object loadClass(String className) {
        try {
            File src;
            try {
                src = IoUtil.loadFile(scriptDir + "/" + className.replace(".", "/") + ".groovy");
            } catch (Exception e) {
                src = IoUtil.loadFile(scriptDir + "/" + className.replace(".", "/") + ".java");
            }
            Class<?> groovyClass = groovyCl.parseClass(new GroovyCodeSource(src, "UTF-8"));
            return groovyClass.newInstance();
        } catch (Exception e) {
            log.error("load groovy script failed.", e);
            return null;
        }
    }
}
