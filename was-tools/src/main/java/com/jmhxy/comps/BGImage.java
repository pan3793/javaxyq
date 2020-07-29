/*    */ package com.jmhxy.comps;
/*    */ 
/*    */ import java.awt.Image;
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
/*    */ public class BGImage
/*    */ {
/*    */   public Image image;
/*    */   public int x;
/*    */   public int y;
/*    */   public int width;
/*    */   public boolean visible;
/*    */   public int height;
/*    */   
/*    */   public BGImage(Image image, int x, int y, int width, int height)
/*    */   {
/* 25 */     this.image = image;
/* 26 */     this.x = x;
/* 27 */     this.y = y;
/* 28 */     this.width = width;
/* 29 */     this.height = height;
/* 30 */     this.visible = true;
/*    */   }
/*    */ }


/* Location:              D:\Desktop\WasTools2.0-all.jar!\com\jmhxy\comps\BGImage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */