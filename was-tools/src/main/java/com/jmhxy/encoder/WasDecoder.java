package com.jmhxy.encoder;

import com.jmhxy.animation.WasFrame;
import com.jmhxy.animation.WasImage;
import com.jmhxy.util.SeekByteArrayInputStream;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Vector;

public class WasDecoder {
	static final int TYPE_ALPHA = 0;
	static final int TYPE_ALPHA_PIXEL = 32;
	static final int TYPE_ALPHA_REPEAT = 0;
	static final int TYPE_FLAG = 192;
	static final int TYPE_PIXELS = 64;
	static final int TYPE_REPEAT = 128;
	static final int TYPE_SKIP = 192;
	static final String WAS_FILE_TAG = "SP";
	static final int WAS_IMAGE_HEADER_SIZE = 12;
	int centerX;
	int centerY;
	Vector<Integer> delays;
	int frameCount;
	int height;
	static short imageHeaderSize;
	Vector<Image> images;
	static short[] palette;
	int spriteCount;
	int width;

	public WasDecoder() {
		palette = new short['Ā'];
		this.images = new Vector();
		this.delays = new Vector();
	}

	public void draw(int[][] pixels, WritableRaster raster, int x, int y, int w, int h) {
		int[] iArray = new int[4];
		for (int y1 = 0; (y1 < h) && (y1 + y < this.height); y1++) {
			for (int x1 = 0; (x1 < w) && (x1 + x < this.width); x1++) {
				iArray[0] = ((pixels[y1][x1] >>> 11 & 0x1F) * 255 / 31);
				iArray[1] = ((pixels[y1][x1] >>> 5 & 0x3F) * 255 / 63);
				iArray[2] = ((pixels[y1][x1] & 0x1F) * 255 / 31);
				iArray[3] = ((pixels[y1][x1] >>> 16 & 0x1F) * 255 / 31);
				raster.setPixel(x1 + x, y1 + y, iArray);
			}
		}
	}

	public short[] getPalette() {
		return palette;
	}

	public int getCenterX() {
		return this.centerX;
	}

	public int getCenterY() {
		return this.centerY;
	}

	public Image getFrame(int index) {
		return (Image) this.images.get(index);
	}

	public Vector<Image> getFrames() {
		return (Vector) this.images.clone();
	}

	public int getDelay(int index) {
		int i = 1;
		if (index < this.delays.size()) {
			i = ((Integer) this.delays.get(index)).intValue();
		}
		return i;
	}

	public int getHeight() {
		return this.height;
	}

	public int getWidth() {
		return this.width;
	}

	public int getSpriteCount() {
		return this.spriteCount;
	}

	public int getFrameCount() {
		return this.frameCount;
	}

	public void load(InputStream in) throws Exception {
		this.images.clear();
		this.delays.clear();
		byte[] buf = new byte[in.available()];
		int a = 0;
		int count = 0;
		while (in.available() > 0) {
			a = in.read(buf, count, in.available());
			count += a;
		}

		SeekByteArrayInputStream sIn = new SeekByteArrayInputStream(buf);
		buf = new byte[2];
		sIn.read(buf, 0, 2);
		String flag = new String(buf, 0, 2);
		if (!"SP".equals(flag)) {
			throw new Exception("文件头标志错:" + flag);
		}

		imageHeaderSize = sIn.readUnsignedShort();
		this.spriteCount = sIn.readUnsignedShort();
		this.frameCount = sIn.readUnsignedShort();
		this.width = sIn.readUnsignedShort();
		this.height = sIn.readUnsignedShort();
		this.centerX = sIn.readUnsignedShort();
		this.centerY = sIn.readUnsignedShort();

		int len = imageHeaderSize - 12;
		if (len < 0)
			throw new Exception("帧延时信息错: " + len);
		for (int i = 0; i < len; i++) {
			this.delays.add(Integer.valueOf(sIn.read()));
		}

		sIn.seek(imageHeaderSize + 4);
		for (int i = 0; i < 256; i++) {
			palette[i] = sIn.readUnsignedShort();
		}

		int[] frameOffsets = new int[this.spriteCount * this.frameCount];
		sIn.seek(imageHeaderSize + 4 + 512);
		for (int i = 0; i < this.spriteCount; i++) {
			for (int n = 0; n < this.frameCount; n++) {
				frameOffsets[(i * this.frameCount + n)] = sIn.readInt();
			}
		}

		for (int i = 0; i < this.spriteCount; i++) {
			for (int n = 0; n < this.frameCount; n++) {
				int offset = frameOffsets[(i * this.frameCount + n)];
				BufferedImage image = new BufferedImage(this.width, this.height, 2);
				if (offset != 0) {
					sIn.seek(offset + imageHeaderSize + 4);
					int frameX = sIn.readInt();
					int frameY = sIn.readInt();
					int frameWidth = sIn.readInt();
					int frameHeight = sIn.readInt();

					int[] lineOffsets = new int[frameHeight];
					for (int l = 0; l < frameHeight; l++) {
						lineOffsets[l] = sIn.readInt();
					}

					int[][] pixels = parse(sIn, offset, lineOffsets, frameWidth, frameHeight);
					draw(pixels, image.getRaster(), this.centerX - frameX, this.centerY - frameY, frameWidth,
							frameHeight);
				}
				this.images.add(image);
			}
		}
	}

