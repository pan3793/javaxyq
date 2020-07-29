package com.jmhxy.comps;

import com.jmhxy.animation.Animation;
import com.jmhxy.core.GameMain;
import com.jmhxy.core.SpriteFactory;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class GFormattedLabel extends JComponent {
	private static final long serialVersionUID = 336384462632376970L;
	private transient ArrayList<Object> sectionList;
	private transient long currTime;
	private static HashMap<String, Animation> faceAnimations = new HashMap(100);
	private String lastStr;
	private String text;

	public GFormattedLabel() {
		this(null);
	}

	public GFormattedLabel(String text) {
		init();
		this.text = text;
		parseText(text);
	}

	private void init() {
		this.sectionList = new ArrayList();
		setBackground(Color.RED);
		setForeground(Color.BLUE);
		setIgnoreRepaint(true);
		setBorder(null);
		setOpaque(false);
		setSize(98, 16);
	}

	private void parseText(String text) {
		this.sectionList.clear();
		if (text == null) {
			return;
		}
		Pattern pattern = Pattern.compile(
				"#([RGBKYWnbur#]|[1-9]\\d|[0-9]|c[0-9A-Fa-f]?[0-9A-Fa-f]?[0-9A-Fa-f]?[0-9A-Fa-f]?[0-9A-Fa-f]?[0-9A-Fa-f]?)|[^#]+");

		Matcher m = pattern.matcher(text);

		while (m.find()) {
			String section = m.group();
			if (section.startsWith("#")) {
				Animation anim = (Animation) faceAnimations.get(section);
				if ((anim == null) && (section.charAt(1) >= '0') && (section.charAt(1) <= '9')) {
					anim = SpriteFactory.loadAnimation("/Resources/表情/" + section + ".was");
					if (anim != null) {
						faceAnimations.put(section, anim);
					}
				}
				if (anim != null) {
					this.sectionList.add(anim);
				} else if (section.startsWith("#c")) {
					Color color = Color.decode("0x" + section.substring(2));
					this.sectionList.add(color);
				} else if (section.equals("#R")) {
					this.sectionList.add(Color.RED);
				} else if (section.equals("#G")) {
					this.sectionList.add(Color.GREEN);
				} else if (section.equals("#B")) {
					this.sectionList.add(Color.BLUE);
				} else if (section.equals("#W")) {
					this.sectionList.add(Color.WHITE);
				} else if (section.equals("#K")) {
					this.sectionList.add(Color.BLACK);
				} else if (section.equals("#Y")) {
					this.sectionList.add(Color.YELLOW);
				} else if (section.equals("#r")) {
					this.sectionList.add(new Integer(-1));
				} else if (section.equals("#n")) {
					this.sectionList.add(Color.WHITE);
				}
			} else {
				this.sectionList.add(section);
			}
		}
	}

	public void update(long elapsedTime) {
		this.currTime += elapsedTime;
		int totalDuration = getTotalDuration();
		if (this.currTime < 0L) {
			this.currTime += totalDuration;
		} else if (this.currTime > totalDuration)
			this.currTime %= totalDuration;
	}

	public void paint(Graphics g) {
		g.setFont(GameMain.TEXT_FONT);
		g.setColor(Color.WHITE);
		int maxwidth = getWidth();
		int rowWidth = 0;
		int y = 0;
		int rowHeight = 0;
		int count = this.sectionList.size();
		int start = 0;
		int x = 0;
		for (int i = 0; i < count; i++) {
			Object obj = this.sectionList.get(i);
			if ((obj instanceof String)) {
				String str = (String) obj;
				FontMetrics fm = g.getFontMetrics();
				rowHeight = Math.max(rowHeight, fm.getHeight());
				int dx = fm.stringWidth(str);
				if (rowWidth + dx <= maxwidth) {
					rowWidth += dx;
				} else {
					Point p = paintFormattedText(g, x, y, maxwidth, rowHeight, start, i + 1);
					start = i + 1;
					rowWidth = p.x;
					rowHeight = fm.getHeight();
					x = p.x;
					y = p.y;
				}
			} else if ((obj instanceof Animation)) {
				Animation anim = (Animation) obj;
				if (anim.getWidth() + rowWidth > maxwidth) {
					paintFormattedText(g, x, y, maxwidth, rowHeight, start, i);
					start = i;
					x = 0;
					y += rowHeight;
					rowWidth = anim.getWidth();
					rowHeight = anim.getHeight();
				} else {
					rowHeight = Math.max(rowHeight, anim.getHeight());
					rowWidth += anim.getWidth();
				}
			} else if ((obj instanceof Integer)) {
				paintFormattedText(g, x, y, maxwidth, rowHeight, start, i + 1);
				start = i;
				x = 0;
				y += rowHeight;
				rowWidth = 0;
				rowHeight = 0;
			}
		}
		paintFormattedText(g, x, y, maxwidth, rowHeight, start, count);
	}

	private Point paintFormattedText(Graphics g, int x, int y, int width, int rowh, int start, int end) {
		FontMetrics fm = g.getFontMetrics();
		if (this.lastStr != null) {
			g.drawString(this.lastStr, 0, y + rowh - fm.getLeading());
			this.lastStr = null;
		}
		for (int i = start; i < end; i++) {
			Object obj = this.sectionList.get(i);
			if ((obj instanceof String)) {
				String str = (String) obj;
				int len = str.length();
				int begin = 0;
				int dx = 0;
				for (int index = 0; index < len;) {
					dx = fm.charWidth(str.charAt(index));
					while (x + dx <= width) {
						index++;
						if (index >= len)
							break;
						dx += fm.charWidth(str.charAt(index));
					}
					String s = str.substring(begin, index);
					if ((i == end - 1) && (index >= len) && (x + dx < width) && (this.sectionList.size() > end)) {
						this.lastStr = s;
						Object nextObj = this.sectionList.get(end);
						if ((nextObj instanceof Animation)) {
							Animation anim = (Animation) nextObj;
							if (anim.getWidth() + x + dx > width) {
								g.drawString(s, x, y + rowh - fm.getLeading());
								this.lastStr = null;
							}
						}
					} else {
						g.drawString(s, x, y + rowh - fm.getLeading());
					}

					if ((index < len) || (x + dx == width)) {
						begin = index;
						x = 0;
						y += rowh;
						rowh = fm.getHeight();
					} else {
						x += fm.stringWidth(s);
					}
				}
			} else if ((obj instanceof Color)) {
				g.setColor((Color) obj);
			} else if ((obj instanceof Animation)) {
				Animation anim = (Animation) obj;
				anim.updateToTime(this.currTime);
				g.drawImage(anim.getImage(), x, y + rowh - anim.getHeight() + fm.getLeading(), null);
				x += anim.getWidth();
			}
		}
		return new Point(x, y);
	}

	public static void main(String[] args) {
		String text = "#35这是#52#r一个#R《梦幻西游》#B#52#r聊天#r格式文本:#G大家#c008000新#R年#Y事#G事#B如#R意#B#u好!#35#52#R恭贺新禧！！";
		JFrame frame = new JFrame("GFormattedLabelText");
		frame.setBackground(Color.BLACK);
		GFormattedLabel label = new GFormattedLabel(text);
		label.setLocation(10, 10);
		frame.add(label);
		final JTextField textField = new JTextField();
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				label.setFormattedText(textField.getText());
			}
		});
		frame.add(textField, "South");
		frame.setSize(400, 300);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(2);
		frame.setVisible(true);
		Thread update = new Thread() {
			public void run() {
				try {
					while (true) {
						label.repaint();
						sleep(100L);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		update.setDaemon(true);
		update.start();
	}

	public void setFormattedText(String text) {
		this.text = text;
		parseText(text);
	}

	public Dimension computeSize(int maxwidth) {
		if (this.text == null)
			return new Dimension(0, 0);
		int rowWidth = 0;
		int y = 0;
		int rowHeight = 0;
		int count = this.sectionList.size();
		int start = 0;
		int x = 0;
		BufferedImage temp = new BufferedImage(maxwidth, 16, 8);
		Graphics g = temp.getGraphics();
		g.setFont(GameMain.TEXT_FONT);
		FontMetrics fm = g.getFontMetrics(g.getFont());
		for (int i = 0; i < count; i++) {
			Object obj = this.sectionList.get(i);
			if ((obj instanceof String)) {
				String str = (String) obj;
				rowHeight = Math.max(rowHeight, fm.getHeight());
				int dx = fm.stringWidth(str);
				if (rowWidth + dx <= maxwidth) {
					rowWidth += dx;
				} else {
					Point p = paintFormattedText(g, x, y, maxwidth, rowHeight, start, i + 1);
					start = i + 1;
					rowWidth = p.x;
					rowHeight = fm.getHeight();
					x = p.x;
					y = p.y;
				}
			} else if ((obj instanceof Animation)) {
				Animation anim = (Animation) obj;
				if (anim.getWidth() + rowWidth > maxwidth) {
					paintFormattedText(g, x, y, maxwidth, rowHeight, start, i);
					start = i;
					x = 0;
					y += rowHeight;
					rowWidth = anim.getWidth();
					rowHeight = anim.getHeight();
				} else {
					rowHeight = Math.max(rowHeight, anim.getHeight());
					rowWidth += anim.getWidth();
				}
			} else if ((obj instanceof Integer)) {
				paintFormattedText(g, x, y, maxwidth, rowHeight, start, i + 1);
				start = i;
				x = 0;
				y += rowHeight;
				rowWidth = 0;
				rowHeight = 0;
			}
		}
		Point p = paintFormattedText(g, x, y, maxwidth, rowHeight, start, count);
		if (y == 0) {
			maxwidth = p.x;
		}
		return new Dimension(maxwidth, y + rowHeight + fm.getDescent());
	}

	public int getFrameCount() {
		int count = 1;
		for (Object obj : this.sectionList) {
			if ((obj instanceof Animation)) {
				Animation anim = (Animation) obj;
				count = Math.max(count, anim.getFrameCount());
			}
		}
		return count;
	}

	public void updateToTime(long totalTime) {
		this.currTime = totalTime;
		int totalDuration = getTotalDuration();
		if (this.currTime < 0L) {
			this.currTime += totalDuration;
		} else if (this.currTime > totalDuration)
			this.currTime %= totalDuration;
	}

	private void readObject(ObjectInputStream s) throws IOException {
		init();
		try {
			setFormattedText((String) s.readObject());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
		s.writeObject(this.text);
	}

	public int getTotalDuration() {
		return getFrameCount() * 100;
	}

	public int getFrameIndex() {
		return (int) (this.currTime / 100L);
	}
}

/*
 * Location:
 * D:\Desktop\WasTools2.0-all.jar!\com\jmhxy\comps\GFormattedLabel.class Java
 * compiler version: 6 (50.0) JD-Core Version: 0.7.1
 */