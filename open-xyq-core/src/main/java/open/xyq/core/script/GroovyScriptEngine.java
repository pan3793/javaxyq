package open.xyq.core.script;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import lombok.extern.slf4j.Slf4j;
import open.xyq.core.util.IoUtil;

import java.io.File;

@Slf4j
public class GroovyScriptEngine implements ScriptEngine {

    public static final GroovyScriptEngine INSTANCE = new GroovyScriptEngine("classpath:script");

    private final GroovyClassLoader groovyCl;

    private final String scriptDir;

    public GroovyScriptEngine(String dir) {
        groovyCl = new GroovyClassLoader(this.getClass().getClassLoader());
        scriptDir = dir;
    }

    @Override
    public Object loadClass(String className) {
        try {
            File src = IoUtil.loadFile(scriptDir + "/" + className.replace(".", "/") + ".groovy");
            Class<?> groovyClass = groovyCl.parseClass(new GroovyCodeSource(src, "UTF-8"));
            return groovyClass.newInstance();
        } catch (Exception e) {
            log.error("load groovy script failed.", e);
            return null;
        }
    }
}
