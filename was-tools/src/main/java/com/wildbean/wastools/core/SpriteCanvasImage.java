package com.wildbean.wastools.core;

import com.jmhxy.animation.WasFrame;
import com.jmhxy.animation.WasImage;
import com.jmhxy.util.Utils;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import javax.swing.Icon;

public class SpriteCanvasImage extends AbstractCanvasImage {
	private static final long serialVersionUID = 1L;
	private WasImage imageData;

	public SpriteCanvasImage(WasImage image, String name) {
		this.imageData = image;
		this.name = name;
		setSpriteIndex(0);
	}

	public WasImage getData() {
		return this.imageData;
	}

	public void alignToCenter(CanvasImage base) {
		if (base.getType() == getType()) {
			SpriteCanvasImage sprite = (SpriteCanvasImage) base;
			this.x = (sprite.x + sprite.imageData.xCenter - this.imageData.xCenter);
			this.y = (sprite.y + sprite.imageData.yCenter - this.imageData.yCenter);
			setSpriteIndex(sprite.spriteIndex);
			setFrameIndex(sprite.frameIndex);
			fireSpriteIndexChanged();
		} else {
			alignToCenter(base.getBounds());
		}
		fireDataChanged();
	}

	public void alignToCenter(Rectangle rect) {
		this.x = (rect.x + rect.width / 2 - this.imageData.xCenter);
		this.y = (rect.y + rect.height / 2 - this.imageData.yCenter);
		fireDataChanged();
	}

	public void draw(Image bgImage) {
		BufferedImage bufferedImage;
		if ((bgImage instanceof BufferedImage)) {
			bufferedImage = (BufferedImage) bgImage;
		} else {
			bufferedImage = new BufferedImage(bgImage.getWidth(null), bgImage.getHeight(null), 2);
		}
		WritableRaster raster = bufferedImage.getRaster();
		WasFrame frame = this.imageData.frames[this.spriteIndex][this.frameIndex];
		frame.draw(raster, this.x + this.imageData.xCenter - frame.xCenter,
				this.y + this.imageData.yCenter - frame.yCenter);
		if (bufferedImage != bgImage)
			bgImage.getGraphics().drawImage(bufferedImage, 0, 0, null);
	}

	public int getFrameCount() {
		return this.imageData.frameCount;
	}

	public int getHeight() {
		return this.imageData.height;
	}

	public String getInfo() {
		return this.imageData.toString();
	}

	public int getSpriteCount() {
		return this.imageData.spriteCount;
	}

	public int getType() {
		return 1;
	}

	public void setFrameIndex(int index) {
		if (index >= getFrameCount()) {
			index %= getFrameCount();
		}
		this.frameIndex = index;
		this.animTime = (this.imageData.frames[this.spriteIndex][this.frameIndex].endTime - 1L);
	}

	public void setSpriteIndex(int index) {
		if (index >= getSpriteCount()) {
			index %= getSpriteCount();
		}
		this.spriteIndex = index;
		WasFrame[] animation = this.imageData.frames[index];
		this.totalDuration = 0L;
		for (WasFrame frame : animation) {
			this.totalDuration += frame.delay * 100;
			frame.endTime = this.totalDuration;
		}
		fireDataChanged();
	}

	public void update(long elapsedTime) {
		if ((this.imageData != null) && (this.imageData.frameCount > 1)) {
			this.animTime += elapsedTime;
			if (this.animTime < 0L) {
				this.animTime += this.totalDuration;
			}
			if (elapsedTime < 0L) {
				this.frameIndex = 0;
			}
			if (this.animTime >= this.totalDuration) {
				this.animTime %= this.totalDuration;
				this.frameIndex = 0;
			}
			WasFrame[] frames = this.imageData.frames[this.spriteIndex];
			while (this.animTime > frames[this.frameIndex].endTime)
				this.frameIndex += 1;
		}
	}

	public Icon getIcon() {
		return Utils.loadIcon("was.png");
	}

	public int getTotalDelay() {
		int delay = 0;
		WasFrame[] frames = this.imageData.frames[this.spriteIndex];
		for (WasFrame frame : frames) {
			delay += frame.delay;
		}
		return delay;
	}

	public CanvasImage clone() {
		SpriteCanvasImage newImage = new SpriteCanvasImage(this.imageData, this.name);
		newImage.setLocation(this.x, this.y);
		newImage.setVisible(this.visible);
		newImage.setSpriteIndex(this.spriteIndex);
		newImage.setFrameIndex(this.frameIndex);
		return newImage;
	}

	public int getWidth() {
		return this.imageData.width;
	}

	public void setData(Object imageData) {
		this.imageData = ((WasImage) imageData);
	}
}