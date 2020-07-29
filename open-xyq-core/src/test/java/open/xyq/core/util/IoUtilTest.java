package open.xyq.core.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IoUtilTest {
    @Test
    public void test() {
        System.out.println(IoUtil.loadFile("classpath:script").getAbsolutePath());
    }
}