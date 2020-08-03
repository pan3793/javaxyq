package open.xyq.core.fmt.was;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import open.xyq.core.fmt.TintScheme;
import open.xyq.core.io.SeekByteArrayInputStream;
import open.xyq.core.util.IoUtil;
import open.xyq.core.util.StrUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * was(tcp/tca)解码器
 */
@Slf4j
public class WasDecoder {
    // 文件头标记
    static final String WAS_MAGIC_NUM = "SP";
    static final int TCP_HEADER_SIZE = 12;
    // 前2位
    static final int TYPE_ALPHA = 0x00;
    // 前3位 0010 0000
    static final int TYPE_ALPHA_PIXEL = 0x20;
    // 前3位
    static final int TYPE_ALPHA_REPEAT = 0x00;
    // 2进制前2位 1100 0000
    static final int TYPE_FLAG = 0xC0;
    // 以下前2位 0100 0000
    static final int TYPE_PIXELS = 0x40;
    // 1000 0000
    static final int TYPE_REPEAT = 0x80;
    // 1100 0000
    static final int TYPE_SKIP = 0xC0;

    // 文件头大小
    private int headerSize;
    @Getter // 包含动画个数
    private int animCount;
    @Getter // 动画的帧数
    private int frameCount;
    @Getter // Reference Pixel(悬挂点)
    private int refPixelX;
    @Getter
    private int refPixelY;
    @Getter // 精灵宽度
    private int width;
    @Getter // 精灵高度
    private int height;
    @Getter
    private int[] schemeIndexes;
    @Getter
    @Setter
    private Section[] sections;

    @Getter // 原始调色板
    private final short[] originPalette = new short[256];
    @Getter // 当前调色板
    private final short[] palette = new short[256];
    private final List<WasFrame> frames = new ArrayList<>();

    public String summary() {
        return "========== summary ==========\n" +
                "height: " + height + '\n' +
                "width: " + width + '\n' +
                "frame count: " + frameCount + '\n' +
                "anim count: " + animCount + '\n' +
                "=============================";
    }

    private SeekByteArrayInputStream randomIn;

    public void load(String filename) throws Exception {
        load(IoUtil.loadResource(filename).getInputStream());
    }

    public void load(File file) throws IllegalStateException, IOException {
        load(new FileInputStream(file));
    }

    public void load(InputStream in) throws IllegalStateException, IOException {
        randomIn = prepareInputStream(in);

        // was 信息
        headerSize = randomIn.readUnsignedShort();
        animCount = randomIn.readUnsignedShort();
        frameCount = randomIn.readUnsignedShort();
        width = randomIn.readUnsignedShort();
        height = randomIn.readUnsignedShort();
        refPixelX = randomIn.readUnsignedShort();
        refPixelY = randomIn.readUnsignedShort();

        // 读取帧延时信息
        int len = headerSize - TCP_HEADER_SIZE;
        if (len < 0) {
            throw new IllegalStateException("帧延时信息错误: " + len);
        }
        int[] delays = new int[len];
        for (int i = 0; i < len; i++) {
            delays[i] = randomIn.read();
        }

        // 读取调色板
        randomIn.seek(headerSize + 4);
        for (int i = 0; i < 256; i++) {
            originPalette[i] = randomIn.readUnsignedShort();
        }
        // 复制调色板
        System.arraycopy(originPalette, 0, palette, 0, 256);

        // 帧偏移列表
        int[] frameOffsets = new int[animCount * frameCount];
        randomIn.seek(headerSize + 4 + 512);
        for (int i = 0; i < animCount; i++) {
            for (int n = 0; n < frameCount; n++) {
                frameOffsets[i * frameCount + n] = randomIn.readInt();
            }
        }
        // 帧信息
        int frameX, frameY, frameWidth, frameHeight;
        for (int i = 0; i < animCount; i++) {
            for (int n = 0; n < frameCount; n++) {
                int offset = frameOffsets[i * frameCount + n];
                if (offset == 0)
                    continue; // blank frame
                randomIn.seek(offset + headerSize + 4);
                frameX = randomIn.readInt();
                frameY = randomIn.readInt();
                frameWidth = randomIn.readInt();
                frameHeight = randomIn.readInt();
                // 行像素数据偏移
                int[] lineOffsets = new int[frameHeight];
                for (int l = 0; l < frameHeight; l++) {
                    lineOffsets[l] = randomIn.readInt();
                }
                // 创建帧对象
                int delay = 1;
                if (i < delays.length) {
                    delay = delays[i];
                }
                WasFrame frame = new WasFrame(frameX, frameY, frameWidth, frameHeight, delay, offset, lineOffsets);
                this.frames.add(frame);
            }
        }
    }

