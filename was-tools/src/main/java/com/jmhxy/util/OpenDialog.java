package com.jmhxy.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Window;
import java.io.File;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

public class OpenDialog extends JFileChooser {
	private static final long serialVersionUID = 1L;
	private JCheckBox btnAddToCurrent;
	private boolean isAddToCurrent;

	public OpenDialog() {
		setMultiSelectionEnabled(true);
	}

	public int showDialog(Component parent, File dir, String title, FileFilter filter, boolean isAddToCurrent) {
		setCurrentDirectory(dir);
		setFileFilter(filter);
		setDialogTitle(title);

		this.isAddToCurrent = isAddToCurrent;
		return showOpenDialog(parent);
	}

	protected JDialog createDialog(Component parent) throws HeadlessException {
		JDialog dialog = super.createDialog(parent);
		Container contentPanel = dialog.getContentPane();
		contentPanel.add(getBottomComp(), "South");
		dialog.pack();
		dialog.setLocationRelativeTo(parent);
		return dialog;
	}

	private Component getBottomComp() {
		if (this.btnAddToCurrent == null) {
			this.btnAddToCurrent = new JCheckBox("添加到当前画布");
			this.btnAddToCurrent.setSelected(this.isAddToCurrent);
		}
		return this.btnAddToCurrent;
	}

	public boolean isAddToCurrent() {
		return this.btnAddToCurrent.isSelected();
	}

	static Window getWindowForComponent(Component parentComponent) throws HeadlessException {
		if (parentComponent == null)
			return JOptionPane.getRootFrame();
		if (((parentComponent instanceof Frame)) || ((parentComponent instanceof Dialog)))
			return (Window) parentComponent;
		return getWindowForComponent(parentComponent.getParent());
	}
}