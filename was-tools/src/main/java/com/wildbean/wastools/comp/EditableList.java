package com.wildbean.wastools.comp;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputListener;

public class EditableList extends JList {
	private static final long serialVersionUID = 1L;
	private ListCellEditor cellEditor;
	protected Component editorComp;
	private int editingIndex;
	private DefaultListModel model;
	private Object lastSelectedItem;
	private EventHandler eventHandler = new EventHandler();

	public EditableList(DefaultListModel dataModel) {
		super(dataModel);
		this.model = dataModel;
		addMouseListener(this.eventHandler);
		addFocusListener(this.eventHandler);
		addKeyListener(this.eventHandler);
		addListSelectionListener(this.eventHandler);
	}

	public void setModel(ListModel model) {
		super.setModel(model);
		this.model = ((DefaultListModel) model);
	}

	public boolean canSelectRow(Point point) {
		int index = locationToIndex(point);
		if ((index != -1) && (this.cellEditor != null)) {
			Rectangle rect = getCellBounds(index, index);
			if (point.y > rect.y + rect.height)
				return false;
			LayerCellPanel editorComp = (LayerCellPanel) getCellRenderer().getListCellRendererComponent(this,
					this.model.getElementAt(index), index, isSelectedIndex(index), true);
			editorComp.setBounds(rect);
			editorComp.doLayout();
			return editorComp.canSelect(point.x - rect.x, point.y - rect.y);
		}
		return false;
	}

	public void cancelEditing() {
		remove(this.editorComp);
		this.editingIndex = -1;
	}

	public void stopEditing() {
		if (this.editingIndex != -1) {
			Object value = this.cellEditor.getCellEditorValue();
			this.model.setElementAt(value, this.editingIndex);
		}
		remove(this.editorComp);
		this.editingIndex = -1;
	}

	public boolean isEditing() {
		return this.editorComp != null;
	}

	public void setCellEditor(ListCellEditor cellEditor) {
		this.cellEditor = cellEditor;
		this.cellEditor.addCellEditorListener(this.eventHandler);
	}

	public ListCellEditor getCellEditor() {
		return this.cellEditor;
	}

	public Object getLastSelectedItem() {
		return this.lastSelectedItem;
	}

	public void setLastSelectedItem(Object lastSelectedItem) {
		this.lastSelectedItem = lastSelectedItem;
	}

	private class EventHandler
			implements MouseInputListener, FocusListener, KeyListener, CellEditorListener, ListSelectionListener {
		private EventHandler() {
		}

		public void mouseClicked(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
			if (EditableList.this.isEditing()) {
				EditableList.this.stopEditing();
			}
			if (EditableList.this.canSelectRow(e.getPoint())) {
				EditableList.this.lastSelectedItem = EditableList.this.getSelectedValue();
			} else
				EditableList.this.setSelectedValue(EditableList.this.lastSelectedItem, false);
		}

		public void mouseReleased(MouseEvent e) {
			int index = EditableList.this.locationToIndex(e.getPoint());
			if ((index != -1) && (EditableList.this.cellEditor != null)) {
				Rectangle rect = EditableList.this.getCellBounds(index, index);
				if (e.getY() > rect.y + rect.height)
					return;
				EditableList.this.setSelectedValue(EditableList.this.lastSelectedItem, false);
				EditableList.this.editorComp = EditableList.this.cellEditor.getListCellEditorComponent(
						EditableList.this, EditableList.this.model.getElementAt(index), index,
						EditableList.this.isSelectedIndex(index));

				EditableList.this.editorComp.setBounds(rect);
				EditableList.this.add(EditableList.this.editorComp);
				EditableList.this.editorComp.validate();
				EditableList.this.editingIndex = index;

				int x = e.getX() - rect.x;
				int y = e.getY() - rect.y;
				Component source = EditableList.this.editorComp.getComponentAt(x, y);
				if ((source != EditableList.this.editorComp) && (source != null)) {
					x -= source.getX();
					y -= source.getY();
				}
				EditableList.this.editorComp.dispatchEvent(new MouseEvent(source, 500, e.getWhen(), e.getModifiers(), x,
						y, e.getClickCount(), e.isPopupTrigger(), e.getButton()));
				EditableList.this.editorComp.repaint();
			}
			EditableList.this.setSelectedValue(EditableList.this.lastSelectedItem, false);
		}

		public void focusGained(FocusEvent e) {
			if (EditableList.this.isEditing())
				EditableList.this.cancelEditing();
		}

		public void focusLost(FocusEvent e) {
			if (EditableList.this.isEditing())
				EditableList.this.cancelEditing();
		}

		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();
			if (keyCode == 27) {
				if (EditableList.this.isEditing())
					EditableList.this.cancelEditing();
			} else if ((keyCode == 10) && (EditableList.this.isEditing()))
				EditableList.this.stopEditing();
		}

		public void keyReleased(KeyEvent e) {
		}

		public void keyTyped(KeyEvent e) {
		}

		public void mouseDragged(MouseEvent e) {
			if (EditableList.this.isEditing())
				EditableList.this.stopEditing();
		}

		public void mouseMoved(MouseEvent e) {
		}

		public void editingCanceled(ChangeEvent e) {
			EditableList.this.cancelEditing();
		}

		public void editingStopped(ChangeEvent e) {
			EditableList.this.stopEditing();
		}

		public void valueChanged(ListSelectionEvent e) {
			Object selectedValue = EditableList.this.getSelectedValue();
			if ((selectedValue != null) && (EditableList.this.lastSelectedItem != selectedValue)
					&& ((EditableList.this.lastSelectedItem == null)
							|| (!EditableList.this.model.contains(EditableList.this.lastSelectedItem))))
				EditableList.this.lastSelectedItem = selectedValue;
		}
	}
}