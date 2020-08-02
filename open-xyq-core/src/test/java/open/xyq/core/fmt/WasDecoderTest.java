package open.xyq.core.fmt;

import lombok.extern.slf4j.Slf4j;
import open.xyq.core.fmt.was.WasDecoder;
import open.xyq.core.util.UiUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class WasDecoderTest {

    @Test
    public void testDecode() throws Exception {
        WasDecoder decoder = new WasDecoder();
        decoder.load("was/银两.tcp");
        assertEquals(1, decoder.getAnimCount());
        assertEquals(1, decoder.getFrameCount());
        log.info(decoder.summary());
    }

    @Test
    @Disabled
    public void testLaunchImage() throws Exception  {
        WasDecoder decoder = new WasDecoder();
        decoder.load("was/银两.tcp");
        assertEquals(1, decoder.getAnimCount());
        assertEquals(1, decoder.getFrameCount());
        BufferedImage image = decoder.getFrame(0);
        UiUtil.launchImageAppBlocked(image);
    }

    @Test
    @Disabled
    public void testLaunchAnimation() throws Exception  {
        WasDecoder decoder = new WasDecoder();
        // 8个方向的跑动动画，每个动画8帧
        decoder.load("was/虎头怪_行走.was");
        assertEquals(8, decoder.getAnimCount());
        assertEquals(8, decoder.getFrameCount());
        BufferedImage image = decoder.getFrame(63);
        // UiUtil.launchAnimationAppBlocked(image);
    }
}
