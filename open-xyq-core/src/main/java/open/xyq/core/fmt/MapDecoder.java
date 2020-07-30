package open.xyq.core.fmt;

import lombok.Getter;
import open.xyq.core.io.RichRandomAccessFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class MapDecoder {
    private static final String MAP_MAGIC_NUMBER = "0.1M";
    private static final String JPEG_MAGIC_NUM = "GEPJ";

    @Getter
    private int width;
    @Getter
    private int height;
    @Getter
    private String filename;

    private int[][] segmentsOffset;

    private Object[][] jpegDatas;

    private RichRandomAccessFile mapFile;

    @Getter
    private int horSegmentCount;
    @Getter
    private int verSegmentCount;

    public MapDecoder(String filename) throws Exception {
        this(new File(filename));
    }

    public MapDecoder(File file) throws Exception {
        this.filename = file.getName();
        mapFile = new RichRandomAccessFile(file, "r");
        loadHeader();
    }

    /**
     * 从流加载MAP
     */
    private void loadHeader() {
        if (!isValidMapFile()) {
            throw new IllegalArgumentException("非梦幻地图格式文件!");
        }
        try {
            // start decoding
            width = mapFile.readInt2();
            height = mapFile.readInt2();
            horSegmentCount = (int) Math.ceil(width / 320.0);
            verSegmentCount = (int) Math.ceil(height / 240.0);

            // System.out.println("size: " + width + "*" + height);
            // System.out.println("segment: " + horSegmentCount + "*" + horSegmentCount);

            segmentsOffset = new int[horSegmentCount][verSegmentCount];
            jpegDatas = new Object[horSegmentCount][verSegmentCount];
            for (int v = 0; v < verSegmentCount; v++) {
                for (int h = 0; h < horSegmentCount; h++) {
                    segmentsOffset[h][v] = mapFile.readInt2();
                }
            }
            // int headerSize = sis.readInt2();// where need it?
        } catch (Exception e) {
            throw new IllegalArgumentException("地图解码失败:" + e.getMessage());
            // e.printStackTrace();
        }
    }

    /**
     * 获取指定的JPEG数据块
     *
     * @param h 行
     * @param v 列
     */
    public byte[] getJpegData(int h, int v) {
        try {
            // read jpeg data
            int len;
            byte jpegBuf[];
            mapFile.seek(segmentsOffset[h][v]);// XXX offset
            if (isJPEGData()) {
                len = mapFile.readInt2();
                jpegBuf = new byte[len];
                mapFile.readFully(jpegBuf);
                jpegDatas[h][v] = jpegBuf;
            }

            // modify jpeg data
            ByteArrayOutputStream bos = new ByteArrayOutputStream(4096);
            boolean isFilled = false;// 是否0xFF->0xFF 0x00
            bos.reset();
            jpegBuf = (byte[]) jpegDatas[h][v];
            bos.write(jpegBuf, 0, 2);
            // skip 2 bytes: FF A0
            int p, start;
            isFilled = false;
            for (p = 4, start = 4; p < jpegBuf.length - 2; p++) {
                if (!isFilled && jpegBuf[p] == (byte) 0xFF && jpegBuf[++p] == (byte) 0xDA) {
                    isFilled = true;
                    // 0xFF 0xDA ; SOS: Start Of Scan
                    // ch=jpegBuf[p+3];
                    // suppose always like this: FF DA 00 09 03...
                    jpegBuf[p + 2] = 12;
                    bos.write(jpegBuf, start, p + 10 - start);
                    // filled 00 3F 00
                    bos.write(0);
                    bos.write(0x3F);
                    bos.write(0);
                    start = p + 10;
                    p += 9;
                }
                if (isFilled && jpegBuf[p] == (byte) 0xFF) {
                    bos.write(jpegBuf, start, p + 1 - start);
                    bos.write(0);
                    start = p + 1;
                }
            }
            bos.write(jpegBuf, start, jpegBuf.length - start);
            jpegDatas[h][v] = bos.toByteArray();
        } catch (Exception e) {
            System.err.println("获取JPEG 数据块失败：" + e.getMessage());
        }
        return (byte[]) jpegDatas[h][v];

    }

    private boolean isJPEGData() {
        byte[] buf = new byte[4];
        try {
            int len = mapFile.read();
            mapFile.skipBytes(3 + len * 4);
            mapFile.read(buf);// 47 45 50 4A; GEPJ
            String str = new String(buf);
            return str.equals(JPEG_MAGIC_NUM);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private boolean isValidMapFile() {
        byte[] buf = new byte[4];
        try {
            mapFile.read(buf);
            String str = new String(buf);
            return str.equals(MAP_MAGIC_NUMBER);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
