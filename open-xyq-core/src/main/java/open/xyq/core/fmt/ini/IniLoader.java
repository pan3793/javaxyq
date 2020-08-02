package open.xyq.core.fmt.ini;

import lombok.SneakyThrows;
import open.xyq.core.util.IoUtil;
import org.ini4j.Ini;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class IniLoader {

    public static Map<String, Map<String, String>> load(String filePath) {
        return load(IoUtil.loadFile(filePath));
    }

    @SneakyThrows
    public static Map<String, Map<String, String>> load(File file) {
        return load(new FileInputStream(file));
    }

    @SneakyThrows
    public static Map<String, Map<String, String>> load(InputStream is) {
        return new HashMap<>(new Ini(is));
    }
}
