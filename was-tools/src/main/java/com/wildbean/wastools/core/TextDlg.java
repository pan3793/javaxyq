package com.wildbean.wastools.core;

import com.jmhxy.comps.ChatFloatPanel;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

public class TextDlg extends JDialog {
	private static final long serialVersionUID = 1L;
	private JToolBar toolBar;
	private JButton btnFont;
	private JTextArea jtaInput;
	private JScrollPane jScrollPane1;
	private JButton btnPreview;
	private JButton btnCancel;
	private JButton btnOK;
	private JPanel btnPanel;
	private JButton btnSize;
	private JButton btnColor;
	private JComboBox cbxTextType;
	private JPanel inputPanel;
	private JPanel previewPanel;
	private ChatFloatPanel mhxyTextPanel;
	private String text;
	private EventHandler eventHandler = new EventHandler();

	public static void main(String[] args) {
		TextDlg inst = new TextDlg(null);
		inst.setVisible(true);
		System.exit(0);
	}

	public TextDlg(JFrame frame) {
		super(frame, true);
		initGUI();
		setLocationRelativeTo(frame);
		this.jtaInput.setText("示例文本#56");
		this.btnPreview.doClick();
	}

	private void initGUI() {
		try {
			setTitle("Text");
			getContentPane().add(getToolBar(), "North");
			getContentPane().add(getPreviewPanel(), "Center");
			getContentPane().add(getInputPanel(), "South");

			setDefaultCloseOperation(2);
			setSize(400, 300);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JPanel getPreviewPanel() {
		if (this.previewPanel == null) {
			this.previewPanel = new JPanel();
			this.mhxyTextPanel = new ChatFloatPanel();
			this.mhxyTextPanel.setBorder(BorderFactory.createEtchedBorder());
			this.previewPanel.add(this.mhxyTextPanel);
			Thread update = new Thread() {
				public void run() {
					try {
						while (TextDlg.this.mhxyTextPanel != null) {
							TextDlg.this.mhxyTextPanel.update(100L);
							TextDlg.this.previewPanel.repaint();
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
		return this.previewPanel;
	}

	public JPanel getInputPanel() {
		if (this.inputPanel == null) {
			this.inputPanel = new JPanel();
			BorderLayout inputPanelLayout = new BorderLayout();
			this.inputPanel.setLayout(inputPanelLayout);
			this.inputPanel.setPreferredSize(new Dimension(392, 92));

			this.jScrollPane1 = new JScrollPane();
			this.inputPanel.add(this.jScrollPane1, "Center");
			this.jtaInput = new JTextArea();
			this.jtaInput.addKeyListener(this.eventHandler);
			this.jtaInput.setLineWrap(true);
			this.jScrollPane1.setViewportView(this.jtaInput);

			this.btnPanel = new JPanel();
			FlowLayout btnPanelLayout = new FlowLayout();
			btnPanelLayout.setAlignment(2);
			btnPanelLayout.setHgap(20);
			this.inputPanel.add(getBtnPanel(), "South");
			this.btnPanel.setLayout(btnPanelLayout);

			this.btnOK = new JButton();
			this.btnPanel.add(this.btnOK);
			this.btnOK.setText("确定");
			this.btnOK.addActionListener(this.eventHandler);

			this.btnCancel = new JButton();
			this.btnPanel.add(this.btnCancel);
			this.btnCancel.setText("取消");
			this.btnCancel.addActionListener(this.eventHandler);

			this.btnPreview = new JButton();
			this.btnPanel.add(this.btnPreview);
			this.btnPreview.setText("预览");
			this.btnPreview.addActionListener(this.eventHandler);
		}

		return this.inputPanel;
	}

	public JToolBar getToolBar() {
		if (this.toolBar == null) {
			this.toolBar = new JToolBar();
			this.toolBar.setFloatable(false);

			this.cbxTextType = new JComboBox(new String[] { "普通文本", "梦幻西游" });
			this.cbxTextType.setMaximumSize(new Dimension(75, 24));
			this.cbxTextType.addActionListener(this.eventHandler);
			this.toolBar.add(this.cbxTextType);

			this.btnFont = new JButton();
			this.toolBar.add(this.btnFont);
			this.btnFont.setText("Font");

			this.btnColor = new JButton();
			this.toolBar.add(this.btnColor);
			this.btnColor.setText("Color");

			this.btnSize = new JButton();
			this.toolBar.add(this.btnSize);
			this.btnSize.setText("size");
		}

		return this.toolBar;
	}

	public JPanel getBtnPanel() {
		return this.btnPanel;
	}

	public String getText() {
		return this.text;
	}

	public ChatFloatPanel removeChatPanel() {
		ChatFloatPanel tmp = this.mhxyTextPanel;
		this.mhxyTextPanel = null;
		return tmp;
	}

	private class EventHandler implements ActionListener, KeyListener {
		private EventHandler() {
		}

		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			if (source != TextDlg.this.jtaInput)
				if (source == TextDlg.this.btnPreview) {
					TextDlg.this.mhxyTextPanel.setText(TextDlg.this.jtaInput.getText());
				} else if (source == TextDlg.this.btnOK) {
					TextDlg.this.mhxyTextPanel.setText(TextDlg.this.jtaInput.getText());
					TextDlg.this.text = TextDlg.this.jtaInput.getText();
					TextDlg.this.dispose();
				} else if (source == TextDlg.this.btnCancel) {
					TextDlg.this.dispose();
				}
		}

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == 10) {
				TextDlg.this.mhxyTextPanel.setText(TextDlg.this.jtaInput.getText());
				e.consume();
			}
		}

		public void keyReleased(KeyEvent e) {
		}

		public void keyTyped(KeyEvent e) {
		}
	}
}