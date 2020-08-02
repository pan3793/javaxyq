package open.xyq.core;

import lombok.extern.slf4j.Slf4j;
import open.xyq.core.util.IoUtil;
import org.ini4j.Ini;
import org.ini4j.Profile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

@Slf4j
public class MiscTest {

    @Test
    public void ini() throws IOException {
        Ini ini = new Ini(IoUtil.loadFile("file://C:/Users/cheng.pan/open-xyq-resources/addon.ini"));
        assertEquals(1, ini.keySet().size());
        Profile.Section section = ini.get("Resource");
        log.info("{}", section.entrySet());
    }
}
