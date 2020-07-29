/*    */ package com.jmhxy.core;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import javax.swing.JDialog;
/*    */ import javax.swing.JLabel;
/*    */ 
/*    */ public class LoadingDialog extends JDialog
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private long beginTime;
/*    */   
/*    */   public LoadingDialog()
/*    */   {
/* 14 */     setTitle("Loading");
/* 15 */     setSize(200, 100);
/* 16 */     JLabel label = new JLabel("Loading, wait a moment...");
/* 17 */     label.setHorizontalAlignment(0);
/* 18 */     add(label);
/* 19 */     setLocationRelativeTo(null);
/*    */   }
/*    */   
/* 22 */   public void start() { this.beginTime = System.currentTimeMillis();
/* 23 */     System.out.println("starting...");
/* 24 */     setVisible(true);
/*    */   }
/*    */   
/* 27 */   public void stop() { long endTime = System.currentTimeMillis();
/* 28 */     System.out.println("game loaded! total " + (endTime - this.beginTime) + "ms");
/* 29 */     dispose();
/*    */   }
/*    */ }


/* Location:              D:\Desktop\WasTools2.0-all.jar!\com\jmhxy\core\LoadingDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */