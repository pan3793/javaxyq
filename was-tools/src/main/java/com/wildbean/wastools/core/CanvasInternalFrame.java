package com.wildbean.wastools.core;

import com.jmhxy.layout.CenterLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

public class CanvasInternalFrame extends JInternalFrame {
	private static final long serialVersionUID = 1L;
	private Canvas canvas;
	private JPanel statusPanel;
	private JButton btnPlay;
	private JButton btnNext;
	private JButton btnPervious;
	private JButton btnFirstFrame;
	private JComboBox cbxZoom;
	private static final String FRAME_FIRST = "first frame";
	private static final String FRAME_NEXT = "next frame";
	private static final String FRAME_PERV = "pervious frame";
	private static final String FRAME_PLAY_OR_STOP = "play or stop";
	private static final String CANVAS_ZOOM = "zoom canvas";
	private JLabel lblFrameIndex;
	private EventHandler eventHandler = new EventHandler();

	public Canvas getCanvas() {
		return this.canvas;
	}

	public CanvasInternalFrame(int width, int height) {
		super("未命名", true, true, true, true);
		this.canvas = new Canvas(width, height);
		this.canvas.setCanvasName("未命名");
		initGUI();
		autoResize(width, height);
		this.canvas.addPropertyChangeListener(this.eventHandler);
	}

	public void autoResize(int width, int height) {
		width += 10;
		height += 49;
		if (width < 150)
			width = 150;
		if (height < 140)
			height = 140;
		setSize(width, height);
	}

	public CanvasInternalFrame(CanvasImage canvasImage, String name) {
		this(new Canvas(canvasImage), name);
	}

	public CanvasInternalFrame(Canvas canvas, String name) {
		super("", true, true, true, true);
		this.canvas = canvas;
		initGUI();
		setTitle(name);
		autoResize(canvas.getCanvasWidth(), canvas.getCanvasHeight());
		canvas.addPropertyChangeListener(this.eventHandler);
	}

	private void initGUI() {
		try {
			setMinimumSize(new Dimension(170, 140));
			JPanel panel = new JPanel(new CenterLayout());
			panel.add("CenterLayout", this.canvas);
			panel.setBackground(Color.LIGHT_GRAY);
			final JScrollPane scroll = new JScrollPane(panel);
			scroll.setBorder(null);
			this.canvas.addPropertyChangeListener("RefreshViewport", new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent evt) {
					scroll.getViewport().setViewSize((Dimension) evt.getNewValue());
					CanvasInternalFrame.this.setTitle(CanvasInternalFrame.this.canvas.getCanvasName());
				}
			});
			getContentPane().add(scroll, "Center");
			getContentPane().add(getStatusPanel(), "South");
			this.cbxZoom.setSelectedIndex(3);
			setDefaultCloseOperation(0);
			setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Component getStatusPanel() {
		if (this.statusPanel == null) {
			this.statusPanel = new JPanel();
			FlowLayout statusPanelLayout = new FlowLayout();
			statusPanelLayout.setAlignment(0);
			statusPanelLayout.setVgap(0);
			statusPanelLayout.setHgap(1);
			this.statusPanel.setLayout(statusPanelLayout);
			this.statusPanel.setPreferredSize(new Dimension(10, 14));
			this.statusPanel.add(getCbxZoom());
			this.statusPanel.add(getBtnFirstFrame());
			this.statusPanel.add(getBtnPervious());
			this.statusPanel.add(getBtnPlay());
			this.statusPanel.add(getBtnNext());

			this.lblFrameIndex = new JLabel("1", 0);
			this.lblFrameIndex.setPreferredSize(new Dimension(20, 14));
			this.statusPanel.add(this.lblFrameIndex);
		}

		return this.statusPanel;
	}

	private JComboBox getCbxZoom() {
		if (this.cbxZoom == null) {
			ComboBoxModel cbxZoomModel = new DefaultComboBoxModel(
					new String[] { "400%", "300%", "200%", "100%", "50%", "25%" });
			this.cbxZoom = new JComboBox(cbxZoomModel);
			this.cbxZoom.setPreferredSize(new Dimension(50, 14));
			this.cbxZoom.setFont(new Font("Tahoma", 0, 10));
			this.cbxZoom.setActionCommand("zoom canvas");
			this.cbxZoom.addActionListener(this.eventHandler);
		}
		return this.cbxZoom;
	}

	private JButton getBtnFirstFrame() {
		if (this.btnFirstFrame == null) {
			this.btnFirstFrame = new JButton();
			this.btnFirstFrame.setText("<<");
			this.btnFirstFrame.setToolTipText("第一帧");
			this.btnFirstFrame.setPreferredSize(new Dimension(20, 14));
			this.btnFirstFrame.setFont(new Font("Tahoma", 0, 10));
			this.btnFirstFrame.setMargin(new Insets(0, 0, 0, 0));
			this.btnFirstFrame.setActionCommand("first frame");
			this.btnFirstFrame.addActionListener(this.eventHandler);
		}
		return this.btnFirstFrame;
	}

	private JButton getBtnPervious() {
		if (this.btnPervious == null) {
			this.btnPervious = new JButton();
			this.btnPervious.setText("<|");
			this.btnPervious.setToolTipText("前一帧");
			this.btnPervious.setPreferredSize(new Dimension(20, 14));
			this.btnPervious.setFont(new Font("Tahoma", 0, 10));
			this.btnPervious.setMargin(new Insets(0, 0, 0, 0));
			this.btnPervious.setActionCommand("pervious frame");
			this.btnPervious.addActionListener(this.eventHandler);
		}
		return this.btnPervious;
	}

	private JButton getBtnPlay() {
		if (this.btnPlay == null) {
			this.btnPlay = new JButton();
			this.btnPlay.setText(">");
			this.btnPlay.setToolTipText("播放/停止");
			this.btnPlay.setPreferredSize(new Dimension(20, 14));
			this.btnPlay.setFont(new Font("Tahoma", 0, 10));
			this.btnPlay.setMargin(new Insets(0, 0, 0, 0));
			this.btnPlay.setActionCommand("play or stop");
			this.btnPlay.addActionListener(this.eventHandler);
		}
		return this.btnPlay;
	}

	private JButton getBtnNext() {
		if (this.btnNext == null) {
			this.btnNext = new JButton();
			this.btnNext.setText("|>");
			this.btnNext.setToolTipText("后一帧");
			this.btnNext.setPreferredSize(new Dimension(20, 14));
			this.btnNext.setFont(new Font("Tahoma", 0, 10));
			this.btnNext.setMargin(new Insets(0, 0, 0, 0));
			this.btnNext.setActionCommand("next frame");
			this.btnNext.addActionListener(this.eventHandler);
		}
		return this.btnNext;
	}

	private class EventHandler implements ActionListener, PropertyChangeListener {
		private EventHandler() {
		}

		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			if ("first frame".equals(cmd))
				CanvasInternalFrame.this.canvas.firstFrame();
			else if ("next frame".equals(cmd))
				CanvasInternalFrame.this.canvas.nextFrame();
			else if ("pervious frame".equals(cmd))
				CanvasInternalFrame.this.canvas.prevFrame();
			else if ("play or stop".equals(cmd)) {
				if (CanvasInternalFrame.this.canvas.isPlaying())
					CanvasInternalFrame.this.canvas.pause();
				else
					CanvasInternalFrame.this.canvas.play();
			} else
				"zoom canvas".equals(cmd);
		}

		public void propertyChange(PropertyChangeEvent evt) {
			String cmd = evt.getPropertyName();
			if ("canvas frame index".equals(cmd))
				CanvasInternalFrame.this.lblFrameIndex
						.setText(String.valueOf(CanvasInternalFrame.this.canvas.getFrameIndex()));
		}
	}
}