    @SneakyThrows
    public void loadTintProfile(String filename) {
        InputStream is = IoUtil.loadResource(filename).getInputStream();
        Scanner scanner = new Scanner(is);
        scanner.useDelimiter("(\r\n)|(\n\r)|[\n\r=]");
        // section
        String strLine = scanner.next();
        String[] values = strLine.split(" ");// StringUtils.split(strLine);
        int sectionCount = Integer.parseInt(values[0]);
        // section 区间
        int[] sectionBounds = new int[sectionCount + 1];
        for (int i = 0; i < sectionBounds.length; i++)
            sectionBounds[i] = Integer.parseInt(values[i + 1]);
        // create section
        Section[] sections = new Section[sectionCount];
        for (int i = 0; i < sections.length; i++) {
            Section section = new Section(sectionBounds[i], sectionBounds[i + 1]);
            int schemeCount = Integer.parseInt(scanner.next());
            for (int s = 0; s < schemeCount; s++) {
                String[] strSchemes = new String[3];
                strSchemes[0] = scanner.next();
                strSchemes[1] = scanner.next();
                strSchemes[2] = scanner.next();
                TintScheme scheme = new TintScheme(strSchemes);
                section.addScheme(scheme);
            }
            sections[i] = section;
        }
        setSections(sections);
    }

    /**
     * 设置着色方案
     */
    public void tint(int[] schemeIndexes) {
        for (int i = 0; i < schemeIndexes.length; i++) {
            this.tint(i, schemeIndexes[i]);
        }
        this.schemeIndexes = schemeIndexes;
    }

    /**
     * 修改某个区段的着色
     */
    public void tint(int sectionIndex, int schemeIndex) {
        if (this.sections == null)
            return;
        Section section = this.sections[sectionIndex];
        TintScheme scheme = section.getScheme(schemeIndex);
        for (int i = section.getStart(); i < section.getEnd(); i++)
            this.palette[i] = scheme.mix(this.originPalette[i]);
    }

    public int getSectionCount() {
        return this.sections.length;
    }

    public int getSchemeCount(int section) {
        return this.sections[section].getSchemeCount();
    }

    /**
     * 将图像数据画到Image上
     */
    public void draw(int[] pixels, WritableRaster raster, int x, int y, int w, int h) {
        int[] iArray = new int[4];
        for (int y1 = 0; y1 < h && y1 + y < height; y1++) {
            for (int x1 = 0; x1 < w && x1 + x < width; x1++) {
                iArray[0] = (pixels[y1 * w + x1] >>> 11 & 0x1F) << 3; // red   5
                iArray[1] = (pixels[y1 * w + x1] >>> 5 & 0x3f) << 2;  // green 6
                iArray[2] = (pixels[y1 * w + x1] & 0x1F) << 3;        // blue  5
                iArray[3] = (pixels[y1 * w + x1] >>> 16 & 0x1f) << 3; // alpha 5
                try {
                    raster.setPixel(x1 + x, y1 + y, iArray);
                } catch (Exception e) {
                    log.warn("{}: x={}, y={}, pixel=[{},{},{},{}]", e.getMessage(),
                            x1 + x, y1 + y, iArray[0], iArray[1], iArray[2], iArray[3]);
                }
            }
        }
    }

    private int[] convert(int[] pixels) {
        int[] data = new int[pixels.length * 4];
        for (int i = 0; i < pixels.length; i++) {
            data[i * 4] = ((pixels[i] >>> 11) & 0x1F) << 3;     // red   5
            data[i * 4 + 1] = ((pixels[i] >>> 5) & 0x3f) << 2;  // green 6
            data[i * 4 + 2] = (pixels[i] & 0x1F) << 3;          // blue  5
            data[i * 4 + 3] = ((pixels[i] >>> 16) & 0x1f) << 3; // alpha 5
        }
        return data;
    }

    public BufferedImage getFrame(int index) {
        WasFrame frame = this.frames.get(index);
        if (frame.getPixels() == null)
            frame.setPixels(parse(frame));
        // 修正单帧动画的偏移问题
        if (this.frameCount == 1)
            return createImage(refPixelX, refPixelY, frame.getWidth(), frame.getHeight(), frame.getPixels());
        else
            return createImage(frame.getX(), frame.getY(), frame.getWidth(), frame.getHeight(), frame.getPixels());
    }

    /**
     * parse frame every time
     */
    public BufferedImage getFrameImage(int index) {
        WasFrame frame = this.frames.get(index);
        if (this.frameCount == 1) {
            return createImage(refPixelX, refPixelY, frame.getWidth(), frame.getHeight(), parse(frame));
        } else {
            return createImage(frame.getX(), frame.getY(), frame.getWidth(), frame.getHeight(), parse(frame));
        }
    }

    private int[] parse(WasFrame frame) {
        return this.parse(randomIn, frame.getFrameOffset(), frame.getLineOffsets(), frame.getWidth(), frame.getHeight());
    }

    public List<Image> getFrames() {
        // TODO
        return null;
    }

    public int getDelay(int index) {
        return this.frames.get(index).getDelay();
    }

    public BufferedImage createImage(int x, int y, int frameWidth, int frameHeight, int[] pixels) {
        // use sprite's width & height
        BufferedImage image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
        draw(pixels, image.getRaster(), refPixelX - x, refPixelY - y, frameWidth, frameHeight);
        return image;
    }


    /**
     * 1. ensure MAGIC_NUM is SP
     * 2. copy data to byte[] to construct SeekByteArrayInputStream
     */
    private SeekByteArrayInputStream prepareInputStream(InputStream in) throws IOException {
        byte[] buf = new byte[2];
        in.mark(2);
        int l = in.read(buf, 0, 2);
        if (l < 2 || !WAS_MAGIC_NUM.equals(new String(buf))) {
            throw new IllegalStateException("文件头标志错误: " + StrUtil.prettyBytes(buf));
        }
        SeekByteArrayInputStream randomIn;
        if (in instanceof SeekByteArrayInputStream) {
            in.reset();
            randomIn = (SeekByteArrayInputStream) in;
        } else {
            byte[] buf2 = new byte[buf.length + in.available()];
            System.arraycopy(buf, 0, buf2, 0, buf.length);
            int a, count = buf.length;
            while (in.available() > 0) {
                a = in.read(buf2, count, in.available());
                count += a;
            }
            // construct a new seek stream
            randomIn = new SeekByteArrayInputStream(buf2);
        }
        // skip header
        randomIn.seek(2);
        return randomIn;
    }

    private int[] parse(SeekByteArrayInputStream in,
                        int frameOffset,
                        int[] lineOffsets,
                        int frameWidth,
                        int frameHeight) {
        int[] pixels = new int[frameHeight * frameWidth];
        int b, x, c;
        int index;
        int count;
        for (int y = 0; y < frameHeight; y++) {
            x = 0;
            in.seek(lineOffsets[y] + frameOffset + headerSize + 4);
            while (x < frameWidth) {
                b = in.read();
                switch ((b & TYPE_FLAG)) {
                    case TYPE_ALPHA:
                        if ((b & TYPE_ALPHA_PIXEL) > 0) {
                            index = in.read();
                            c = palette[index];
                            pixels[y * frameWidth + x++] = c + ((b & 0x1F) << 16);
                        } else if (b != 0) {  // ???
                            count = b & 0x1F; // count
                            b = in.read();    // alpha
                            index = in.read();
                            c = palette[index];
                            for (int i = 0; i < count; i++)
                                pixels[y * frameWidth + x++] = c + ((b & 0x1F) << 16);
                        } else { // block end
                            if (x != 0)
                                x = frameWidth;
                        }
                        break;
                    case TYPE_PIXELS:
                        count = b & 0x3F;
                        for (int i = 0; i < count; i++) {
                            index = in.read();
                            pixels[y * frameWidth + x++] = palette[index] + (0x1F << 16);
                        }
                        break;
                    case TYPE_REPEAT:
                        count = b & 0x3F;
                        index = in.read();
                        c = palette[index];
                        for (int i = 0; i < count; i++)
                            pixels[y * frameWidth + x++] = c + (0x1F << 16);
                        break;
                    case TYPE_SKIP:
                        count = b & 0x3F;
                        x += count;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + (b & TYPE_FLAG));
                }
            }
            if (x > frameWidth)
                log.error("block end error: [{}][{}/{}]", y, x, frameWidth);
        }
        return pixels;
    }

    public void resetPalette() {
        System.arraycopy(originPalette, 0, palette, 0, 256);
    }

}
