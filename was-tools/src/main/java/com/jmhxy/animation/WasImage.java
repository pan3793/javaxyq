/*    */ package com.jmhxy.animation;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WasImage
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public short[] palette;
/*    */   public int spriteCount;
/*    */   public int frameCount;
/*    */   public int width;
/*    */   public int height;
/*    */   public int xCenter;
/*    */   public int yCenter;
/*    */   public WasFrame[][] frames;
/*    */   
/*    */   public String toString()
/*    */   {
/* 36 */     StringBuffer buf = new StringBuffer();
/* 37 */     buf.append("精灵:\t");
/* 38 */     buf.append(this.spriteCount);
/* 39 */     buf.append("\r\n");
/* 40 */     buf.append("帧数:\t");
/* 41 */     buf.append(this.frameCount);
/* 42 */     buf.append("\r\n");
/* 43 */     buf.append("宽度:\t");
/* 44 */     buf.append(this.width);
/* 45 */     buf.append("\r\n");
/* 46 */     buf.append("高度:\t");
/* 47 */     buf.append(this.height);
/* 48 */     buf.append("\r\n");
/* 49 */     buf.append("中心X:\t");
/* 50 */     buf.append(this.xCenter);
/* 51 */     buf.append("\r\n");
/* 52 */     buf.append("中心Y:\t");
/* 53 */     buf.append(this.yCenter);
/* 54 */     buf.append("\r\n");
/* 55 */     return buf.toString();
/*    */   }
/*    */ }


/* Location:              D:\Desktop\WasTools2.0-all.jar!\com\jmhxy\animation\WasImage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */