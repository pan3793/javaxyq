package com.jmhxy.core;

import com.jmhxy.animation.Player;
import com.jmhxy.animation.Sprite;
import com.jmhxy.comps.DialogFactory;
import com.jmhxy.comps.GButton;
import com.jmhxy.comps.GDialog;
import com.jmhxy.comps.GEditor;
import com.jmhxy.comps.GFormattedLabel;
import com.jmhxy.comps.GLabel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class ScriptLoader {
	private static SAXBuilder saxBuilder = new SAXBuilder();

	private static String dirPath = "";

	public static void load(String filename) {
		System.out.println("loading script " + filename + " ... ");
		try {
			load(new FileInputStream(new File(dirPath, filename)));
		} catch (FileNotFoundException e) {
			System.err.println("load script failed!");
			e.printStackTrace();
		}

		System.out.println(filename + " ok");
	}

	public static void load(InputStream in) {
		GameCanvas canvas = GameMain.getGameCanvas();
		ActionMap actionMap = GameMain.getActionMap();
		Document doc;
		try {
			doc = saxBuilder.build(in);
			Element root = doc.getRootElement();
			String rootElementText = root.getName();
			Element actions;
			if ("GameCore".equals(rootElementText)) {
				GameMain.applicationName = root.getAttributeValue("applicationName");
				GameMain.version = root.getAttributeValue("version");
				GameMain.homeURL = root.getAttributeValue("homeURL");

				InputMap inputMap = canvas.getInputMap();
				Element hotKeys = root.getChild("HotKeys");
				parseHotKeys(hotKeys, inputMap);

				actions = root.getChild("Actions");
				List<Element> list = actions.getChildren();
				for (Element elem : list) {
					parseAction(elem);
				}
				canvas.setActionMap(actionMap);

				Element listeners = root.getChild("Listeners");
				parseListenners(listeners, GameMain.getGameCanvas());

				Element loading = root.getChild("Loading");
				parseLoading(loading);
			} else if ("Actions".equals(rootElementText)) {
				List<Element> list = root.getChildren();
				for (Element elem : list)
					parseAction(elem);
			} else if ("Dialog".equals(rootElementText)) {
				parseDialog(root);
			} else if ("Dialogs".equals(rootElementText)) {
				List<Element> dialogList = root.getChildren();
				for (Element elem : dialogList)
					parseDialog(elem);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException localIOException) {
				}
		}
	}

	private static void parseListenners(Element listeners, Component comp) {
		List<Element> listenerList = listeners.getChildren();

		for (Element elem : listenerList) {
			String listName = elem.getName();
			String className = elem.getAttributeValue("class");
			try {
				Object handler = Class.forName(className).newInstance();
				if ("MouseListener".equals(listName))
					comp.addMouseListener((MouseListener) handler);
				else if ("MouseMotionListener".equals(listName))
					comp.addMouseMotionListener((MouseMotionListener) handler);
				else if ("MouseWheelListener".equals(listName))
					comp.addMouseWheelListener((MouseWheelListener) handler);
				else if ("WindowListener".equals(listName))
					GameMain.addWindowListener((WindowListener) handler);
				else if ("WindowStateListener".equals(listName))
					GameMain.addWindowStateListener((WindowStateListener) handler);
			} catch (Exception e) {
				System.err.println("Listenners parsing error: " + e.getMessage());
			}
		}
	}

	private static void parseHotKeys(Element hotKeys, InputMap inputMap) {
		List<Element> keylist = hotKeys.getChildren();
		int keyCode = 0;
		int modifiers = 0;
		String actionID = "";

		for (Element elem : keylist)
			if ("HotKey".equals(elem.getName())) {
				String keystr = elem.getAttributeValue("keycode");
				if ((keystr.charAt(0) >= '0') && (keystr.charAt(0) <= '9'))
					keyCode = Integer.parseInt(keystr);
				else
					keyCode = keystr.charAt(0);
				String mask = elem.getAttributeValue("mask");
				if ("alt".equals(mask))
					modifiers = 512;
				else if ("ctrl".equals("mask"))
					modifiers = 128;
				else if ("shift".equals("mask"))
					modifiers = 64;
				else
					modifiers = 0;
				actionID = elem.getAttributeValue("actionID");
				inputMap.put(KeyStroke.getKeyStroke(keyCode, modifiers), actionID);
			} else {
				System.err.println("unknown tag in HotKeys:" + elem.getName());
			}
	}

	private static void parseLoading(Element loading) {
		GameWindow window = GameMain.getWindow();

		Element mapElem = loading.getChild("Map");
		if (mapElem != null) {
			String path = mapElem.getAttributeValue("path");
			window.getGameCanvas().bgImage = SpriteFactory.loadMap(path);
		}

		Element dialogs = loading.getChild("Dialogs");
		GDialog dlg;
		if (dialogs != null) {
			List<Element> dialogList = dialogs.getChildren();
			for (Element elem : dialogList) {
				if ("Dialog".equalsIgnoreCase(elem.getName())) {
					dlg = DialogFactory.getDialog(elem.getAttributeValue("id"));
					GameMain.showDialog(dlg);
				}
			}
		}

		Element sprites = loading.getChild("Sprites");
		if (sprites != null) {
			List<Element> npcList = sprites.getChildren();
			for (Element elem : npcList) {
				try {
					Sprite sprite = SpriteFactory.loadSprite(elem.getAttributeValue("was"));
					sprite.setAngle(Integer.valueOf(elem.getAttributeValue("angle")).intValue());
					int x = Integer.valueOf(elem.getAttributeValue("x")).intValue();
					int y = Integer.valueOf(elem.getAttributeValue("y")).intValue();
					window.addSprite(sprite, x, y);
				} catch (Exception e) {
					System.err.println("load error:" + elem);
				}
			}
		}

		Element npcs = loading.getChild("NPCs");
		if (npcs != null) {
			List<Element> npcList = npcs.getChildren();
			for (Element elem : npcList) {
				try {
					Player npc = parsePlayer(elem);
					npc.setNameForeground(GameMain.TEXT_NAME_NPC_COLOR);
					window.addNPC(npc);
				} catch (Exception e) {
					System.err.println("load npc error:" + elem);
				}
			}
		}

		Element playerElem = loading.getChild("Player");
		if (playerElem != null)
			window.setPlayer(parsePlayer(playerElem));
	}

	private static Player parsePlayer(Element playerElem) {
		int angle = Integer.valueOf(playerElem.getAttributeValue("angle")).intValue();
		int state = Integer.valueOf(playerElem.getAttributeValue("state")).intValue();
		Point p = getLocation(playerElem);
		String name = playerElem.getAttributeValue("name");
		String chat = playerElem.getAttributeValue("chat");
		Player player = new Player(name);
		player.setChatText(chat);
		List<Element> stateList = playerElem.getChildren();
		for (Element elem : stateList) {
			Sprite character = SpriteFactory.loadSprite(elem.getAttributeValue("character"));
			Sprite weapon = SpriteFactory.loadSprite(elem.getAttributeValue("weapon"));
			int stateId = Integer.valueOf(elem.getAttributeValue("id")).intValue();
			player.put(stateId, character, weapon);
		}
		player.setState(state);
		player.setAngle(angle);
		player.setLocation(p);
		return player;
	}

	private static void parseDialog(Element dialogElem) {
		GDialog dialog = null;

		Point p = getLocation(dialogElem);
		Dimension d = getSize(dialogElem);
		if (d == null) {
			d = new Dimension();
		}
		String id = dialogElem.getAttributeValue("id");
		String background = dialogElem.getAttributeValue("background");
		String closable = dialogElem.getAttributeValue("closable");
		String movable = dialogElem.getAttributeValue("movable");
		dialog = new GDialog(d.width, d.height);
		dialog.setLocation(p);
		dialog.setLayout(null);
		if (background != null) {
			Sprite s = SpriteFactory.loadSprite(background);
			dialog.addBGImage(s.getImage(), 0, 0);
		}
		if (closable != null) {
			dialog.setClosable(Boolean.valueOf(closable));
		}
		if (movable != null) {
			dialog.setMovable(Boolean.valueOf(movable));
		}

		List<Element> children = dialogElem.getChildren();
		for (Element elem : children) {
			if ("Button".equalsIgnoreCase(elem.getName()))
				dialog.add(parseButton(elem));
			else if ("Text".equalsIgnoreCase(elem.getName()))
				dialog.add(parseText(elem));
			else if ("FormattedText".equalsIgnoreCase(elem.getName()))
				dialog.add(parseFormattedText(elem));
			else if ("Action".equalsIgnoreCase(elem.getName()))
				parseAction(elem);
			else if ("Image".equalsIgnoreCase(elem.getName()))
				dialog.add(parseImage(elem));
			else if ("Sprite".equalsIgnoreCase(elem.getName()))
				dialog.add(parseSprite(elem));
			else if ("Editor".equalsIgnoreCase(elem.getName())) {
				dialog.add(parseEditor(elem));
			}
		}
		dialog.setActionMap(GameMain.getActionMap());
		DialogFactory.addDialog(id, dialog);
	}

	private static Component parseFormattedText(Element elem) {
		Point p = getLocation(elem);
		Dimension d = getSize(elem);
		String name = elem.getAttributeValue("name");
		String text = elem.getAttributeValue("text");
		GFormattedLabel label = new GFormattedLabel(text);
		label.setName(name);
		label.setLocation(p);
		label.setSize(d);
		return label;
	}

	private static void parseAction(Element elem) {
		ActionMap actionMap = GameMain.getActionMap();

		String actionID = elem.getAttributeValue("id");
		String className = elem.getAttributeValue("class");
		try {
			Action action = (Action) Class.forName(className).newInstance();
			action.putValue("ActionCommandKey", actionID);
			actionMap.put(actionID, action);
		} catch (Exception e) {
			System.err.println("parse <Action> error:" + e.getMessage());
			e.printStackTrace();
		}
	}

	private static Component parseSprite(Element elem) {
		GLabel imgLabel = null;
		int index = 0;
		String filename = elem.getAttributeValue("path");
		try {
			index = Integer.parseInt(elem.getAttributeValue("index"));
		} catch (Exception localException) {
		}
		Point location = getLocation(elem);
		Dimension dimension = getSize(elem);

		imgLabel = new GLabel(SpriteFactory.loadAnimation(filename, index));
		if (location != null) {
			imgLabel.setLocation(location);
		}
		if (dimension != null) {
			imgLabel.setSize(dimension);
		}
		return imgLabel;
	}

	private static Component parseImage(Element elem) {
		JLabel imgLabel = null;
		String filename = elem.getAttributeValue("path");
		Point location = getLocation(elem);
		Dimension dimension = getSize(elem);

		imgLabel = new JLabel(new ImageIcon(SpriteFactory.loadImage(filename)));
		if (location != null) {
			imgLabel.setLocation(location);
		}
		if (dimension != null) {
			imgLabel.setSize(dimension);
		} else {
			int width = imgLabel.getIcon().getIconWidth();
			int height = imgLabel.getIcon().getIconHeight();
			imgLabel.setSize(width, height);
		}
		return imgLabel;
	}

	private static Dimension getSize(Element elem) {
		int height;
		int width;
		try {
			width = Integer.parseInt(elem.getAttributeValue("width"));
			height = Integer.parseInt(elem.getAttributeValue("height"));
		} catch (Exception e) {
			return null;
		}
		return new Dimension(width, height);
	}

	private static Point getLocation(Element elem) {
		int y;
		int x;
		try {
			x = Integer.parseInt(elem.getAttributeValue("x"));
			y = Integer.parseInt(elem.getAttributeValue("y"));
		} catch (Exception e) {
			return null;
		}
		return new Point(x, y);
	}

	private static Component parseEditor(Element elem) {
		GEditor editor = new GEditor();
		String actionID = elem.getAttributeValue("actionID");
		Point location = getLocation(elem);
		Dimension dimension = getSize(elem);
		if (location != null) {
			editor.setLocation(location);
		}
		if (dimension != null) {
			editor.setSize(dimension);
		}
		if (actionID != null) {
			Action action = GameMain.getActionMap().get(actionID);
			editor.addActionListener(action);
			editor.setActionCommand(actionID);
		}
		return editor;
	}

	private static Component parseText(Element elem) {
		Point p = getLocation(elem);
		Dimension d = getSize(elem);
		String name = elem.getAttributeValue("name");
		String text = elem.getAttributeValue("text");
		String color = elem.getAttributeValue("color");
		String align = elem.getAttributeValue("align");
		GLabel label = new GLabel(text);
		label.setName(name);
		label.setLocation(p);
		label.setSize(d);
		if ((color != null) && (color.equals("white"))) {
			label.setForeground(Color.WHITE);
		}
		if (align != null) {
			if (align.equals("center"))
				label.setHorizontalAlignment(0);
			else if (align.equals("right")) {
				label.setHorizontalAlignment(4);
			}
		}
		return label;
	}

	private static Component parseButton(Element elem) {
		String actionID = elem.getAttributeValue("actionID");
		String text = elem.getAttributeValue("text");
		String wasName = elem.getAttributeValue("was");
		String enable = elem.getAttributeValue("enable");
		Point location = getLocation(elem);
		Dimension dimension = getSize(elem);
		GButton button = new GButton();
		if (actionID != null) {
			button.setAction(GameMain.getActionMap().get(actionID));
		}
		if (enable != null) {
			button.setEnabled(Boolean.valueOf(enable).booleanValue());
		}
		button.setText(text);
		button.setLocation(location);
		button.setActionCommand(actionID);
		button.init(SpriteFactory.loadSprite(wasName));
		if (dimension != null) {
			button.setSize(dimension);
		}
		return button;
	}

	public static void pretreatScripts(String dirPath) {
		System.out.print("pretreat Scripts...");
		dirPath = dirPath;
		File dir = new File(dirPath);
		if (dir.isDirectory()) {
			String[] scriptlist = dir.list();
			for (String filename : scriptlist) {
				if (filename.endsWith(".xml")) {
					try {
						File file = new File(dir, filename);
						Document doc = saxBuilder.build(file);
						Element root = doc.getRootElement();
						String rootText = root.getName();
						if ("Dialogs".equalsIgnoreCase(rootText)) {
							List<Element> dialogList = root.getChildren();
							for (Element elem : dialogList) {
								if ("Dialog".equalsIgnoreCase(elem.getName()))
									DialogFactory.addScript(elem.getAttributeValue("id"), filename);
							}
						} else if ("Dialog".equalsIgnoreCase(rootText)) {
							DialogFactory.addScript(root.getAttributeValue("id"), filename);
						}
					} catch (Exception e) {
						System.err.println("不能预处理脚本文件:" + filename);
						e.printStackTrace();
					}
				}
			}
		}
		System.out.println("ok");
	}

	public static Rectangle parseRectangle(Element elem) {
		return null;
	}
}

/*
 * Location: D:\Desktop\WasTools2.0-all.jar!\com\jmhxy\core\ScriptLoader.class
 * Java compiler version: 6 (50.0) JD-Core Version: 0.7.1
 */