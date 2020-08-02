package open.xyq.core.fmt.map;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import open.xyq.core.io.RichRandomAccessFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@Slf4j
@Deprecated
public class MapDecoder {
    private static final String MAGIC_HEADER_MAP = "0.1M";
    private static final String MAGIC_HEADER_JPEG = "GEPJ";

    @Getter
    private final String filename;
    private final RichRandomAccessFile raf;

    // ===== metadata start =====
    @Getter
    private int width;
    @Getter
    private int height;
    @Getter
    private int hBlockCount;
    @Getter
    private int vBlockCount;
    private int[][] blockOffsetTable;
    // ====== metadata end ======

    public MapDecoder(String filename) throws Exception {
        this(new File(filename));
    }

    public MapDecoder(File file) throws Exception {
        this.filename = file.getName();
        raf = new RichRandomAccessFile(file, "r");
        decodeMetadata();
    }

    private void decodeMetadata() {
        if (!checkMapMagic())
            throw new IllegalArgumentException("无效的地图格式文件");

        try {
            width = raf.readInt2();
            height = raf.readInt2();
            hBlockCount = (int) Math.ceil(width / 320.0);
            vBlockCount = (int) Math.ceil(height / 240.0);
            blockOffsetTable = new int[hBlockCount][vBlockCount];
            for (int v = 0; v < vBlockCount; v++) {
                for (int h = 0; h < hBlockCount; h++) {
                    blockOffsetTable[h][v] = raf.readInt2();
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("地图文件" + filename + "解码失败", e);
        }
    }

    /**
     * 获取指定的JPEG数据块
     *
     * @param h 行
     * @param v 列
     */
    public synchronized byte[] getJpegData(int h, int v) {
        try {
            raf.seek(blockOffsetTable[h][v]);
            if (!checkJpegMagic())
                throw new RuntimeException("不是合法的JPEG文件");

            int len = raf.readInt2();
            byte[] jpegBuf = new byte[len];
            raf.readFully(jpegBuf);

            // modify jpeg data
            ByteArrayOutputStream bos = new ByteArrayOutputStream(4096);
            bos.reset();
            // skip 2 bytes: FF A0
            bos.write(jpegBuf, 0, 2);
            int p, start;
            boolean isFilled = false; // 是否0xFF->0xFF 0x00
            for (p = 4, start = 4; p < jpegBuf.length - 2; p++) {
                if (!isFilled && jpegBuf[p] == (byte) 0xFF && jpegBuf[++p] == (byte) 0xDA) {
                    isFilled = true;
                    // 0xFF 0xDA ; SOS: Start Of Scan
                    // ch=jpegBuf[p+3];
                    // suppose always like this: FF DA 00 09 03...
                    jpegBuf[p + 2] = 12;
                    bos.write(jpegBuf, start, p + 10 - start);
                    // filled 00 3F 00
                    bos.write(0x00);
                    bos.write(0x3F);
                    bos.write(0x00);
                    start = p + 10;
                    p += 9;
                }
                if (isFilled && jpegBuf[p] == (byte) 0xFF) {
                    bos.write(jpegBuf, start, p + 1 - start);
                    bos.write(0x00);
                    start = p + 1;
                }
            }
            bos.write(jpegBuf, start, jpegBuf.length - start);
            return bos.toByteArray();
        } catch (Exception e) {
            log.error("获取JPEG数据块失败：{}", e.getMessage(), e);
        }
        return null;
    }

    private boolean checkMapMagic() {
        byte[] buf = new byte[MAGIC_HEADER_MAP.length()];
        try {
            raf.read(buf);
            String str = new String(buf);
            return str.equals(MAGIC_HEADER_MAP);
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
        return false;
    }

    private boolean checkJpegMagic() {
        byte[] buf = new byte[MAGIC_HEADER_JPEG.length()];
        try {
            int len = raf.read();
            raf.skipBytes(3 + len * 4);
            raf.read(buf);
            String str = new String(buf);
            return str.equals(MAGIC_HEADER_JPEG);
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
        return false;
    }
}
