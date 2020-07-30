package open.xyq.core.util;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
public class SysUtil {

    public static void sleep(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            log.warn("interrupted");
        }
    }
}
