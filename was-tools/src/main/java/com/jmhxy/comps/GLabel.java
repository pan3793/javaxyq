/*    */ package com.jmhxy.comps;
/*    */ 
/*    */ import com.jmhxy.animation.Animation;
/*    */ import com.jmhxy.core.GameMain;
/*    */ import java.awt.Graphics;
/*    */ import javax.swing.Icon;
/*    */ import javax.swing.ImageIcon;
/*    */ import javax.swing.JLabel;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GLabel
/*    */   extends JLabel
/*    */ {
/*    */   private static final long serialVersionUID = 7814113439988128271L;
/*    */   private Animation anim;
/*    */   private long startTime;
/*    */   
/*    */   public GLabel(Animation anim)
/*    */   {
/* 22 */     this(null, new ImageIcon(anim.getImage()), 2);
/* 23 */     this.anim = anim;
/* 24 */     this.startTime = System.currentTimeMillis();
/* 25 */     setSize(anim.getWidth(), anim.getHeight());
/*    */   }
/*    */   
/*    */   public GLabel(String text) {
/* 29 */     this(text, null, 2);
/*    */   }
/*    */   
/*    */   public GLabel(Icon image) {
/* 33 */     this(null, image, 2);
/*    */   }
/*    */   
/*    */   public GLabel(String text, int horizontalAlignment) {
/* 37 */     this(text, null, horizontalAlignment);
/*    */   }
/*    */   
/*    */   public GLabel(Icon image, int horizontalAlignment) {
/* 41 */     this(null, image, horizontalAlignment);
/*    */   }
/*    */   
/*    */   public GLabel(String text, Icon icon, int horizontalAlignment) {
/* 45 */     super(text, icon, horizontalAlignment);
/* 46 */     setIgnoreRepaint(true);
/* 47 */     setBorder(null);
/*    */     
/*    */ 
/*    */ 
/* 51 */     setFont(GameMain.TEXT_FONT);
/*    */   }
/*    */   
/*    */   public void paint(Graphics g) {
/* 55 */     if (this.anim != null) {
/* 56 */       this.anim.updateToTime(System.currentTimeMillis() - this.startTime);
/* 57 */       setIcon(new ImageIcon(this.anim.getImage()));
/*    */     }
/* 59 */     super.paint(g);
/*    */   }
/*    */ }


/* Location:              D:\Desktop\WasTools2.0-all.jar!\com\jmhxy\comps\GLabel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */