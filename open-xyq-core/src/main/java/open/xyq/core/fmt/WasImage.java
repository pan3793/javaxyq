package open.xyq.core.fmt;

import open.xyq.core.io.SeekByteArrayInputStream;
import open.xyq.core.util.IoUtil;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class WasImage {
    private static final String WAS_FILE_TAG = "SP";
    private static final int WAS_IMAGE_HEADER_SIZE = 12;
    // 前2位
    private static final int TYPE_ALPHA = 0x00;
    // 前3位 0010 0000
    private static final int TYPE_ALPHA_PIXEL = 0x20;
    // 前3位
    private static final int TYPE_ALPHA_REPEAT = 0x00;
    // 2进制前2位 1100 0000
    private static final int TYPE_FLAG = 0xC0;
    // 以下前2位 0100 0000
    private static final int TYPE_PIXELS = 0x40;
    // 1000 0000
    private static final int TYPE_REPEAT = 0x80;
    // 1100 0000
    private static final int TYPE_SKIP = 0xC0;

    private SeekByteArrayInputStream in;

    public int centerX;

    public int centerY;

    int[] delayLine;

    public int frameCount;

    public WasFrame[][] frames;

    public int height;

    short imageHeaderSize;

    String name;

    short[] palette;

    String path;

    public int spriteCount;

    public int width;

    public WasImage() {
        palette = new short[256];
    }

    public String getName() {
        return name;
    }

    public Dimension getSize() {
        return new Dimension(width, height);
    }

    /**
     * 获取某帧的延时帧数
     */
    public int getFrameDelay(int index) {
        return delayLine[index];
    }

    /**
     * 文件标志 2字节， SP 53 50 / SP<br>
     * 文件头大小 2字节， 不包含前四个字节 0C 00 / 12<br>
     * 动画方向数 2字节 01 00 / 1<br>
     * 每方向的帧数 2字节 01 00 / 1<br>
     * 动画的宽度 2字节 80 02 / 640<br>
     * 动画的高度 2字节 29 00 / 41<br>
     * 动画的中心点 X 2字节， 有符号 00 00 / 0<br>
     * 动画的中心点 Y 2字节, 有符号 00 00 / 0<br>
     */
    public synchronized void loadWAS(String filename) {
        InputStream fileIn = null;
        try {
            File file = new File(filename);
            path = file.getAbsolutePath();
            name = file.getName();
            fileIn = IoUtil.loadResource(filename).getInputStream();
            byte[] buf = new byte[fileIn.available()];
            int a = 0, count = 0;
            while (fileIn.available() > 0) {
                a = fileIn.read(buf, count, fileIn.available());
                count += a;
            }

            // construct a new seekable stream
            in = new SeekByteArrayInputStream(buf);
            buf = new byte[2];
            in.read(buf, 0, 2);
            String flag = new String(buf, 0, 2);
            if (!WAS_FILE_TAG.equals(flag)) {
                throw new Exception("文件头标志错误:" + flag);
            }

            // was 信息
            imageHeaderSize = readUnsignedShort();
            spriteCount = readUnsignedShort();
            frameCount = readUnsignedShort();
            width = readUnsignedShort();
            height = readUnsignedShort();
            centerX = readUnsignedShort();
            centerY = readUnsignedShort();

            // 读取帧延时信息
            int len = imageHeaderSize - WAS_IMAGE_HEADER_SIZE;
            if (len < 0) {
                throw new Exception("帧延时信息错误！");
            } else if (len > 0) {
                delayLine = new int[len];
                for (int i = 0; i < delayLine.length; i++) {
                    delayLine[i] = in.read();
                }
            } else
                delayLine = null;

            // 读取调色板
            in.seek(imageHeaderSize + 4);
            for (int i = 0; i < 256; i++) {
                palette[i] = readUnsignedShort();
            }
            frames = new WasFrame[spriteCount][];
            in.seek(imageHeaderSize + 4 + 512);
            for (int i = 0; i < spriteCount; i++) {
                frames[i] = new WasFrame[frameCount];
                for (int n = 0; n < frameCount; n++) {// 帧偏移列表
                    WasFrame frame = new WasFrame();
                    frames[i][n] = frame;
                    if (delayLine != null && n < delayLine.length) {
                        frames[i][n].setDelay(delayLine[n]);
                    }
                    frame.setFrameOffset(readInt());
                }
            }
            for (int i = 0; i < spriteCount; i++) {// 帧信息
                for (int n = 0; n < frameCount; n++) {
                    WasFrame frame = frames[i][n];
                    int offset = frame.getFrameOffset();
                    if (offset == 0)
                        continue;// blank frame
                    in.seek(offset + imageHeaderSize + 4);
                    frame.setX(readInt());
                    frame.setY(readInt());
                    frame.setWidth(readInt());
                    frame.setHeight(readInt());
                    frame.setLineOffsets(new int[frame.getHeight()]);
                    for (int l = 0; l < frame.getHeight(); l++) {// 行像素数据偏移
                        frame.getLineOffsets()[l] = readInt();
                    }
                    parse(frame);// 解析帧数据
                }
            }
        } catch (Exception e) {
            System.err.println("load was file failed!");
            e.printStackTrace();
        } finally {
            if (in != null)
                in.close();
            try {
                if (fileIn != null)
                    fileIn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //System.out.println((System.currentTimeMillis() - startTime) + "ms");
        }
    }

    /**
     * 将图片一行RLE编码格式的数据解码,解码后的数据放到pixels中<br>
     * 格式:低16位为[565]rgb颜色值,16-20位为alpha值(最大为0x1F);
     */
    public void parse(WasFrame frame) throws IOException {
        int frameWidth = frame.getWidth();
        int frameHeight = frame.getHeight();
        int[] pixels = new int[frameHeight * frameWidth];
        frame.setPixels(pixels);
        int b;
        int x;
        int c;
        int count;
        for (int y = 0; y < frameHeight; y++) {
            x = 0;
            in.seek(frame.getLineOffsets()[y] + frame.getFrameOffset() + imageHeaderSize + 4);
            while (x < frameWidth) {
                b = in.read();
                switch ((b & TYPE_FLAG)) {
                    case TYPE_ALPHA:
                        if ((b & TYPE_ALPHA_PIXEL) > 0) {
                            c = palette[in.read()];
                            pixels[y * width + x++] = c + ((b & 0x1F) << 16);
                        } else if (b != 0) {// ???
                            count = b & 0x1F;// count
                            b = in.read();// alpha
                            c = palette[in.read()];
                            for (int i = 0; i < count; i++) {
                                pixels[y * width + x++] = c + ((b & 0x1F) << 16);
                            }
                        } else {// block end
                            if (x > frameWidth) {
                                System.err.println("block end error: [" + y + "][" + x + "/" + frameWidth + "]");
                                continue;
                            } else if (x == 0) {
                                // System.err.println("x==0");
                            } else {
                                x = frameWidth;// set the x value to break the 'while' sentences
                            }
                        }
                        break;
                    case TYPE_PIXELS:
                        count = b & 0x3F;
                        for (int i = 0; i < count; i++) {
                            pixels[y * width + x++] = palette[in.read()] + (0x1F << 16);
                        }
                        break;
                    case TYPE_REPEAT:
                        count = b & 0x3F;
                        c = palette[in.read()];
                        for (int i = 0; i < count; i++) {
                            pixels[y * width + x++] = c + (0x1F << 16);
                        }
                        break;
                    case TYPE_SKIP:
                        count = b & 0x3F;
                        x += count;
                        break;
                }
            }
            if (x > frameWidth)
                System.err.println("block end error: [" + y + "][" + x + "/" + frameWidth + "]");
        }
    }

    private int readInt() throws IOException {
        int ch1 = in.read();
        int ch2 = in.read();
        int ch3 = in.read();
        int ch4 = in.read();
        return (ch1 + (ch2 << 8) + (ch3 << 16) + (ch4 << 24));
    }

    private short readUnsignedShort() throws IOException {
        int ch1 = in.read();
        int ch2 = in.read();
        return (short) ((ch2 << 8) + ch1);
    }
}

// 接下来是图片段数据信息，格式如下：
// 段数据=类型(8比特)+数据
//
// 类型的格式如下：
// 类型有4种，用2比特表示：
//
// 00：表示alpha象素，剩下的6比特也是0时，数据段结束。
// 如果第3个比特是1时，剩下的5比特(0~31)为alpha层。并且以后的字节是象素引索。
// 否则，剩下的5比特(0~31)是alpha象素重复次数.并且以后的2字节是alpha,象素引索。
// 01：表示象素组，剩下的6比特(0~63)为数据段的长度。
// 10：表示重复象素 n 次，n 为剩下的6比特(0~63)表示。
// 11：表示跳过象素 n 个，n 为剩下的6比特(0~63)表示。
