package com.jmhxy.core;

import com.jmhxy.animation.Animation;
import com.jmhxy.animation.Sprite;
import com.jmhxy.encoder.WasDecoder;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.PrintStream;
import java.util.TreeMap;
import javax.swing.ImageIcon;

public class SpriteFactory {
	private static TreeMap<String, Sprite> sprites = new TreeMap();

	public static Sprite loadCursor(String filename) {
		return loadSprite("/Resources/cursor/" + filename);
	}

	public static Image loadMap(String filename) {
		return new ImageIcon(GameMain.class.getResource(filename)).getImage();
	}

	public static Sprite loadSprite(String filename) {
		if (filename == null)
			return null;
		Sprite sprite = (Sprite) sprites.get(filename);
		if (sprite == null) {

			try {

				WasDecoder decoder = new WasDecoder();
				decoder.load(filename);

				int centerX = decoder.getCenterX();
				int centerY = decoder.getCenterY();
				sprite = new Sprite(decoder.getWidth(), decoder.getHeight(), centerX, centerY);
				int s = decoder.getSpriteCount();
				int f = decoder.getFrameCount();
				for (int i = 0; i < s; i++) {
					Animation anim = new Animation();
					for (int j = 0; j < f; j++) {
						int index = i * f + j;
						anim.addFrame(decoder.getFrame(index), decoder.getDelay(index) * 100, centerX, centerY);
					}
					sprite.addAnimation(anim);
				}
				sprites.put(filename, sprite);
			} catch (Exception e) {
				System.err.println("load was failed:" + filename + ":" + e.getClass());
				e.printStackTrace();
			}
		} else {
			try {
				return sprite.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		return sprite;
	}

	public static Animation loadAnimation(String filename, int index) {
		Sprite s = loadSprite(filename);
		return s == null ? null : s.getAnimation(index);
	}

	public static Animation loadAnimation(String filename) {
		return loadAnimation(filename, 0);
	}

	public static Image loadImage(String filename) {
		if (filename.endsWith(".was")) {
			Sprite s = loadSprite(filename);
			return s == null ? null : s.getImage();
		}
		return Toolkit.getDefaultToolkit().getImage(filename);
	}

	public static Animation getAnimationByID(int id) {
		String filename;

		if (id == 1133229609) {
			filename = "/Resources/fireworks/特效/蓝色妖姬.was";
		} else
			filename = "/Resources/fireworks/花卉/玫瑰2.was";
		Sprite sprite = (Sprite) sprites.get(filename);
		if (sprite == null)
			sprite = loadSprite(filename);
		return sprite.getAnimation(0);
	}
}

/*
 * Location: D:\Desktop\WasTools2.0-all.jar!\com\jmhxy\core\SpriteFactory.class
 * Java compiler version: 6 (50.0) JD-Core Version: 0.7.1
 */