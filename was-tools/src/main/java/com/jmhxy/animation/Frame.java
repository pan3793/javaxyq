/*    */ package com.jmhxy.animation;
/*    */ 
/*    */ import java.awt.Image;
/*    */ 
/*    */ public class Frame implements java.io.Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -7750015391484154342L;
/*    */   Image image;
/*    */   long endTime;
/*    */   int centerX;
/*    */   int centerY;
/*    */   public int width;
/*    */   public int height;
/*    */   
/*    */   public Frame(Image image, long endTime, int centerX, int centerY)
/*    */   {
/* 17 */     this.image = image;
/* 18 */     this.endTime = endTime;
/* 19 */     this.centerX = centerX;
/* 20 */     this.centerY = centerY;
/* 21 */     this.width = image.getWidth(null);
/* 22 */     this.height = image.getHeight(null);
/*    */   }
/*    */   
/* 25 */   public Image getImage() { return this.image; }
/*    */   
/*    */   public Image getFixedImage(int width, int height) {
/* 28 */     java.awt.image.BufferedImage buf = new java.awt.image.BufferedImage(width, height, 2);
/* 29 */     java.awt.Graphics g = buf.getGraphics();
/* 30 */     g.drawImage(this.image, -this.centerX, -this.centerY, null);
/* 31 */     g.dispose();
/* 32 */     return buf;
/*    */   }
/*    */ }


/* Location:              D:\Desktop\WasTools2.0-all.jar!\com\jmhxy\animation\Frame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */