package com.wildbean.wastools.core;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

public class Canvas extends JPanel {
	private static final long serialVersionUID = 1L;
	public static final String LAYER_SELECTED = "LayerSelected";
	public static final String UPDATE_PREFERENCES = "RefreshViewport";
	transient BufferedImage bufImg;
	public transient CanvasImage curImage;
	private Color canvasBackground;
	protected int canvasWidth;
	protected int canvasHeight;
	private transient boolean playing = true;

	private transient UpdateThread updateThread = new UpdateThread();
	private transient Graphics2D bufGraphics;
	private Vector<CanvasImage> images;
	private transient Point srcPoint;
	private transient boolean bStop;
	private transient boolean bUpdate = true;

	private transient EventHandler eventHandler = new EventHandler();

	private transient boolean autoSelect = false;
	private boolean autoZoom;
	private transient long lastTime;
	private boolean transparent = true;
	private String canvasName;
	private transient boolean export;
	private transient boolean showBoundary;
	private transient String filename;
	private transient boolean isDirty;
	private static final Color GRID_BACKGROUND = new Color(204, 204, 204);
	public static final String CANVAS_DATA_CHANGED = "canvas data has changed";
	public static final int ALIGN_SPRITE = 0;
	public static final int ALIGN_LEFT = 1;
	public static final int ALIGN_HOR_CENTER = 2;
	public static final int ALIGN_RIGHT = 3;
	public static final int ALIGN_TOP = 4;
	public static final int ALIGN_VER_CENTER = 5;
	public static final int ALIGN_BOTTOM = 6;
	public static final String CANVAS_FRAME_INDEX = "canvas frame index";

	public boolean isTransparent() {
		return this.transparent;
	}

	public void replaceLinkedBase(CanvasImage base, CanvasImage newBase) {
		int size = this.images.size();
		for (int i = 0; i < size; i++) {
			CanvasImage img = (CanvasImage) this.images.get(i);
			if (img.getLinkedBase() == base) {
				img.setLinkedBase(newBase);
			}
		}
		base.setBase(false);
		if (newBase != null)
			newBase.setBase(true);
	}

	protected void fireCanvasDataChanged() {
		firePropertyChange("canvas data has changed", null, null);
	}

	public void setTransparent(boolean transparent) {
		if (this.transparent != transparent) {
			this.transparent = transparent;
			setDirty(true);
		}
	}

	public Canvas() {
		this.images = new Vector();
		this.canvasBackground = Color.BLACK;
		init();
	}

	public Canvas(int width, int height) {
		this.images = new Vector();
		this.canvasBackground = Color.BLACK;
		setCanvasSize(width, height, true);
		init();
		setDirty(true);
	}

	public Canvas(CanvasImage image) {
		this(image.getWidth(), image.getHeight());
		addImage(image);
		setCanvasName(image.getName());
	}

	private void paintBackground() {
		if (this.transparent) {
			if (this.export) {
				this.bufGraphics.setComposite(AlphaComposite.Clear);
				this.bufGraphics.fillRect(0, 0, this.bufImg.getWidth(), this.bufImg.getHeight());
			} else {
				this.bufGraphics.setComposite(AlphaComposite.SrcOver);
				this.bufGraphics.setColor(Color.WHITE);
				this.bufGraphics.fillRect(0, 0, this.bufImg.getWidth(), this.bufImg.getHeight());
				this.bufGraphics.setColor(GRID_BACKGROUND);
				int row = (this.bufImg.getHeight() + 7) / 8;
				int col = (this.bufImg.getWidth() + 7) / 8;
				int dx = 8;
				int dy = 0;
				for (int r = 0; r < row; r++) {
					dx = r % 2 * 8;
					for (int c = 0; c < col; c += 2) {
						this.bufGraphics.fillRect(dx, dy, 8, 8);
						dx += 16;
					}
					dy += 8;
				}
			}
		} else {
			this.bufGraphics.setComposite(AlphaComposite.SrcOver);
			this.bufGraphics.setColor(this.canvasBackground);
			this.bufGraphics.fillRect(0, 0, this.bufImg.getWidth(), this.bufImg.getHeight());
		}
	}

	public synchronized void paintCanvas() {
		paintBackground();
		for (int i = this.images.size() - 1; i >= 0; i--) {
			CanvasImage image = (CanvasImage) this.images.get(i);
			paintImage(image);
		}

		if ((!this.export) && (this.showBoundary) && (this.curImage != null)) {
			this.bufGraphics.setColor(Color.RED);
			this.bufGraphics.drawRect(this.curImage.getX(), this.curImage.getY(), this.curImage.getWidth() - 1,
					this.curImage.getHeight() - 1);
		}
	}

	public void updateAnimations() {
		int oldIndex = getFrameIndex();
		long currTime = System.currentTimeMillis();
		long elapsedTime = currTime - this.lastTime;
		if (this.playing) {
			for (CanvasImage image : this.images) {
				image.update(elapsedTime);
			}
		}
		this.lastTime = currTime;
		fireFrameIndexChanged(oldIndex);
	}

	private void init() {
		addKeyListener(this.eventHandler);
		addMouseListener(this.eventHandler);
		addMouseMotionListener(this.eventHandler);
		addPropertyChangeListener(this.eventHandler);

		this.updateThread.setDaemon(true);
		this.updateThread.start();
		setFocusable(true);
		setRequestFocusEnabled(true);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (this.bufImg != null) {
			Rectangle rect = getVisibleRect();
			int x = rect.x;
			int y = rect.y;
			int w = rect.width;
			int h = rect.height;
			double rate = Math.max(1.0D * this.canvasWidth / w, 1.0D * this.canvasHeight / h);
			if ((this.autoZoom) && (rate > 1.0D)) {
				int cw = (int) (this.canvasWidth / rate);
				int ch = (int) (this.canvasHeight / rate);
				x = (w - cw) / 2;
				y = (h - ch) / 2;
				g.drawImage(this.bufImg, x, y, cw, ch, this);
			} else {
				g.drawImage(this.bufImg, 0, 0, this);
			}
		}
	}

	public void paintImage(CanvasImage image) {
		if (image.isVisible())
			image.draw(this.bufImg);
	}

	public void setCanvasSize(int width, int height) {
		setCanvasSize(width, height, false);
	}

	public void setCanvasSize(int width, int height, boolean preferredsize) {
		if (preferredsize)
			setPreferredSize(new Dimension(width, height));
		this.canvasWidth = width;
		this.canvasHeight = height;

		if (this.bufGraphics != null)
			this.bufGraphics.dispose();
		this.bufImg = new BufferedImage(width, height, 2);
		this.bufGraphics = this.bufImg.createGraphics();
		setDirty(true);
	}

	public void setCanvasBackground(Color background) {
		this.canvasBackground = background;
		setDirty(true);
	}

	public Color getCanvasBackground() {
		return this.canvasBackground;
	}

	public void removeImage(CanvasImage image) {
		if (image != null) {
			this.images.remove(image);
			if (this.curImage == image) {
				this.curImage = null;
			}
			image.removePropertyChangeListener(this.eventHandler);
		}
	}

	public void removeAllImages() {
		for (CanvasImage image : this.images) {
			image.removePropertyChangeListener(this.eventHandler);
		}
		this.images.removeAllElements();
		this.curImage = null;
	}

	public int getCanvasWidth() {
		return this.canvasWidth;
	}

	public int getCanvasHeight() {
		return this.canvasHeight;
	}

	public void dispose() {
		this.bStop = true;

		this.bUpdate = false;
		this.bufGraphics.dispose();
		this.bufImg = null;
		this.curImage = null;
		this.images = null;
		System.gc();
	}

	public void setUpdate(boolean b) {
		this.bUpdate = b;
	}

	public Dimension getCanvasSize() {
		return new Dimension(this.canvasWidth, this.canvasHeight);
	}

	public void setAutoZoom(boolean autoZoom) {
		this.autoZoom = autoZoom;
	}

	public boolean isAutoZoom() {
		return this.autoZoom;
	}

	public void addImage(CanvasImage canvasImage) {
		addImage(0, canvasImage, true);
	}

	public int getMinFrameCount() {
		int count = 0;
		for (CanvasImage image : this.images) {
			if (image.isVisible()) {
				count = Math.max(count, image.getFrameCount());
			}
		}
		return count;
	}

	public boolean isAutoSelect() {
		return this.autoSelect;
	}

	public void setAutoSelect(boolean autoSelect) {
		this.autoSelect = autoSelect;
	}

	public void setCanvasName(String text) {
		this.canvasName = text;
	}

	public String getCanvasName() {
		return this.canvasName;
	}

	public void setExport(boolean b) {
		this.export = b;
	}

	public void firstFrame() {
		pause();
		int oldIndex = getFrameIndex();
		for (CanvasImage image : this.images) {
			image.setFrameIndex(0);
		}
		fireFrameIndexChanged(oldIndex);
	}

	public void nextFrame() {
		pause();
		int oldIndex = getFrameIndex();
		for (CanvasImage image : this.images) {
			image.update(100L);
		}
		fireFrameIndexChanged(oldIndex);
	}

	protected void fireFrameIndexChanged(int oldIndex) {
		firePropertyChange("canvas frame index", oldIndex, getFrameIndex());
	}

	public void prevFrame() {
		pause();
		int oldIndex = getFrameIndex();
		for (CanvasImage image : this.images) {
			image.update(-100L);
		}
		fireFrameIndexChanged(oldIndex);
	}

	public void play() {
		this.playing = true;
	}

	public void pause() {
		this.playing = false;
	}

	public boolean isPlaying() {
		return this.playing;
	}

	public int getTotalDelay() {
		int count = 0;
		for (CanvasImage image : this.images) {
			if (image.isVisible()) {
				count = Math.max(count, image.getTotalDelay());
			}
		}
		return count;
	}

	public Vector<CanvasImage> getImages() {
		return this.images;
	}

	public void alignImages(int type) {
		int destX = this.curImage.getX();
		int destY = this.curImage.getY();
		int width = this.curImage.getWidth();
		int height = this.curImage.getHeight();
		if (this.curImage.isBase()) {
			switch (type) {
			case 0:
				for (CanvasImage image : this.images) {
					if (image.getLinkedBase() == this.curImage) {
						image.alignToCenter(this.curImage);
					}
				}
				break;
			case 1:
				for (CanvasImage image : this.images) {
					if (image.getLinkedBase() == this.curImage) {
						image.setLocation(destX, image.getY());
					}
				}
				break;
			case 2:
				for (CanvasImage image : this.images) {
					if (image.getLinkedBase() == this.curImage) {
						image.setLocation(destX + (width - image.getWidth()) / 2, image.getY());
					}
				}
				break;
			case 3:
				for (CanvasImage image : this.images) {
					if (image.getLinkedBase() == this.curImage) {
						image.setLocation(destX + width - image.getWidth(), image.getY());
					}
				}
				break;
			case 4:
				for (CanvasImage image : this.images) {
					if (image.getLinkedBase() == this.curImage) {
						image.setLocation(image.getX(), destY);
					}
				}
				break;
			case 5:
				for (CanvasImage image : this.images) {
					if (image.getLinkedBase() == this.curImage) {
						image.setLocation(image.getX(), destY + (height - image.getHeight()) / 2);
					}
				}
				break;
			case 6:
				for (CanvasImage image : this.images) {
					if (image.getLinkedBase() == this.curImage) {
						image.setLocation(image.getX(), destY + height - image.getHeight());
					}
				}
				break;
			default:
				break;
			}
		} else {
			switch (type) {
			case 0:
				this.curImage.alignToCenter(new Rectangle(0, 0, getWidth(), getHeight()));
				break;
			case 1:
				this.curImage.setLocation(0, destY);
				break;
			case 2:
				this.curImage.setLocation((getWidth() - width) / 2, destY);
				break;
			case 3:
				this.curImage.setLocation(getWidth() - width, destY);
				break;
			case 4:
				this.curImage.setLocation(destX, 0);
				break;
			case 5:
				this.curImage.setLocation(destX, (getHeight() - height) / 2);
				break;
			case 6:
				this.curImage.setLocation(destX, getHeight() - height);
				break;
			}
		}
	}

	public boolean isShowBoundary() {
		return this.showBoundary;
	}

	public void setShowBoundary(boolean showBoundary) {
		this.showBoundary = showBoundary;
	}

	public void setCurrentImage(CanvasImage image) {
		this.curImage = image;
		CanvasImage base = this.curImage.getLinkedBase();
		if (base != null) {
			replaceLinkedBase(base, this.curImage);
			base.setLinkedBase(this.curImage);
			this.curImage.setLinkedBase(null);
		}
		fireFrameIndexChanged(-1);
	}

	public String toString() {
		return this.canvasName;
	}

	public int indexOfImage(CanvasImage image) {
		return this.images.indexOf(image);
	}

	public void addImage(int index, CanvasImage image, boolean autoAlign) {
		if (index == -1) {
			index = this.images.size();
		}
		if (autoAlign) {
			int x = (this.canvasWidth - image.getWidth()) / 2;
			int y = (this.canvasHeight - image.getHeight()) / 2;
			image.setLocation(x, y);
		}
		image.addPropertyChangeListener(this.eventHandler);
		this.images.add(index, image);
		setDirty(true);
	}

	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
		this.canvasName = new File(filename).getName();
	}

	public void save() {
		save(this.filename);
	}

	public boolean isDirty() {
		return this.isDirty;
	}

	public void save(String filename) {
		if (filename != null) {
			File file = new File(filename);
			save(file);
		}
	}

	public void setFilename(File file) {
		this.filename = (file != null ? file.getAbsolutePath() : null);
		this.canvasName = file.getName();
	}

	public void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
	}

	public void save(File file) {
		try {
			if (isDirty()) {
				if (!file.exists())
					file.createNewFile();
				ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream(file));
				objOut.writeUnshared(this);
				objOut.close();
				setFilename(file);
				setDirty(false);
				fireCanvasDataChanged();
			}
		} catch (Exception e) {
			System.err.println("保存失败:" + e.getMessage());
			e.printStackTrace();
		}
	}

	public static Canvas loadFromFile(String filename) {
		return loadFromFile(new File(filename));
	}

	public static Canvas loadFromFile(File file) {
		Canvas canvas = new Canvas();
		canvas.load(file);
		return canvas;
	}

	public void load(File file) {
		try {
			ObjectInputStream objInput = new ObjectInputStream(new FileInputStream(file));
			Canvas srcCanvas = (Canvas) objInput.readUnshared();
			srcCanvas.stop();
			setCanvasName(srcCanvas.canvasName);
			setCanvasBackground(srcCanvas.canvasBackground);
			setCanvasSize(srcCanvas.canvasWidth, srcCanvas.canvasHeight, true);
			for (CanvasImage image : srcCanvas.images) {
				addImage(-1, image, false);
			}
			setTransparent(srcCanvas.transparent);
			setFilename(file);
			setDirty(false);
			objInput.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void stop() {
		this.bStop = true;
		if (this.updateThread != null) {
			this.updateThread.interrupt();
		}

		this.bUpdate = false;
		if (this.bufGraphics != null)
			this.bufGraphics.dispose();
		this.bufImg = null;
		this.curImage = null;
	}

	public void load(String filename) {
		load(new File(filename));
	}

	public int getFrameIndex() {
		return this.curImage != null ? this.curImage.getFrameIndex() + 1 : -1;
	}

	public void firePreferencesChange() {
		firePropertyChange("RefreshViewport", null, getCanvasSize());
	}

	private class UpdateThread extends Thread {
		private UpdateThread() {
		}

		public void run() {
			Canvas.this.lastTime = System.currentTimeMillis();
			while (!Canvas.this.bStop) {
				if ((!Canvas.this.export) && (Canvas.this.bUpdate) && (Canvas.this.bufImg != null)) {
					Canvas.this.updateAnimations();
					Canvas.this.paintCanvas();
					Canvas.this.repaint();
				}
				try {
					Thread.sleep(50L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private class EventHandler implements ActionListener, PropertyChangeListener, KeyListener, MouseInputListener {
		Vector<Integer> keyList = new Vector();

		private final Integer KEY_LEFT = new Integer(37);

		private final Integer KEY_UP = new Integer(38);

		private final Integer KEY_RIGHT = new Integer(39);

		private final Integer KEY_DOWN = new Integer(40);

		private EventHandler() {
		}

		public void actionPerformed(ActionEvent e) {
		}

		public void mousePressed(MouseEvent e) {
			Canvas.this.requestFocus(true);
			if (e.getButton() == 1) {
				Canvas.this.srcPoint = e.getPoint();
				if (Canvas.this.autoSelect) {
					int size = Canvas.this.images.size();
					for (int i = 0; i < size; i++) {
						CanvasImage image = (CanvasImage) Canvas.this.images.get(i);
						if ((image.isVisible()) && (image.contain(e.getPoint()))) {
							Canvas.this.firePropertyChange("LayerSelected", null, image);
							Canvas.this.setCurrentImage(image);
							break;
						}
					}
				}
				Canvas.this.setCursor(Cursor.getPredefinedCursor(13));
			}
		}

		public void mouseReleased(MouseEvent e) {
			Canvas.this.srcPoint = null;
			Canvas.this.setCursor(Cursor.getDefaultCursor());
		}

		public void mouseDragged(MouseEvent e) {
			if (Canvas.this.srcPoint == null)
				return;
			Canvas.this.setCursor(Cursor.getPredefinedCursor(13));
			Point curPoint = e.getPoint();
			int dx = curPoint.x - Canvas.this.srcPoint.x;
			int dy = curPoint.y - Canvas.this.srcPoint.y;
			if (Canvas.this.curImage != null) {
				Canvas.this.curImage.translate(dx, dy);
				for (CanvasImage image : Canvas.this.images) {
					if (image.getLinkedBase() == Canvas.this.curImage) {
						image.translate(dx, dy);
					}
				}
			}
			Canvas.this.srcPoint = curPoint;
		}

		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			if (key == 37) {
				if (this.keyList.contains(this.KEY_UP))
					Canvas.this.curImage.setSpriteIndex(2);
				else if (this.keyList.contains(this.KEY_DOWN))
					Canvas.this.curImage.setSpriteIndex(1);
				else
					Canvas.this.curImage.setSpriteIndex(5);
			} else if (key == 38) {
				if (this.keyList.contains(this.KEY_LEFT))
					Canvas.this.curImage.setSpriteIndex(2);
				else if (this.keyList.contains(this.KEY_RIGHT))
					Canvas.this.curImage.setSpriteIndex(3);
				else
					Canvas.this.curImage.setSpriteIndex(6);
			} else if (key == 39) {
				if (this.keyList.contains(this.KEY_UP))
					Canvas.this.curImage.setSpriteIndex(3);
				else if (this.keyList.contains(this.KEY_DOWN))
					Canvas.this.curImage.setSpriteIndex(0);
				else
					Canvas.this.curImage.setSpriteIndex(7);
			} else if (key == 40) {
				if (this.keyList.contains(this.KEY_LEFT))
					Canvas.this.curImage.setSpriteIndex(1);
				else if (this.keyList.contains(this.KEY_RIGHT))
					Canvas.this.curImage.setSpriteIndex(0);
				else {
					Canvas.this.curImage.setSpriteIndex(4);
				}
			}
			if (Canvas.this.curImage != null)
				Canvas.this.curImage.fireSpriteIndexChanged();
			if (!this.keyList.contains(Integer.valueOf(key)))
				this.keyList.add(Integer.valueOf(key));
		}

		public void keyReleased(KeyEvent e) {
			Integer key = null;
			switch (e.getKeyCode()) {
			case 37:
				key = this.KEY_LEFT;
				break;
			case 38:
				key = this.KEY_UP;
				break;
			case 39:
				key = this.KEY_RIGHT;
				break;
			case 40:
				key = this.KEY_DOWN;
			}

			this.keyList.remove(key);
		}

		public void propertyChange(PropertyChangeEvent evt) {
			String cmd = evt.getPropertyName();
			Object source = evt.getSource();
			if ("sprite index changed".equals(cmd)) {
				CanvasImage destImage = (CanvasImage) source;
				if (destImage != null) {
					int index = ((Integer) evt.getNewValue()).intValue();
					if (index > destImage.getSpriteCount())
						destImage.setSpriteIndex(index % destImage.getSpriteCount());
					else {
						destImage.setSpriteIndex(index);
					}
					for (CanvasImage image : Canvas.this.images) {
						if (image.getLinkedBase() == destImage) {
							if (index > image.getSpriteCount())
								image.setSpriteIndex(index % image.getSpriteCount());
							else {
								image.setSpriteIndex(index);
							}
						}
					}
					Canvas.this.setDirty(true);
					Canvas.this.fireCanvasDataChanged();
				}
			} else if ("image data changed".equals(cmd)) {
				Canvas.this.setDirty(true);
				Canvas.this.fireCanvasDataChanged();
			}
		}

		public void keyTyped(KeyEvent e) {
		}

		public void mouseClicked(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mouseMoved(MouseEvent e) {
		}
	}
}