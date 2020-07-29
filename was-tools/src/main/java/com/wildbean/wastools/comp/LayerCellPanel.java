package com.wildbean.wastools.comp;

import com.jmhxy.util.Utils;
import com.wildbean.wastools.core.CanvasImage;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.ListModel;
import javax.swing.UIManager;
import javax.swing.border.Border;

public class LayerCellPanel extends JPanel {
	private JList list;
	private static final long serialVersionUID = 1L;
	private static final Icon ICON_VISIBLE = Utils.loadIcon("visible.png");

	private static final Icon ICON_LINKED_THIS = Utils.loadIcon("linked_current.png");

	private static final Icon ICON_CURRENT = Utils.loadIcon("current.png");

	private static final Icon ICON_LINKED = Utils.loadIcon("linked.png");
	private static final String CMD_VISIBLE_LINKED = "visible of all linked layers";
	private static final String CMD_VISIBLE_NON_LINKED = "visible of all non-linked layers";
	private static final String CMD_VISIBLE_OTHERS = "visible of other layers";
	private JLabel visibleLabel;
	private JLabel linkedLabel;
	private JLabel typeLabel;
	private JLabel nameLabel;
	private Color cellBackground = UIManager.getColor("Panel.background");

	private Color cellForeground = UIManager.getColor("Panel.foreground");

	private Color selectionForeground = UIManager.getColor("List.selectionForeground");

	private Color selectionBackground = UIManager.getColor("List.selectionBackground");

	private Border iconBorder = BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLUE);

	private Border cellBorder = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY);
	private CanvasImage image;
	private Dimension iconSize = new Dimension(16, 16);
	private JPopupMenu indexMenu;
	private int index;
	private JPopupMenu visibleMenu;
	private EventHandler eventHandler = new EventHandler();

	public Color getCellForeground() {
		return this.cellForeground;
	}

	public void setCellForeground(Color cellForeground) {
		this.cellForeground = cellForeground;
	}

	public Color getSelectionBackground() {
		return this.selectionBackground;
	}

	public void setSelectionBackground(Color selectionBackground) {
		this.selectionBackground = selectionBackground;
	}

	public Color getSelectionForeground() {
		return this.selectionForeground;
	}

	public void setSelectionForeground(Color selectionForeground) {
		this.selectionForeground = selectionForeground;
	}

	public void setCellBackground(Color cellBackground) {
		this.cellBackground = cellBackground;
	}

	public LayerCellPanel() {
		addMouseListener(this.eventHandler);
		this.visibleLabel = new JLabel(ICON_VISIBLE);
		this.visibleLabel.setBorder(this.iconBorder);
		this.visibleLabel.setPreferredSize(this.iconSize);
		this.visibleLabel.addMouseListener(this.eventHandler);

		this.linkedLabel = new JLabel();
		this.linkedLabel.setBorder(this.iconBorder);
		this.linkedLabel.setPreferredSize(this.iconSize);
		this.linkedLabel.addMouseListener(this.eventHandler);
		this.typeLabel = new JLabel("", 2);
		this.typeLabel.setBorder(this.iconBorder);
		this.typeLabel.setForeground(Color.BLUE);
		this.typeLabel.addMouseListener(this.eventHandler);
		this.nameLabel = new JLabel("", 2);
		this.nameLabel.addMouseListener(this.eventHandler);
		GridBagLayout layout = new GridBagLayout();
		layout.rowWeights = new double[] { 0.1D };
		layout.rowHeights = new int[] { 20 };
		layout.columnWeights = new double[] { 0.0D, 0.0D, 0.0D, 0.1D };
		layout.columnWidths = new int[] { 18, 18, 35, 200 };
		setLayout(layout);
		setBorder(this.cellBorder);
		add(this.visibleLabel, new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
		add(this.linkedLabel, new GridBagConstraints(1, 0, 1, 1, 0.0D, 0.0D, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
		add(this.typeLabel, new GridBagConstraints(2, 0, 1, 1, 0.0D, 0.0D, 10, 2, new Insets(0, 2, 0, 0), 0, 0));
		add(this.nameLabel, new GridBagConstraints(3, 0, 1, 1, 0.1D, 0.0D, 10, 2, new Insets(0, 5, 0, 0), 0, 0));
	}

	public void setValue(JList list, CanvasImage image, int index) {
		this.image = image;
		this.list = list;
		this.index = index;
		configure();
	}

	private void configure() {
		this.nameLabel.setText(this.image.getName());
		this.typeLabel.setIcon(this.image.getIcon());
		this.typeLabel.setText(String.valueOf(this.image.getSpriteIndex() + 1));
		this.visibleLabel.setIcon(this.image.isVisible() ? ICON_VISIBLE : null);
		this.linkedLabel.setIcon((this.image.isLinked()) || (this.image.isBase()) ? ICON_LINKED
				: this.image.isLinked(this.list.getSelectedValue()) ? ICON_LINKED_THIS : null);
		this.indexMenu = null;
		if (this.list.isSelectedIndex(this.index)) {
			this.linkedLabel.setIcon(ICON_CURRENT);
			setBackground(getSelectionBackground());
			setForeground(getSelectionForeground());
		} else {
			setBackground(getCellBackground());
			setForeground(getCellForeground());
		}
	}

	public Color getCellBackground() {
		return this.cellBackground;
	}

	public CanvasImage getValue() {
		return this.image;
	}

	private JPopupMenu getIndexMenu() {
		if (this.indexMenu == null) {
			this.indexMenu = new JPopupMenu();
			int count = this.image.getSpriteCount();
			for (int i = 0; i < count; i++) {
				JMenuItem item = new JMenuItem(String.valueOf(i + 1));
				item.setActionCommand("sprite" + i);
				item.addActionListener(this.eventHandler);
				this.indexMenu.add(item);
			}
		}
		return this.indexMenu;
	}

	private JPopupMenu getVisibleMenu() {
		if (this.visibleMenu == null) {
			this.visibleMenu = new JPopupMenu();
			JMenuItem item;
			item = new JMenuItem("显示/隐藏链接的图层");
			item.setActionCommand("visible of all linked layers");
			item.addActionListener(this.eventHandler);
			this.visibleMenu.add(item);

			item = new JMenuItem("显示/隐藏未链接的图层");
			item.setActionCommand("visible of all non-linked layers");
			item.addActionListener(this.eventHandler);
			this.visibleMenu.add(item);

			item = new JMenuItem("显示/隐藏所有其它图层");
			item.setActionCommand("visible of other layers");
			item.addActionListener(this.eventHandler);
			this.visibleMenu.add(item);
		}

		return this.visibleMenu;
	}

	public boolean canSelect(int x, int y) {
		return x >= this.typeLabel.getX();
	}

	public boolean isAlignBase(CanvasImage base) {
		ListModel model = this.list.getModel();
		int size = model.getSize();
		for (int i = 0; i < size; i++) {
			CanvasImage img = (CanvasImage) model.getElementAt(i);
			if (img.getLinkedBase() == base) {
				return true;
			}
		}
		return false;
	}

	public void replaceLinkedBase(CanvasImage base, CanvasImage newBase) {
		ListModel model = this.list.getModel();
		int size = model.getSize();
		for (int i = 0; i < size; i++) {
			CanvasImage img = (CanvasImage) model.getElementAt(i);
			if (img.getLinkedBase() == base) {
				img.setLinkedBase(newBase);
			}
		}
		base.setBase(false);
		if (newBase != null)
			newBase.setBase(true);
	}

	private class EventHandler implements MouseListener, ActionListener {
		private EventHandler() {
		}

		public void mouseClicked(MouseEvent e) {
			Object source = e.getSource();
			if (source == LayerCellPanel.this.visibleLabel)
				LayerCellPanel.this.image.setVisible(!LayerCellPanel.this.image.isVisible());
			else if (source == LayerCellPanel.this.linkedLabel) {
				if (!LayerCellPanel.this.list.isSelectedIndex(LayerCellPanel.this.index)) {
					CanvasImage base = (CanvasImage) LayerCellPanel.this.list.getSelectedValue();
					if (LayerCellPanel.this.image.getLinkedBase() != base) {
						LayerCellPanel.this.image.setLinkedBase(base);
						base.setBase(true);
					} else if (base != null) {
						LayerCellPanel.this.image.setLinkedBase(null);
						base.setBase(LayerCellPanel.this.isAlignBase(base));
					} else {
						LayerCellPanel.this.image.setLinkedBase(null);
					}
					LayerCellPanel.this.replaceLinkedBase(LayerCellPanel.this.image, null);
				}
			} else if ((source == LayerCellPanel.this.typeLabel) && (LayerCellPanel.this.image.getSpriteCount() > 1)) {
				LayerCellPanel.this.getIndexMenu().show(LayerCellPanel.this.typeLabel, 0,
						-5 + LayerCellPanel.this.typeLabel.getY() + LayerCellPanel.this.typeLabel.getHeight());
			}

			LayerCellPanel.this.configure();
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			if (cmd.startsWith("sprite")) {
				int index = Integer.parseInt(cmd.substring(6));
				LayerCellPanel.this.image.setSpriteIndex(index);
				LayerCellPanel.this.image.fireSpriteIndexChanged();
			}
		}
	}
}