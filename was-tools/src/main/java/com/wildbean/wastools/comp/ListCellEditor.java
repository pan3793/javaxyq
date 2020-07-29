package com.wildbean.wastools.comp;

import java.awt.Component;
import javax.swing.CellEditor;
import javax.swing.JList;

public abstract interface ListCellEditor
  extends CellEditor
{
  public abstract Component getListCellEditorComponent(JList paramJList, Object paramObject, int paramInt, boolean paramBoolean);
}


/* Location:              D:\Desktop\WasTools2.0-all.jar!\com\wildbean\wastools\comp\ListCellEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */