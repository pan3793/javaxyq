package open.xyq.core.fmt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WasDecoderTest {

    @Test
    public void testDecode() throws Exception {
        WasDecoder decoder = new WasDecoder();
        decoder.load("was/银两.tcp");
        assertEquals(1, decoder.getAnimCount());
        assertEquals(1, decoder.getFrameCount());
        decoder.getFrame(0);
        System.out.println(decoder.summary());
    }
}
