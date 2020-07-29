package open.xyq.core.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MathUtilTest {

    @Test
    void testMd5() {
        assertEquals("900150983cd24fb0d6963f7d28e17f72", MathUtil.md5("abc"));
        assertEquals("de30f03b6683e62aff82ff43eb1e5a68", MathUtil.md5("小白"));
    }
}