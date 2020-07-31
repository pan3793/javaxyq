package open.xyq.core.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StrUtilTest {

    @Test
    void testPrettyBytes() {
        byte[] bs = {0x0F, 0x1F, 0x2F, 0x3F, 0x4F, 0x5F, 0x6F};
        System.out.println(StrUtil.prettyBytes(bs));
    }
}