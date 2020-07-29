package com.wildbean.wastools.core;

import com.jmhxy.util.Utils;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.ColorModel;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import javax.swing.Icon;

public class StandardCanvasImage extends AbstractCanvasImage {
	private static final long serialVersionUID = 1L;
	private transient Image imageData;
	protected static final Component component = new Component() {
		private static final long serialVersionUID = 1L;
	};

	protected static final MediaTracker tracker = new MediaTracker(component);
	static int id;

	public StandardCanvasImage(Image image, String name) {
		this.imageData = image;
		this.name = name;
	}

	public Icon getIcon() {
		return Utils.loadIcon("standard.png");
	}

	public String getInfo() {
		return "标准图片\n宽度\t" + this.imageData.getWidth(null) + "\n高度\t" + this.imageData.getHeight(null);
	}

	public void draw(Image bgImage) {
		bgImage.getGraphics().drawImage(this.imageData, this.x, this.y, null);
	}

	public int getHeight() {
		return this.imageData.getHeight(null);
	}

	public int getWidth() {
		return this.imageData.getWidth(null);
	}

	public CanvasImage clone() {
		StandardCanvasImage newImage = new StandardCanvasImage(this.imageData, this.name);
		newImage.setLocation(this.x, this.y);
		newImage.setVisible(this.visible);
		return newImage;
	}

	private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
		s.defaultReadObject();

		int w = s.readInt();
		int h = s.readInt();
		int[] pixels = (int[]) s.readObject();

		if (pixels != null) {
			Toolkit tk = Toolkit.getDefaultToolkit();
			ColorModel cm = ColorModel.getRGBdefault();
			this.imageData = tk.createImage(new MemoryImageSource(w, h, cm, pixels, 0, w));
			loadImage(this.imageData);
		}
	}

	protected void loadImage(Image image) {
		synchronized (tracker) {
			id += 1;
			tracker.addImage(image, id);
			try {
				tracker.waitForID(id, 0L);
			} catch (InterruptedException e) {
				System.out.println("INTERRUPTED while loading Image");
			}

			tracker.removeImage(image, id);
		}
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
		s.defaultWriteObject();

		int w = getWidth();
		int h = getHeight();
		int[] pixels = this.imageData != null ? new int[w * h] : null;

		if (this.imageData != null) {
			try {
				PixelGrabber pg = new PixelGrabber(this.imageData, 0, 0, w, h, pixels, 0, w);
				pg.grabPixels();
				if ((pg.getStatus() & 0x80) != 0)
					throw new IOException("failed to load image contents");
			} catch (InterruptedException e) {
				throw new IOException("image load interrupted");
			}
		}

		s.writeInt(w);
		s.writeInt(h);
		s.writeObject(pixels);
	}

	public Image getData() {
		return this.imageData;
	}

	public void setData(Object imageData) {
		this.imageData = ((Image) imageData);
	}
}