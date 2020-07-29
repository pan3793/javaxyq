/*    */ package com.wildbean.wastools.comp;
/*    */ 
/*    */ import com.wildbean.wastools.core.CanvasImage;
/*    */ import java.awt.Component;
/*    */ import java.awt.event.MouseAdapter;
/*    */ import java.awt.event.MouseEvent;
/*    */ import java.awt.event.MouseMotionListener;
/*    */ import javax.swing.AbstractCellEditor;
/*    */ import javax.swing.JList;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LayerListCellEditor
/*    */   extends AbstractCellEditor
/*    */   implements ListCellEditor
/*    */ {
/* 18 */   protected LayerCellPanel editorComponent = new LayerCellPanel();
/*    */   private CanvasImage image;
/*    */   
/*    */   public LayerListCellEditor() {
/* 22 */     this.editorComponent.setInheritsPopupMenu(true);
/* 23 */     this.editorComponent.addMouseMotionListener(new MouseMotionListener() {
/*    */       public void mouseDragged(MouseEvent e) {
/* 25 */         LayerListCellEditor.this.stopCellEditing();
/*    */       }
/*    */       
/*    */       public void mouseMoved(MouseEvent e) {}
/* 29 */     });
/* 30 */     this.editorComponent.addMouseListener(new MouseAdapter() {
/*    */       public void mouseClicked(MouseEvent e) {
/* 32 */         LayerListCellEditor.this.stopCellEditing();
/*    */       }
/*    */     });
/*    */   }
/*    */   
/*    */   public Component getListCellEditorComponent(JList list, Object value, int index, boolean isSelected) {
/* 38 */     this.image = ((CanvasImage)value);
/* 39 */     this.editorComponent.setValue(list, this.image, index);
/*    */     
/* 41 */     return this.editorComponent;
/*    */   }
/*    */   
/*    */   public Object getCellEditorValue() {
/* 45 */     return this.image;
/*    */   }
/*    */ }


/* Location:              D:\Desktop\WasTools2.0-all.jar!\com\wildbean\wastools\comp\LayerListCellEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */