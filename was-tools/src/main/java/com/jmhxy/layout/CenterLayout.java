/*    */ package com.jmhxy.layout;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.Container;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Insets;
/*    */ 
/*    */ public class CenterLayout implements java.awt.LayoutManager
/*    */ {
/*    */   private Component center;
/*    */   
/*    */   public void addLayoutComponent(String name, Component comp)
/*    */   {
/* 14 */     synchronized (comp.getTreeLock()) {
/* 15 */       this.center = comp;
/*    */     }
/*    */   }
/*    */   
/*    */   public void layoutContainer(Container target) {
/* 20 */     if (this.center == null) return;
/* 21 */     synchronized (target.getTreeLock()) {
/* 22 */       Insets insets = target.getInsets();
/* 23 */       int maxwidth = target.getWidth() - (insets.left + insets.right);
/* 24 */       int maxheight = target.getHeight() - (insets.top + insets.bottom);
/* 25 */       Dimension d = this.center.getPreferredSize();
/* 26 */       if (d.width > maxwidth) d.width = maxwidth;
/* 27 */       if (d.height > maxheight) d.height = maxheight;
/* 28 */       int x = 0;int y = 0;
/* 29 */       x = (maxwidth - d.width) / 2 + insets.left;
/* 30 */       y = (maxheight - d.height) / 2 + insets.top;
/* 31 */       this.center.setLocation(x, y);
/* 32 */       this.center.setSize(d);
/*    */     }
/*    */   }
/*    */   
/*    */   public Dimension minimumLayoutSize(Container target) {
/* 37 */     synchronized (target.getTreeLock()) {
/* 38 */       Component c = target.getComponent(0);
/* 39 */       return c == null ? new Dimension() : c.getMinimumSize();
/*    */     }
/*    */   }
/*    */   
/*    */   public Dimension preferredLayoutSize(Container target) {
/* 44 */     synchronized (target.getTreeLock()) {
/* 45 */       Component c = target.getComponent(0);
/* 46 */       return c == null ? new Dimension() : c.getPreferredSize();
/*    */     }
/*    */   }
/*    */   
/*    */   public void removeLayoutComponent(Component comp) {
/* 51 */     synchronized (comp.getTreeLock()) {
/* 52 */       this.center = null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Desktop\WasTools2.0-all.jar!\com\jmhxy\layout\CenterLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */