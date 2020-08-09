/*
 * JavaXYQ Engine
 *
 * javaxyq@2008 all rights.
 * http://www.javaxyq.com
 */

package open.xyq.core.fmt.map;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import open.xyq.core.MapProvider;
import open.xyq.core.ui.TileMap;
import open.xyq.core.cfg.MapConfig;
import open.xyq.core.alg.MiniLZO;
import open.xyq.core.io.RichRandomAccessFile;
import open.xyq.core.util.IoUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * @author 龚德伟
 * @history 2008-05-29 龚德伟 新建
 * 2013-12-26 wpaul modify
 */
@Slf4j
public class TileMapProvider implements MapProvider {

    private static final String MAGIC_HEADER_MAP = "0.1M";
    private static final String MAGIC_HEADER_JPEG = "GEPJ";

    private RichRandomAccessFile raf;

    @Getter // 地图宽度
    private int width;
    @Getter // 地图高度
    private int height;
    @Getter // 地图水平方向块数
    private int hBlockCount;
    @Getter // 地图垂直方向块数
    private int vBlockCount;
    @Getter // mask数量
    private int maskCount;
    // 地图块入口地址偏移表
    private int[][] blockOffsetTable;
    // mask偏移量
    private int[] maskOffsets;

    public TileMapProvider() {
    }

    @SneakyThrows
    public TileMapProvider(File file) {
        raf = new RichRandomAccessFile(file, "r");
        decodeMetadata();
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String getDescription() {
        return "default tile map provider for XYQ";
    }

    @Override
    public TileMap getResource(String sceneId) {
        if (raf != null) // 场景切换，释放资源
            dispose();
        try {
            String path = String.format("scene/%s.map", sceneId);
            String music = String.format("music/%s.mp3", sceneId);
            MapConfig cfg = new MapConfig(sceneId, "scene_name", path, music); // TODO
            raf = new RichRandomAccessFile(IoUtil.loadFile(cfg.getPath()), "r");
            decodeMetadata();
            return new TileMap(this, cfg);
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    @Override
    public void dispose() {
        try {
            raf.close();
        } catch (IOException e) {
            log.debug("", e);
        }
        blockOffsetTable = null;
    }

    public synchronized MapUnit readUnit(int h, int v) {
        try {
            raf.seek(blockOffsetTable[h][v]);
            int maskNum = raf.readInt2();
            int[] maskList = new int[maskNum];
            if (maskNum > 0)
                for (int i = 0; i < maskNum; i++)
                    maskList[i] = raf.readInt2();

            byte[] cellData = new byte[0];
            boolean loop = true;
            do {
                String unitHead = raf.readUtf8(4);
                int headSize = raf.readInt2();
                switch (unitHead) {
                    case "GAMI":
                    case "KSAM":
                    case "KOLB":
                        break;
                    case "LLEC":
                        cellData = new byte[headSize];
                        raf.read(cellData);
                        break;
                    case "GEPJ":
                        raf.skipBytes(headSize);
                        break;
                    case "GIRB": // 光亮规则
                        raf.skipBytes(headSize);
                        loop = false;
                        break;
                }
            } while (loop);
            return new MapUnit(maskList, cellData);
        } catch (IOException e) {
            log.error("", e);
            return null;
        }
    }

    public synchronized MaskUnit readMask(int unitNum) {
        try {
            raf.seek(maskOffsets[unitNum]);

            int maskX = raf.readInt2();
            int maskY = raf.readInt2();
            int maskWidth = raf.readInt2();
            int maskHeight = raf.readInt2();
            int maskSize = raf.readInt2();

            // 压缩后的 mask 数据
            byte[] compressedMaskData = new byte[maskSize];
            raf.read(compressedMaskData);

            // 解压 mask 数据
            int paddedMaskWidth = (int) Math.ceil(maskWidth / 4.0) * 4;                // 以4对齐的宽度
            byte[] decompressedMaskData = new byte[paddedMaskWidth * maskHeight / 4];  // 1个字节4个像素，故要除以4
            int decompressedMaskSize = MiniLZO.lzo1x_decompress(compressedMaskData, decompressedMaskData);

            // 还原 mask 数据
            int[] maskData = new int[maskWidth * maskHeight];
            int ow = paddedMaskWidth - maskWidth;
            int md = 0;
            for (int i = 0; i < decompressedMaskSize; i++) {
                int border = ((i + 1) * 4) % paddedMaskWidth == 0 ? 4 - ow : Math.min(maskWidth, 4);
                for (int j = 0; j < border; j++)
                    maskData[md++] = uInt2(decompressedMaskData[i], j * 2);
            }
            return new MaskUnit(maskX, maskY, maskWidth, maskHeight, maskData);
        } catch (Exception e) {
            log.error("read map mask error", e);
        }
        return null;
    }

    @SneakyThrows
    public BufferedImage getBlockImage(int h, int v) {
        byte[] data = this.readJpegData(h, v);
        return ImageIO.read(new ByteArrayInputStream(data));
    }

    public synchronized byte[] readJpegData(int h, int v) {
        try {
            raf.seek(blockOffsetTable[h][v]);
            if (!checkJpegMagic())
                throw new RuntimeException("不是合法的JPEG文件");

            int len = raf.readInt2();
            byte[] encodedJpegBuf = new byte[len];
            raf.readFully(encodedJpegBuf);
            return decodeJpeg(encodedJpegBuf);
        } catch (Exception e) {
            log.error("获取JPEG数据块失败：{}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
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
            for (int v = 0; v < vBlockCount; v++)
                for (int h = 0; h < hBlockCount; h++)
                    blockOffsetTable[h][v] = raf.readInt2();
            raf.skipBytes(4); // head_size, no needed
            maskCount = raf.readInt2();
            maskOffsets = new int[maskCount];
            for (int i = 0; i < maskCount; i++)
                maskOffsets[i] = raf.readInt2();
        } catch (Exception e) {
            throw new IllegalArgumentException("地图文件解码失败", e);
        }
    }

    private byte[] decodeJpeg(byte[] encodedJpegBuf) {
        // it should be almost enough
        ByteArrayOutputStream decodedJpegOs = new ByteArrayOutputStream(encodedJpegBuf.length + 128);
        // copy 0xFFA0
        decodedJpegOs.write(encodedJpegBuf, 0, 2);
        int current, last;
        boolean needPadding = false; // padding means 0xFF->0xFF00
        for (current = 4, last = 4; current < encodedJpegBuf.length - 2; current++) {
            if (!needPadding &&
                    encodedJpegBuf[current] == (byte) 0xFF && encodedJpegBuf[++current] == (byte) 0xDA) {
                // 0xFFDA ; SOS: Start Of Scan;
                // Suppose always like this: FF DA 00 09 03...
                assert encodedJpegBuf[current + 1] == (byte) 0x00;
                assert encodedJpegBuf[current + 2] == (byte) 0x09;
                assert encodedJpegBuf[current + 3] == (byte) 0x03;
                needPadding = true;
                encodedJpegBuf[current + 2] = 0x0C;
                decodedJpegOs.write(encodedJpegBuf, last, current + 10 - last);
                // filled 00 3F 00
                decodedJpegOs.write(0x00);
                decodedJpegOs.write(0x3F);
                decodedJpegOs.write(0x00);
                last = current + 10;
                current += 9;
            }
            if (needPadding && encodedJpegBuf[current] == (byte) 0xFF) {
                decodedJpegOs.write(encodedJpegBuf, last, current + 1 - last);
                decodedJpegOs.write(0x00);
                last = current + 1;
            }
        }
        decodedJpegOs.write(encodedJpegBuf, last, encodedJpegBuf.length - last);
        return decodedJpegOs.toByteArray();
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
            String magicHeader = new String(buf);
            if (magicHeader.equals("2GPJ")) {
                log.warn("don't support 2GPJ map");
                return false;
            }
            return magicHeader.equals(MAGIC_HEADER_JPEG);
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
        return false;
    }

    // 构造小端 u_int2
    private int uInt2(byte mask, int bitOffset) {
        return mask & (0x03 << bitOffset) >> bitOffset;
    }
}
