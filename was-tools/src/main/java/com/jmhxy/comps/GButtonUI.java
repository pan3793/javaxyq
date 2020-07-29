/*     */ package com.jmhxy.comps;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.plaf.basic.BasicButtonUI;
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
/*     */ class GButtonUI
/*     */   extends BasicButtonUI
/*     */ {
/*     */   protected void paintIcon(Graphics g, JComponent c, Rectangle iconRect)
/*     */   {
/* 112 */     GButton btn = (GButton)c;
/* 113 */     ButtonModel model = btn.getModel();
/* 114 */     if ((btn.isAutoOffset()) && (model.isArmed()) && (model.isPressed())) {
/* 115 */       this.defaultTextShiftOffset = 1;
/*     */     } else {
/* 117 */       this.defaultTextShiftOffset = 0;
/*     */     }
/* 119 */     setTextShiftOffset();
/* 120 */     super.paintIcon(g, c, iconRect);
/*     */   }
/*     */   
/*     */   protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {
/* 124 */     AbstractButton b = (AbstractButton)c;
/* 125 */     ButtonModel model = b.getModel();
/* 126 */     if ((model.isArmed()) && (model.isPressed())) {
/* 127 */       this.defaultTextShiftOffset = 1;
/*     */     } else {
/* 129 */       this.defaultTextShiftOffset = 0;
/*     */     }
/* 131 */     setTextShiftOffset();
/* 132 */     super.paintText(g, c, textRect, text);
/*     */   }
/*     */ }


/* Location:              D:\Desktop\WasTools2.0-all.jar!\com\jmhxy\comps\GButtonUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */