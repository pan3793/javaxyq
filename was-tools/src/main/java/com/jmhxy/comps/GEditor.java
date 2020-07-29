/*    */ package com.jmhxy.comps;
/*    */ 
/*    */ import com.jmhxy.core.GameMain;
/*    */ import java.awt.Color;
/*    */ import javax.swing.JTextField;
/*    */ 
/*    */ public class GEditor
/*    */   extends JTextField
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public GEditor()
/*    */   {
/* 14 */     this("");
/*    */   }
/*    */   
/*    */   public GEditor(String text) {
/* 18 */     super(text);
/* 19 */     setFont(GameMain.TEXT_FONT);
/* 20 */     setForeground(Color.WHITE);
/* 21 */     setCaretColor(Color.WHITE);
/* 22 */     setBorder(null);
/* 23 */     setOpaque(false);
/*    */   }
/*    */ }


/* Location:              D:\Desktop\WasTools2.0-all.jar!\com\jmhxy\comps\GEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */