package open.xyq.core.util;

import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class IoUtil {

    public static String EOL = System.getProperty("line.separator");
    public static String SEP = File.separator;

    public static PathMatchingResourcePatternResolver RESOURCE_RESOLVER = new PathMatchingResourcePatternResolver();

    // Support pattern:
    //     file:/absolute_path/filename.ext
    //     file:relative_path/filename.ext
    //     classpath:path/filename.ext
    private static Resource loadResource(String path) {
        return RESOURCE_RESOLVER.getResource(path);
    }

    // Support pattern:
    //     file:/absolute_path/**/file*.ext
    //     file:relative_path/file*.ext
    //     classpath:**/file*.ext
    @SneakyThrows
    private static Resource[] loadResources(String path) {
        return RESOURCE_RESOLVER.getResources(path);
    }

    @SneakyThrows
    public static String readTextFromLocalFs(String path) {
        return readText(new FileInputStream(path));
    }

    public static String readTextFromClasspath(String path) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null)
            classLoader = IoUtil.class.getClassLoader();
        return readText(classLoader.getResourceAsStream(path));
    }

    public static String readText(InputStream is) {
        return new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)).lines().collect(Collectors.joining(EOL));
    }

    @SneakyThrows
    public static List<String> readLinesFromLocalFs(String path) {
        return readLines(new FileInputStream(path));
    }

    public static List<String> readLinesFromClasspath(String path) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null)
            classLoader = IoUtil.class.getClassLoader();
        return readLines(classLoader.getResourceAsStream(path));
    }

    public static List<String> readLines(InputStream is) {
        return new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
    }

    @SneakyThrows
    public static File loadFile(String path) {
        return loadResource(path).getFile();
    }

    @SneakyThrows
    public static File[] loadFiles(String path) {
        Resource[] resources = loadResources(path);
        File[] files = new File[resources.length];
        for (int i = 0; i < resources.length; i++) {
            files[i] = resources[i].getFile();
        }
        return files;
    }
}