	public void load(String filename) throws Exception {
		InputStream fileIn = getClass().getResourceAsStream(filename);
		load(fileIn);
	}

	private static int[][] parse(SeekByteArrayInputStream in, int frameOffset, int[] lineOffsets, int frameWidth,
			int frameHeight) throws IOException {
		int[][] pixels = new int[frameHeight][frameWidth];

		for (int y = 0; y < frameHeight; y++) {
			int x = 0;
			int count;
			in.seek(lineOffsets[y] + frameOffset + imageHeaderSize + 4);
			while (x < frameWidth) {
				int b = in.read();
				switch (b & 0xC0) {
				case 0:
					if ((b & 0x20) > 0) {
						int index = in.read();
						int c = palette[index];

						pixels[y][(x++)] = (c + ((b & 0x1F) << 16));
					} else if (b != 0) {
						count = b & 0x1F;
						b = in.read();
						int index = in.read();
						int c = palette[index];

						for (int i = 0; i < count; i++) {
							pixels[y][(x++)] = (c + ((b & 0x1F) << 16));
						}
					} else if (x > frameWidth) {
						System.err.println("block end error: [" + y + "][" + x + "/" + frameWidth + "]");
					} else if (x != 0) {

						x = frameWidth;
					}

					break;
				case 64:
					count = b & 0x3F;
					for (int i = 0; i < count; i++) {
						int index = in.read();
						pixels[y][(x++)] = (palette[index] + 2031616);
					}

					break;
				case 128:
					count = b & 0x3F;
					int index = in.read();
					int c = palette[index];

					for (int i = 0; i < count; i++) {
						pixels[y][(x++)] = (c + 2031616);
					}
					break;
				case 192:
					count = b & 0x3F;
					x += count;
				}

			}
			if (x > frameWidth)
				System.err.println("block end error: [" + y + "][" + x + "/" + frameWidth + "]");
		}
		return pixels;
	}

	public static WasImage loadImage(InputStream is) throws IOException,IllegalArgumentException   {
		int[] frameDelays = (int[]) null;
		SeekByteArrayInputStream in;
		if ((is instanceof SeekByteArrayInputStream)) {
			in = (SeekByteArrayInputStream) is;
		} else {
			byte[] buf = new byte[is.available()];
			is.read(buf);
			in = new SeekByteArrayInputStream(buf);
		}

		byte[] buf = new byte[2];
		in.read(buf, 0, 2);
		if (!"SP".equals(new String(buf, 0, 2))) {
			in.close();
			throw new IllegalArgumentException("��WAS�ļ���ʽ!");
		}
		imageHeaderSize = in.readUnsignedShort();
		int len = imageHeaderSize - 12;
		if (len < 0) {
			in.close();
			throw new IllegalArgumentException("֡��ʱ��Ϣ����");
		}
		int spriteCount = in.readUnsignedShort();
		int frameCount = in.readUnsignedShort();
		int width = in.readUnsignedShort();
		int height = in.readUnsignedShort();
		int xCenter = in.readUnsignedShort();
		int yCenter = in.readUnsignedShort();
		if (len > 0) {
			frameDelays = new int[len];
			for (int i = 0; i < frameDelays.length; i++) {
				frameDelays[i] = in.read();
			}
		}
		in.seek(imageHeaderSize + 4);
		for (int i = 0; i < 256; i++) {
			palette[i] = in.readUnsignedShort();
		}
		WasFrame[][] frames = new WasFrame[spriteCount][frameCount];
		int[][] frameOffset = new int[spriteCount][frameCount];
		in.seek(imageHeaderSize + 4 + 512);
		for (int i = 0; i < spriteCount; i++) {
			for (int n = 0; n < frameCount; n++) {
				frameOffset[i][n] = in.readInt();
				WasFrame frame = new WasFrame();
				if ((frameDelays != null) && (n < frameDelays.length)) {
					frame.delay = frameDelays[n];
				}
				frames[i][n] = frame;
			}
		}
		for (int i = 0; i < spriteCount; i++)
			for (int n = 0; n < frameCount; n++) {
				WasFrame frame = frames[i][n];
				int offset = frameOffset[i][n];
				if (offset != 0) {
					in.seek(offset + imageHeaderSize + 4);
					frame.xCenter = in.readInt();
					frame.yCenter = in.readInt();
					frame.width = in.readInt();
					frame.height = in.readInt();
					int[] lineOffsets = new int[frame.height];
					for (int l = 0; l < frame.height; l++) {
						lineOffsets[l] = in.readInt();
					}
					frame.pixels = parse(in, offset, lineOffsets, frame.width, frame.height);
				}
			}
		in.close();
		WasImage wasImage = new WasImage();
		wasImage.frameCount = frameCount;
		wasImage.spriteCount = spriteCount;
		wasImage.frames = frames;
		wasImage.width = width;
		wasImage.height = height;
		wasImage.xCenter = xCenter;
		wasImage.yCenter = yCenter;
		wasImage.palette = palette;
		return wasImage;
	}
}

/*
 * Location: D:\Desktop\WasTools2.0-all.jar!\com\jmhxy\encoder\WasDecoder.class
 * Java compiler version: 6 (50.0) JD-Core Version: 0.7.1
 */