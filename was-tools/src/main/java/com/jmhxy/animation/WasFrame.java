/*     */ package com.jmhxy.animation;
/*     */ 
/*     */ import java.awt.Point;
/*     */ import java.awt.image.WritableRaster;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class WasFrame
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  11 */   public int delay = 1;
/*     */   
/*     */ 
/*     */   public int height;
/*     */   
/*     */ 
/*     */   public int width;
/*     */   
/*     */ 
/*     */   public int xCenter;
/*     */   
/*     */ 
/*     */   public int yCenter;
/*     */   
/*     */ 
/*     */   public int[][] pixels;
/*     */   
/*     */ 
/*     */   public long endTime;
/*     */   
/*     */ 
/*     */ 
/*     */   public void draw(WritableRaster raster, int x, int y)
/*     */   {
/*  35 */     int[] imageData = (int[])null;
/*     */     
/*     */ 
/*  38 */     int index = 0;
/*     */     
/*     */     try
/*     */     {
/*  42 */       int minX = raster.getMinX();
/*  43 */       int minY = raster.getMinY();
/*  44 */       int maxX = raster.getWidth();
/*  45 */       int maxY = raster.getHeight();
/*  46 */       int clipWidth = this.width;
/*  47 */       int clipHeight = this.height;
/*  48 */       int deltaX = this.width - clipWidth;
/*  49 */       int deltaY = this.height - clipHeight;
/*  50 */       if (x < minX) {
/*  51 */         deltaX = minX - x;
/*  52 */         clipWidth -= minX - x;
/*  53 */         x = minX;
/*     */       }
/*  55 */       if (y < minY) {
/*  56 */         deltaY = minY - y;
/*  57 */         clipHeight -= minY - y;
/*  58 */         y = minY;
/*     */       }
/*  60 */       if (x + clipWidth > maxX)
/*  61 */         clipWidth = maxX - x;
/*  62 */       if (y + clipHeight > maxY)
/*  63 */         clipHeight = maxY - y;
/*  64 */       if ((clipWidth <= 0) || (clipHeight <= 0)) {
/*  65 */         return;
/*     */       }
/*  67 */       imageData = raster.getPixels(x, y, clipWidth, clipHeight, imageData);
/*  68 */       for (int row = deltaY; row < clipHeight + deltaY; row++) {
/*  69 */         for (int c = deltaX; c < clipWidth + deltaX; c++)
/*     */         {
/*  71 */           int index0 = index;
/*  72 */           int r = imageData[(index++)];
/*  73 */           int g = imageData[(index++)];
/*  74 */           int b = imageData[(index++)];
/*  75 */           int a = imageData[(index++)];
/*     */           
/*  77 */           int p0 = this.pixels[row][c];
/*  78 */           int r0 = (p0 >>> 11 & 0x1F) << 3;
/*  79 */           int g0 = (p0 >>> 5 & 0x3F) << 2;
/*  80 */           int b0 = (p0 & 0x1F) << 3;
/*  81 */           if (a > 0) {
/*  82 */             double a0 = ((p0 >>> 16 & 0x1F) << 3) / 255.0D;
/*  83 */             r = (int)(r * (1.0D - a0) + r0 * a0);
/*  84 */             g = (int)(g * (1.0D - a0) + g0 * a0);
/*  85 */             b = (int)(b * (1.0D - a0) + b0 * a0);
/*  86 */             a = (int)(a + (255 - a) * a0);
/*  87 */             if (a > 255)
/*  88 */               a = 255;
/*     */           } else {
/*  90 */             a = (p0 >>> 16 & 0x1F) << 3;
/*  91 */             r = r0;
/*  92 */             b = b0;
/*  93 */             g = g0;
/*     */           }
/*  95 */           imageData[(index0++)] = r;
/*  96 */           imageData[(index0++)] = g;
/*  97 */           imageData[(index0++)] = b;
/*  98 */           imageData[index0] = a;
/*     */         }
/*     */       }
/* 101 */       raster.setPixels(x, y, clipWidth, clipHeight, imageData);
/*     */     } catch (Exception e) {
/* 103 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void drawCenter(WritableRaster raster, int xCenter, int yCenter)
/*     */   {
/* 117 */     int xOffset = xCenter - this.xCenter;
/* 118 */     int yOffset = yCenter - this.yCenter;
/* 119 */     draw(raster, xOffset, yOffset);
/*     */   }
/*     */   
/*     */   public void draw(int[][] pixels, WritableRaster raster, int x, int y, int w, int h) {
/* 123 */     int[] iArray = new int[4];
/* 124 */     for (int row = y; row < h + y; row++) {
/* 125 */       for (int col = x; col < w + x; col++) {
/* 126 */         iArray[0] = ((pixels[row][col] >>> 11 & 0x1F) << 3);
/* 127 */         iArray[1] = ((pixels[row][col] >>> 5 & 0x3F) << 2);
/* 128 */         iArray[2] = ((pixels[row][col] & 0x1F) << 3);
/* 129 */         iArray[3] = ((pixels[row][col] >>> 16 & 0x1F) << 3);
/* 130 */         raster.setPixel(col + x, row + y, iArray);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean contain(Point point)
/*     */   {
/* 142 */     return (point.x >= 0) && (point.x <= this.width) && (point.y >= 0) && (point.y <= this.height);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hit(Point point)
/*     */   {
/* 153 */     return contain(point);
/*     */   }
/*     */ }


/* Location:              D:\Desktop\WasTools2.0-all.jar!\com\jmhxy\animation\WasFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */