package com.wildbean.wastools.core;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CopyLayerDlg extends JDialog {
	private JPanel srcPanel;
	private JPanel destPanel;
	private JTextField jtfDestCanvasName;
	private JComboBox cbxDestCanvas;
	private JTextField jtfDestName;
	private JLabel lblSrcName;
	private JButton btnCancel;
	private JButton btnOK;
	private CanvasImage canvasImage;
	private List<Canvas> canvasList;
	private Canvas destCanvas;
	private boolean isNewCanvas;
	private CanvasImage srcImage;
	private EventHandler eventHandler = new EventHandler();

	public CopyLayerDlg(JFrame frame, CanvasImage image, List<Canvas> canvasList) {
		super(frame, true);

		this.srcImage = image;
		this.canvasImage = image.clone();
		this.canvasList = canvasList;
		initGUI();
		setLocationRelativeTo(frame);
		configure();
	}

	private void configure() {
		this.lblSrcName.setText(this.canvasImage.getName());
		this.jtfDestName.setText(this.canvasImage.getName() + " 副本");
		DefaultComboBoxModel cbxModel = new DefaultComboBoxModel(this.canvasList.toArray());
		cbxModel.addElement("新建");
		this.cbxDestCanvas.setModel(cbxModel);
	}

	private void initGUI() {
		try {
			getContentPane().setLayout(null);
			getContentPane().add(getSrcPanel());
			getContentPane().add(getDestPanel());

			this.btnOK = new JButton();
			getContentPane().add(this.btnOK);
			this.btnOK.setText("确定(O)");
			this.btnOK.setMnemonic('O');
			this.btnOK.setBounds(70, 231, 80, 28);
			this.btnOK.addActionListener(this.eventHandler);

			this.btnCancel = new JButton();
			getContentPane().add(this.btnCancel);
			this.btnCancel.setText("取消(C)");
			this.btnCancel.setMnemonic('C');
			this.btnCancel.setBounds(252, 231, 80, 28);
			this.btnCancel.addActionListener(this.eventHandler);

			setTitle("复制图层");
			setDefaultCloseOperation(2);
			pack();
			setSize(400, 300);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JPanel getSrcPanel() {
		if (this.srcPanel == null) {
			this.srcPanel = new JPanel();
			this.srcPanel.setLayout(null);
			this.srcPanel.setBounds(14, 14, 364, 91);
			this.srcPanel.setBorder(BorderFactory.createTitledBorder("源"));

			JLabel jLabel1 = new JLabel();
			this.srcPanel.add(jLabel1);
			jLabel1.setText("复制:");
			jLabel1.setBounds(14, 21, 63, 28);
			jLabel1.setSize(63, 24);

			this.lblSrcName = new JLabel();
			this.srcPanel.add(this.lblSrcName);
			this.lblSrcName.setBounds(77, 21, 273, 28);
			this.lblSrcName.setSize(273, 24);

			this.jtfDestName = new JTextField();
			this.srcPanel.add(this.jtfDestName);
			this.jtfDestName.setBounds(77, 49, 273, 28);
			this.jtfDestName.setSize(273, 24);

			JLabel jLabel2 = new JLabel();
			this.srcPanel.add(jLabel2);
			jLabel2.setText("重命名(R):");
			jLabel2.setDisplayedMnemonic('R');
			jLabel2.setBounds(14, 49, 63, 28);
			jLabel2.setSize(63, 24);
			jLabel2.setLabelFor(this.jtfDestName);
		}

		return this.srcPanel;
	}

	public JPanel getDestPanel() {
		if (this.destPanel == null) {
			this.destPanel = new JPanel();
			this.destPanel.setLayout(null);
			this.destPanel.setBounds(14, 119, 364, 98);
			this.destPanel.setBorder(BorderFactory.createTitledBorder("目的"));

			this.cbxDestCanvas = new JComboBox();
			this.destPanel.add(this.cbxDestCanvas);
			this.cbxDestCanvas.setBounds(77, 21, 273, 28);
			this.cbxDestCanvas.setSize(273, 24);
			this.cbxDestCanvas.addActionListener(this.eventHandler);

			JLabel jLabel3 = new JLabel();
			this.destPanel.add(jLabel3);
			jLabel3.setText("画布(D):");
			jLabel3.setDisplayedMnemonic('D');
			jLabel3.setBounds(14, 21, 63, 28);
			jLabel3.setSize(63, 24);
			jLabel3.setLabelFor(this.cbxDestCanvas);

			this.jtfDestCanvasName = new JTextField();
			this.destPanel.add(this.jtfDestCanvasName);
			this.jtfDestCanvasName.setBounds(77, 56, 273, 28);
			this.jtfDestCanvasName.setSize(273, 24);
			this.jtfDestCanvasName.setEditable(false);

			JLabel jLabel4 = new JLabel();
			this.destPanel.add(jLabel4);
			jLabel4.setText("名称(N):");
			jLabel4.setDisplayedMnemonic('N');
			jLabel4.setBounds(14, 56, 63, 21);
			jLabel4.setLabelFor(this.jtfDestCanvasName);
		}

		return this.destPanel;
	}

	public Canvas getDestCanvas() {
		return this.destCanvas;
	}

	public boolean isNewCanvas() {
		return this.isNewCanvas;
	}

	private class EventHandler implements ActionListener {
		private EventHandler() {
		}

		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			if (source == CopyLayerDlg.this.cbxDestCanvas) {
				CopyLayerDlg.this.jtfDestCanvasName
						.setEditable(!(CopyLayerDlg.this.cbxDestCanvas.getSelectedItem() instanceof Canvas));
			} else if (source == CopyLayerDlg.this.btnOK) {
				CopyLayerDlg.this.canvasImage.setName(CopyLayerDlg.this.jtfDestName.getText());
				try {
					CopyLayerDlg.this.destCanvas = ((Canvas) CopyLayerDlg.this.cbxDestCanvas.getSelectedItem());
					int index = CopyLayerDlg.this.destCanvas.indexOfImage(CopyLayerDlg.this.srcImage);
					if (index == -1)
						index = 0;
					CopyLayerDlg.this.destCanvas.addImage(index, CopyLayerDlg.this.canvasImage, false);
				} catch (Exception ex) {
					CopyLayerDlg.this.destCanvas = new Canvas(CopyLayerDlg.this.canvasImage);
					CopyLayerDlg.this.destCanvas.setCanvasName(CopyLayerDlg.this.jtfDestCanvasName.getText());
					CopyLayerDlg.this.isNewCanvas = true;
				}
				CopyLayerDlg.this.dispose();
			} else if (source == CopyLayerDlg.this.btnCancel) {
				CopyLayerDlg.this.dispose();
			}
		}
	}
}