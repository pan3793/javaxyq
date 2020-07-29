/*     */ package com.wildbean.wastools.comp;
/*     */ 
/*     */ import com.wildbean.wastools.core.CanvasImage;
/*     */ import java.awt.Component;
/*     */ import java.awt.Rectangle;
/*     */ import java.io.Serializable;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.ListCellRenderer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LayerListCellRenderer
/*     */   extends LayerCellPanel
/*     */   implements ListCellRenderer, Serializable
/*     */ {
/*     */   public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
/*     */   {
/*  42 */     CanvasImage canvasImage = (CanvasImage)value;
/*  43 */     setValue(list, canvasImage, index);
/*     */     
/*  45 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void invalidate() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void repaint() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void revalidate() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void repaint(long tm, int x, int y, int width, int height) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void repaint(Rectangle r) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void firePropertyChange(String propertyName, Object oldValue, Object newValue)
/*     */   {
/* 102 */     if ((propertyName == "text") || (
/* 103 */       ((propertyName == "font") || (propertyName == "foreground")) && 
/* 104 */       (oldValue != newValue) && (getClientProperty("html") != null)))
/*     */     {
/* 106 */       super.firePropertyChange(propertyName, oldValue, newValue);
/*     */     }
/*     */   }
/*     */   
/*     */   public void firePropertyChange(String propertyName, byte oldValue, byte newValue) {}
/*     */   
/*     */   public void firePropertyChange(String propertyName, char oldValue, char newValue) {}
/*     */   
/*     */   public void firePropertyChange(String propertyName, short oldValue, short newValue) {}
/*     */   
/*     */   public void firePropertyChange(String propertyName, int oldValue, int newValue) {}
/*     */   
/*     */   public void firePropertyChange(String propertyName, long oldValue, long newValue) {}
/*     */   
/*     */   public void firePropertyChange(String propertyName, float oldValue, float newValue) {}
/*     */   
/*     */   public void firePropertyChange(String propertyName, double oldValue, double newValue) {}
/*     */   
/*     */   public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {}
/*     */ }


/* Location:              D:\Desktop\WasTools2.0-all.jar!\com\wildbean\wastools\comp\LayerListCellRenderer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */