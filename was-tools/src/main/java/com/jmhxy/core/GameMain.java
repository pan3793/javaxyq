package com.jmhxy.core;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;

public class GameMain {
	private static ActionMap actionMap = new ActionMap();
	static final int ANIMATION_INTERVAL = 100;
	static String applicationName = "JavaMHXY - 飞燕逍遥";
	private static GameCanvas canvas;
	public static FontMetrics fontMetrics;
	private static GameWindow gameWindow;
	public static String homeURL;
	public static final Font TEXT_FONT = new Font("宋体", 0, 14);

	public static final Color TEXT_NAME_BGCOLOR = new Color(27, 26, 18);

	public static final Color TEXT_NAME_COLOR = new Color(118, 229, 128);

	public static final Font TEXT_NAME_FONT = new Font("宋体", 0, 16);

	public static final Color TEXT_NAME_NPC_COLOR = new Color(219, 197, 63);
	static String version;
	public static int windowHeight;
	public static int windowWidth;

	public static void actionPerformed(Object source, String cmd) {
		Action action = actionMap.get(cmd);
		action.actionPerformed(new ActionEvent(source, 1001, cmd));
	}

	public static void addWindowListener(WindowListener handler) {
		gameWindow.addWindowListener(handler);
	}

	public static void addWindowStateListener(WindowStateListener handler) {
		gameWindow.addWindowStateListener(handler);
	}

	public static void fullScreen() {
		if (gameWindow.isFullScreen())
			gameWindow.restoreScreen();
		else
			gameWindow.setFullScreen();
	}

	public static ActionMap getActionMap() {
		return actionMap;
	}

	public static GameCanvas getGameCanvas() {
		return gameWindow.getGameCanvas();
	}

	public static void getTarget() {
	}

	public static GameWindow getWindow() {
		return gameWindow;
	}

	private static void loadGame(DisplayMode displayMode) {
		LoadingDialog loading = new LoadingDialog();
		loading.start();
		gameWindow = new GameWindow(displayMode);
		canvas = gameWindow.getGameCanvas();
		gameWindow.setLocationRelativeTo(null);
		fontMetrics = gameWindow.getFontMetrics(TEXT_NAME_FONT);

		Image blankImage = new ImageIcon("").getImage();
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Cursor blankCursor = toolkit.createCustomCursor(blankImage, new Point(0, 0), "blankCursor");
		gameWindow.setCursor(blankCursor);

		ScriptLoader.pretreatScripts("scripts/");
		ScriptLoader.load("GameCore.xml");

		gameWindow.setTitle(applicationName);
		gameWindow.setVisible(true);
		loading.stop();
	}

	public static void main(String[] args) throws InterruptedException {
		int width = 640;
		int height = 480;
		DisplayMode displayMode;
		if (args.length == 3) {
			width = Integer.valueOf(args[0]).intValue();
			height = Integer.valueOf(args[1]).intValue();
			displayMode = new DisplayMode(width, height, Integer.valueOf(args[2]).intValue(), 0);
		} else {
			displayMode = new DisplayMode(width, height, 16, 0);
		}
		windowWidth = width;
		windowHeight = height;
		loadGame(displayMode);
	}

	public static void showDialog(Component dialog) {
		if (dialog != null)
			if (dialog.getParent() == canvas) {
				canvas.remove(dialog);
			} else {
				canvas.add(dialog);
				canvas.setComponentZOrder(dialog, 0);
			}
	}
}

/*
 * Location: D:\Desktop\WasTools2.0-all.jar!\com\jmhxy\core\GameMain.class Java
 * compiler version: 6 (50.0) JD-Core Version: 0.7.1
 */