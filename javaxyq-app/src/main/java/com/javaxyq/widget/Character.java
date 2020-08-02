/**
 * 
 */
package com.javaxyq.widget;

import java.awt.Graphics;
import java.awt.Point;

/**
 * 角色
 * @author gongdewei
 * @date 2011-7-24 create
 */
public interface Character {

	String getId();
	
	boolean isReady();
	
	void initialize();
	
	/**
	 * 更新动画
	 * @param elapsedTime
	 */
	void update(long elapsedTime);
	
	/**
	 * 绘制到画布上
	 */
	void draw(Graphics g);
	
	/**
	 * UI绘制坐标位置
	 */
	Point getLocation();
	
	/**
	 * 将角色移动到指定坐标
	 */
	void moveTo(int x, int y);
	
	/**
	 * 移动增量
	 */
	void moveBy(int x, int y);
	
	/**
	 * 行走
	 */
	void walk();
	
	/**
	 * 奔跑
	 */
	void rush();
	
	/**
	 * 站立
	 */
	void stand();
	
	/**
	 * 转向
	 */
	void turn(int direction);

	void turn();

	int getDirection();
	
	/**
	 * 设置人物动作
	 */
	void action(String key);

	/**
	 * 是否继续移动
	 */
	boolean isMoveOn();

	/**
	 * 设置是否连续移动
	 */
	void setMoveOn(boolean moveOn);

}
