/*    */ package com.wildbean.wastools.comp;
/*    */ 
/*    */ import com.jmhxy.animation.Animation;
/*    */ import java.awt.Component;
/*    */ import java.awt.Graphics;
/*    */ import javax.swing.Icon;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WasIcon
/*    */   implements Icon
/*    */ {
/*    */   private int width;
/*    */   private int height;
/*    */   private Animation animation;
/*    */   
/*    */   public WasIcon(Animation animation)
/*    */   {
/* 20 */     this.animation = animation;
/* 21 */     this.width = animation.getWidth();
/* 22 */     this.height = animation.getHeight();
/*    */   }
/*    */   
/*    */   public int getIconHeight() {
/* 26 */     return this.height;
/*    */   }
/*    */   
/*    */   public int getIconWidth() {
/* 30 */     return this.width;
/*    */   }
/*    */   
/*    */   public void paintIcon(Component c, Graphics g, int x, int y)
/*    */   {
/* 35 */     this.animation.update(100L);
/* 36 */     g.clearRect(0, 0, this.width, this.height);
/* 37 */     g.drawImage(this.animation.getImage(), x, y, c);
/*    */   }
/*    */ }


/* Location:              D:\Desktop\WasTools2.0-all.jar!\com\wildbean\wastools\comp\WasIcon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */