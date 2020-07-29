/*     */ package com.jmhxy.animation;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Image;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.io.Serializable;
/*     */ import java.util.Vector;
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
/*     */ public class Sprite
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 3020344223229541682L;
/*     */   public static final int ANGLE_BOTTOM_RIGHT = 0;
/*     */   public static final int ANGLE_BOTTOM_LEFT = 1;
/*     */   public static final int ANGLE_TOP_LEFT = 2;
/*     */   public static final int ANGLE_TOP_RIGHT = 3;
/*     */   public static final int ANGLE_BOTTOM = 4;
/*     */   public static final int ANGLE_LEFT = 5;
/*     */   public static final int ANGLE_TOP = 6;
/*     */   public static final int ANGLE_RIGHT = 7;
/*     */   private Vector<Animation> animations;
/*     */   int angle;
/*     */   Animation currAnimation;
/*     */   int x;
/*     */   int y;
/*     */   int width;
/*     */   int height;
/*     */   int centerX;
/*     */   int centerY;
/*     */   float dx;
/*     */   float dy;
/*     */   float acceX;
/*     */   float acceY;
/*  55 */   private boolean visible = true;
/*     */   
/*     */   public Sprite(int width, int height, int centerX, int centerY) {
/*  58 */     this.animations = new Vector();
/*  59 */     this.width = width;
/*  60 */     this.height = height;
/*  61 */     this.centerX = centerX;
/*  62 */     this.centerY = centerY;
/*     */   }
/*     */   
/*     */   public static Sprite newInstance(Sprite sprite) {
/*  66 */     Sprite newSprite = new Sprite(sprite.width, sprite.height, sprite.centerX, sprite.centerY);
/*  67 */     for (Animation anim : sprite.animations) {
/*  68 */       newSprite.animations.add(new Animation(anim));
/*     */     }
/*  70 */     newSprite.reset();
/*  71 */     return newSprite;
/*     */   }
/*     */   
/*     */   public void reset() {
/*  75 */     this.angle = 0;
/*  76 */     this.currAnimation = ((Animation)this.animations.get(0));
/*     */   }
/*     */   
/*     */   public void update(long elapsedTime)
/*     */   {
/*  81 */     this.dx += this.acceX * (float)elapsedTime;
/*  82 */     this.dy += this.acceY * (float)elapsedTime;
/*  83 */     if ((this.dx != 0.0F) || (this.dy != 0.0F)) {
/*  84 */       this.x = ((int)(this.x + (0.5D + this.dx * (float)elapsedTime)));
/*  85 */       this.y = ((int)(this.y + (0.5D + this.dy * (float)elapsedTime)));
/*     */     }
/*  87 */     this.currAnimation.update(elapsedTime);
/*     */   }
/*     */   
/*     */   public void addAnimation(Animation anim) {
/*  91 */     anim.reset();
/*  92 */     this.animations.add(anim);
/*  93 */     this.currAnimation = ((Animation)this.animations.get(0));
/*     */   }
/*     */   
/*     */   public int getAngle() {
/*  97 */     return this.angle;
/*     */   }
/*     */   
/*     */   public void setAngle(int index)
/*     */   {
/* 102 */     if (index >= this.animations.size())
/* 103 */       index -= 4;
/* 104 */     this.angle = index;
/* 105 */     boolean canPlay = this.currAnimation.canPlay;
/* 106 */     this.currAnimation = ((Animation)this.animations.get(this.angle));
/* 107 */     this.currAnimation.canPlay = canPlay;
/*     */   }
/*     */   
/*     */   public synchronized void draw(Graphics g) {
/* 111 */     if (this.visible) {
/* 112 */       int x1 = this.x - this.currAnimation.getCenterX();
/* 113 */       int y1 = this.y - this.currAnimation.getCenterY();
/*     */       
/*     */ 
/* 116 */       g.drawImage(this.currAnimation.getImage(), x1, y1, null);
/*     */     }
/*     */   }
/*     */   
/*     */   public float getAcceX() {
/* 121 */     return this.acceX;
/*     */   }
/*     */   
/*     */   public float getAcceY() {
/* 125 */     return this.acceY;
/*     */   }
/*     */   
/*     */   public void setAcceY(float acceY) {
/* 129 */     this.acceY = acceY;
/*     */   }
/*     */   
/*     */   public void setAcceX(float acceX) {
/* 133 */     this.acceX = acceX;
/*     */   }
/*     */   
/*     */   public int getX() {
/* 137 */     return this.x;
/*     */   }
/*     */   
/*     */   public void setX(int x) {
/* 141 */     this.x = x;
/*     */   }
/*     */   
/*     */   public int getY() {
/* 145 */     return this.y;
/*     */   }
/*     */   
/*     */   public void setY(int y) {
/* 149 */     this.y = y;
/*     */   }
/*     */   
/*     */   public float getVelocityX() {
/* 153 */     return this.dx;
/*     */   }
/*     */   
/*     */   public void setVelocityX(float dx) {
/* 157 */     this.dx = dx;
/*     */   }
/*     */   
/*     */   public float getVelocityY() {
/* 161 */     return this.dy;
/*     */   }
/*     */   
/*     */   public void setVelocityY(float dy) {
/* 165 */     this.dy = dy;
/*     */   }
/*     */   
/*     */   public int getHeight() {
/* 169 */     return this.height;
/*     */   }
/*     */   
/*     */   public void setHeight(int height) {
/* 173 */     this.height = height;
/*     */   }
/*     */   
/*     */   public int getWidth() {
/* 177 */     return this.width;
/*     */   }
/*     */   
/*     */   public void setWidth(int width) {
/* 181 */     this.width = width;
/*     */   }
/*     */   
/*     */   public void setLocation(Point point) {
/* 185 */     this.x = point.x;
/* 186 */     this.y = point.y;
/*     */   }
/*     */   
/*     */   public Point getLocation() {
/* 190 */     return new Point(this.x, this.y);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void move(int x, int y) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isArrived()
/*     */   {
/* 209 */     return false;
/*     */   }
/*     */   
/*     */   public boolean canPlay() {
/* 213 */     return this.currAnimation.canPlay;
/*     */   }
/*     */   
/*     */   public int getRepeat() {
/* 217 */     return this.currAnimation.repeat;
/*     */   }
/*     */   
/*     */   public void setRepeat(int repeat) {
/* 221 */     this.currAnimation.repeat = repeat;
/* 222 */     this.currAnimation.canPlay = true;
/* 223 */     this.visible = true;
/*     */   }
/*     */   
/*     */   public Animation getAnimation(int index) {
/* 227 */     return (Animation)this.animations.get(index);
/*     */   }
/*     */   
/*     */   public void setVisible(boolean b) {
/* 231 */     this.visible = b;
/*     */   }
/*     */   
/*     */   public Image getImage() {
/* 235 */     return this.currAnimation.getImage();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int computeAngle(Point src, Point mouse)
/*     */   {
/* 247 */     int angle = 0;
/* 248 */     double dy = mouse.y - src.y;
/* 249 */     double dx = mouse.x - src.x;
/* 250 */     if (dx == 0.0D)
/* 251 */       return dy >= 0.0D ? 4 : 6;
/* 252 */     if (dy == 0.0D) return dx >= 0.0D ? 7 : 5;
/* 253 */     double k = Math.abs(dy / dx);
/* 254 */     if (k >= k2) {
/* 255 */       if (dy > 0.0D) {
/* 256 */         angle = 4;
/*     */       } else
/* 258 */         angle = 6;
/* 259 */     } else if (k <= k1) {
/* 260 */       if (dx > 0.0D) {
/* 261 */         angle = 7;
/*     */       } else
/* 263 */         angle = 5;
/* 264 */     } else if (dy > 0.0D) {
/* 265 */       if (dx > 0.0D) {
/* 266 */         angle = 0;
/*     */       } else {
/* 268 */         angle = 1;
/*     */       }
/* 270 */     } else if (dx > 0.0D) {
/* 271 */       angle = 3;
/*     */     } else {
/* 273 */       angle = 2;
/*     */     }
/* 275 */     return angle;
/*     */   }
/*     */   
/* 278 */   private static double k1 = Math.tan(0.39269908169872414D);
/*     */   
/* 280 */   private static double k2 = 3.0D * k1;
/*     */   
/*     */   public boolean contains(Point p) {
/* 283 */     Rectangle rect = new Rectangle(this.x - this.centerX, this.y - this.centerY, this.width, this.height);
/* 284 */     return rect.contains(p);
/*     */   }
/*     */   
/*     */   public Sprite clone() throws CloneNotSupportedException {
/* 288 */     Sprite newSprite = new Sprite(this.width, this.height, this.centerX, this.centerY);
/* 289 */     newSprite.animations = new Vector(this.animations.size());
/* 290 */     for (Animation anim : this.animations) {
/* 291 */       newSprite.animations.add(anim.clone());
/*     */     }
/* 293 */     newSprite.reset();
/* 294 */     return newSprite;
/*     */   }
/*     */ }


/* Location:              D:\Desktop\WasTools2.0-all.jar!\com\jmhxy\animation\Sprite.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */