package com.mxxy.game.widget;

import java.awt.Graphics;

public interface AnimationDraw {
	/**
	 * 指定 x y
	 */
	void drawBitmap(Graphics g, int x, int y);
	/**
	 * 指定 x, y ,width,height
	 */
	void drawBitmap(Graphics g, int x, int y, int width, int height);

	void fadeIn(long paramLong);

	void fadeOut(long paramLong);

	int getWidth();

	int getHeight();

	boolean contains(int paramInt1, int paramInt2);
	
	/**
	 * 设置坐标
	 */
	void setLocation(int x, int y);

	/**
	 * 销毁
	 */
	void dispose();

}
