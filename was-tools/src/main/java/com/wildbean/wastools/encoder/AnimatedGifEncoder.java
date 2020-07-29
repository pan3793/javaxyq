package com.wildbean.wastools.encoder;

import com.jmhxy.encoder.OcTreePal;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class AnimatedGifEncoder {
	protected int width;
	protected int height;
	protected Color transparent = null;
	protected int transIndex;
	protected int repeat = -1;

	protected int delay = 0;

	protected boolean started = false;
	protected OutputStream out;
	protected BufferedImage image;
	protected byte[] pixels;
	protected byte[] indexedPixels;
	protected int colorDepth;
	protected byte[] colorTab;
	protected boolean[] usedEntry = new boolean[256];

	protected int palSize = 7;

	protected int dispose = -1;

	protected boolean closeStream = false;

	protected boolean firstFrame = true;

	protected boolean sizeSet = false;

	protected int sample = 10;

	public void setDelay(int ms) {
		this.delay = Math.round(ms / 10.0F);
	}

	public void setDispose(int code) {
		if (code >= 0)
			this.dispose = code;
	}

	public void setRepeat(int iter) {
		if (iter >= 0)
			this.repeat = iter;
	}

	public void setTransparent(Color c) {
		this.transparent = c;
	}

	public boolean addFrame(BufferedImage im) {
		if ((im == null) || (!this.started)) {
			return false;
		}
		boolean ok = true;
		try {
			if (!this.sizeSet) {
				setSize(im.getWidth(), im.getHeight());
			}
			this.image = im;
			getImagePixels();
			analyzePixels();
			if (this.firstFrame) {
				writeLSD();
				writePalette();
				if (this.repeat >= 0) {
					writeNetscapeExt();
				}
			}
			writeGraphicCtrlExt();
			writeImageDesc();
			if (!this.firstFrame) {
				writePalette();
			}
			writePixels();
			this.firstFrame = false;
		} catch (IOException e) {
			ok = false;
		}

		return ok;
	}

	public boolean addFrame0(BufferedImage im) {
		if ((im == null) || (!this.started)) {
			return false;
		}
		boolean ok = true;
		try {
			if (!this.sizeSet) {
				setSize(im.getWidth(), im.getHeight());
			}
			this.image = im;
			getImagePixels0();
			analyzePixels0();
			if (this.firstFrame) {
				writeLSD();
				writePalette();
				if (this.repeat >= 0) {
					writeNetscapeExt();
				}
			}
			writeGraphicCtrlExt();
			writeImageDesc();
			if (!this.firstFrame) {
				writePalette();
			}
			writePixels();
			this.firstFrame = false;
		} catch (IOException e) {
			ok = false;
		}

		return ok;
	}

	public boolean finish() {
		if (!this.started)
			return false;
		boolean ok = true;
		this.started = false;
		try {
			this.out.write(59);
			this.out.flush();
			if (this.closeStream)
				this.out.close();
		} catch (IOException e) {
			ok = false;
		}

		this.transIndex = 0;
		this.out = null;
		this.image = null;
		this.pixels = null;
		this.indexedPixels = null;
		this.colorTab = null;
		this.closeStream = false;
		this.firstFrame = true;

		return ok;
	}

	public void setFrameRate(float fps) {
		if (fps != 0.0F)
			this.delay = Math.round(100.0F / fps);
	}

	public void setQuality(int quality) {
		if (quality < 1)
			quality = 1;
		this.sample = quality;
	}

	public void setSize(int w, int h) {
		if ((this.started) && (!this.firstFrame))
			return;
		this.width = w;
		this.height = h;
		if (this.width < 1)
			this.width = 320;
		if (this.height < 1)
			this.height = 240;
		this.sizeSet = true;
	}

	public boolean start(OutputStream os) {
		if (os == null)
			return false;
		boolean ok = true;
		this.closeStream = false;
		this.out = os;
		try {
			writeString("GIF89a");
		} catch (IOException e) {
			ok = false;
		}
		return this.started = ok;
	}

	public boolean start(String file) {
		boolean ok = true;
		try {
			this.out = new BufferedOutputStream(new FileOutputStream(file));
			ok = start(this.out);
			this.closeStream = true;
		} catch (IOException e) {
			ok = false;
		}
		return this.started = ok;
	}

	protected void analyzePixels() {
		int len = this.pixels.length;
		int nPix = len / 3;
		this.indexedPixels = new byte[nPix];
		NeuQuant nq = new NeuQuant(this.pixels, len, this.sample);

		this.colorTab = nq.process();

		for (int i = 0; i < this.colorTab.length; i += 3) {
			byte temp = this.colorTab[i];
			this.colorTab[i] = this.colorTab[(i + 2)];
			this.colorTab[(i + 2)] = temp;
			this.usedEntry[(i / 3)] = false;
		}

		int k = 0;
		for (int i = 0; i < nPix; i++) {
			int index = nq.map(this.pixels[(k++)] & 0xFF, this.pixels[(k++)] & 0xFF, this.pixels[(k++)] & 0xFF);
			this.usedEntry[index] = true;
			this.indexedPixels[i] = ((byte) index);
		}
		this.pixels = null;
		this.colorDepth = 8;
		this.palSize = 7;

		if (this.transparent != null)
			this.transIndex = findClosest(this.transparent);
	}

	protected void analyzePixels0() {
		int len = this.pixels.length;
		int nPix = len / 3;
		this.indexedPixels = new byte[nPix];

		OcTreePal pal = new OcTreePal();
		pal.addFrame(this.pixels);
		short[] palette = pal.getPalette();
		int plen = palette.length * 3;
		this.colorTab = new byte[plen];
		int n = 0;
		for (int i = 0; i < palette.length; i++) {
			this.colorTab[(n++)] = ((byte) (palette[i] >> 11 & 0x1F));
			this.colorTab[(n++)] = ((byte) (palette[i] >> 5 & 0x3F));
			this.colorTab[(n++)] = ((byte) (palette[i] & 0x1F));
		}

		int k = 0;
		for (int i = 0; i < nPix; i++) {
			int index = pal.getColorIndex(this.pixels[(k++)], this.pixels[(k++)], this.pixels[(k++)]);
			this.usedEntry[index] = true;
			this.indexedPixels[i] = ((byte) index);
		}
		this.pixels = null;
		this.colorDepth = 8;
		this.palSize = 7;

		if (this.transparent != null)
			this.transIndex = findClosest(this.transparent);
	}

	protected int findClosest(Color c) {
		if (this.colorTab == null)
			return -1;
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		int minpos = 0;
		int dmin = 16777216;
		int len = this.colorTab.length;
		for (int i = 0; i < len; i++) {
			int dr = r - (this.colorTab[(i++)] & 0xFF);
			int dg = g - (this.colorTab[(i++)] & 0xFF);
			int db = b - (this.colorTab[i] & 0xFF);
			int d = dr * dr + dg * dg + db * db;
			int index = i / 3;
			if (usedEntry[index] && d < dmin) {
				dmin = d;
				minpos = index;
			}
		}
		return minpos;
	}

	protected void getImagePixels() {
		int w = this.image.getWidth();
		int h = this.image.getHeight();
		int type = this.image.getType();
		if ((w != this.width) || (h != this.height) || (type != 5)) {
			BufferedImage temp = new BufferedImage(this.width, this.height, 5);
			Graphics2D g = temp.createGraphics();
			g.drawImage(this.image, 0, 0, null);
			this.image = temp;
		}
		this.pixels = ((DataBufferByte) this.image.getRaster().getDataBuffer()).getData();
	}

	protected void getImagePixels0() {
		int w = this.image.getWidth();
		int h = this.image.getHeight();
		int type = this.image.getType();
		if ((w != this.width) || (h != this.height) || (type != 1)) {
			BufferedImage temp = new BufferedImage(this.width, this.height, 1);
			Graphics2D g = temp.createGraphics();
			g.drawImage(this.image, 0, 0, null);
			this.image = temp;
		}

		int[] pixels0 = (int[]) null;
		pixels0 = this.image.getRaster().getPixels(0, 0, this.image.getWidth(), this.image.getHeight(), pixels0);
		this.pixels = new byte[pixels0.length * 3];
		for (int i = 0; i < pixels0.length; i++)
			this.pixels[i] = ((byte) pixels0[0]);
	}

	protected void writeGraphicCtrlExt() throws IOException {
		this.out.write(33);
		this.out.write(249);
		this.out.write(4);
		int disp;
		int transp;
		if (this.transparent == null) {
			transp = 0;
			disp = 0;
		} else {
			transp = 1;
			disp = 2;
		}
		if (this.dispose >= 0) {
			disp = this.dispose & 0x7;
		}
		disp <<= 2;

		this.out.write(disp | transp);

		writeShort(this.delay);
		this.out.write(this.transIndex);
		this.out.write(0);
	}

	protected void writeImageDesc() throws IOException {
		this.out.write(44);
		writeShort(0);
		writeShort(0);
		writeShort(this.width);
		writeShort(this.height);

		if (this.firstFrame) {
			this.out.write(0);
		} else
			this.out.write(0x80 | this.palSize);
	}

	protected void writeLSD() throws IOException {
		writeShort(this.width);
		writeShort(this.height);

		this.out.write(0xF0 | this.palSize);

		this.out.write(0);
		this.out.write(0);
	}

	protected void writeNetscapeExt() throws IOException {
		this.out.write(33);
		this.out.write(255);
		this.out.write(11);
		writeString("NETSCAPE2.0");
		this.out.write(3);
		this.out.write(1);
		writeShort(this.repeat);
		this.out.write(0);
	}

	protected void writePalette() throws IOException {
		this.out.write(this.colorTab, 0, this.colorTab.length);
		int n = 768 - this.colorTab.length;
		for (int i = 0; i < n; i++)
			this.out.write(0);
	}

	protected void writePixels() throws IOException {
		LZWEncoder encoder = new LZWEncoder(this.width, this.height, this.indexedPixels, this.colorDepth);
		encoder.encode(this.out);
	}

	protected void writeShort(int value) throws IOException {
		this.out.write(value & 0xFF);
		this.out.write(value >> 8 & 0xFF);
	}

	protected void writeString(String s) throws IOException {
		for (int i = 0; i < s.length(); i++)
			this.out.write((byte) s.charAt(i));
	}
}