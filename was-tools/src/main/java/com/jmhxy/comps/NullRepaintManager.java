/*    */ package com.jmhxy.comps;
/*    */ 
/*    */ import javax.swing.JComponent;
/*    */ 
/*    */ public class NullRepaintManager extends javax.swing.RepaintManager
/*    */ {
/*    */   public static void install()
/*    */   {
/*  9 */     javax.swing.RepaintManager repaintManager = new NullRepaintManager();
/* 10 */     repaintManager.setDoubleBufferingEnabled(false);
/* 11 */     javax.swing.RepaintManager.setCurrentManager(repaintManager);
/*    */   }
/*    */   
/*    */   public synchronized void addInvalidComponent(JComponent invalidComponent) {}
/*    */   
/*    */   public void addDirtyRegion(JComponent c, int x, int y, int w, int h) {}
/*    */   
/*    */   public void markCompletelyDirty(JComponent aComponent) {}
/*    */   
/*    */   public void paintDirtyRegions() {}
/*    */ }


/* Location:              D:\Desktop\WasTools2.0-all.jar!\com\jmhxy\comps\NullRepaintManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */