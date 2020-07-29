/*     */ package com.jmhxy.animation;
/*     */ 
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Image;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.RescaleOp;
/*     */ import java.io.Serializable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ 
/*     */ public class Animation
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -6869119010889158767L;
/*     */   private Vector<Frame> frames;
/*  16 */   int repeat = -1;
/*     */   
/*     */   int index;
/*     */   
/*     */   Frame currFrame;
/*     */   
/*     */   private long animTime;
/*     */   
/*     */   private long totalDuration;
/*     */   
/*  26 */   boolean canPlay = true;
/*     */   
/*     */   private int id;
/*     */   
/*     */   private BufferedImage biSrc;
/*     */   
/*     */   private BufferedImage biDest;
/*     */   
/*     */   public int getId()
/*     */   {
/*  36 */     return this.id;
/*     */   }
/*     */   
/*     */   public Animation() {
/*  40 */     this.frames = new Vector();
/*     */   }
/*     */   
/*     */   public Animation(Animation anim) {
/*  44 */     this.totalDuration = anim.totalDuration;
/*  45 */     this.frames = anim.frames;
/*  46 */     this.currFrame = anim.currFrame;
/*     */   }
/*     */   
/*     */   public synchronized void addFrame(Frame frame) {
/*  50 */     this.frames.add(frame);
/*  51 */     this.totalDuration += frame.endTime;
/*  52 */     this.currFrame = frame;
/*     */   }
/*     */   
/*     */   public synchronized void addFrame(Image image, long duration, int centerX, int centerY) {
/*  56 */     this.totalDuration += duration;
/*  57 */     Frame frame = new Frame(image, this.totalDuration, centerX, centerY);
/*  58 */     this.frames.add(frame);
/*  59 */     this.currFrame = frame;
/*     */   }
/*     */   
/*     */   public synchronized void update(long elapsedTime)
/*     */   {
/*  64 */     if (!this.canPlay)
/*  65 */       return;
/*  66 */     if (this.frames.size() > 1) {
/*  67 */       this.animTime += elapsedTime;
/*  68 */       if (this.animTime >= this.totalDuration) {
/*  69 */         this.animTime %= this.totalDuration;
/*  70 */         this.index = 0;
/*  71 */         this.currFrame = ((Frame)this.frames.get(0));
/*  72 */         if (this.repeat > 0) {
/*  73 */           this.repeat -= 1;
/*  74 */         } else if (this.repeat == 0) {
/*  75 */           this.canPlay = false;
/*  76 */           return;
/*     */         }
/*     */       }
/*  79 */       if (elapsedTime < 0L) this.index = 0;
/*  80 */       while (this.animTime > ((Frame)this.frames.get(this.index)).endTime) {
/*  81 */         this.index += 1;
/*     */       }
/*  83 */       this.currFrame = ((Frame)this.frames.get(this.index));
/*  84 */     } else if (this.frames.size() > 0) {
/*  85 */       this.currFrame = ((Frame)this.frames.get(0));
/*     */     } else {
/*  87 */       this.currFrame = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateToTime(long playTime)
/*     */   {
/*  97 */     if (this.frames.size() > 1) {
/*  98 */       this.animTime = playTime;
/*  99 */       if (this.animTime >= this.totalDuration) {
/* 100 */         this.animTime %= this.totalDuration;
/* 101 */         this.index = 0;
/* 102 */         this.currFrame = ((Frame)this.frames.get(0));
/* 103 */         if (this.repeat > 0) {
/* 104 */           this.repeat -= 1;
/* 105 */         } else if (this.repeat == 0) {
/* 106 */           this.canPlay = false;
/* 107 */           return;
/*     */         }
/*     */       }
/* 110 */       this.index = 0;
/* 111 */       while (this.animTime > ((Frame)this.frames.get(this.index)).endTime) {
/* 112 */         this.index += 1;
/*     */       }
/* 114 */       this.currFrame = ((Frame)this.frames.get(this.index));
/* 115 */     } else if (this.frames.size() > 0) {
/* 116 */       this.currFrame = ((Frame)this.frames.get(0));
/*     */     } else {
/* 118 */       this.currFrame = null;
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized Image getImage() {
/* 123 */     if ((this.frames.size() == 0) || (!this.canPlay)) {
/* 124 */       return null;
/*     */     }
/* 126 */     return this.currFrame.image;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void reset()
/*     */   {
/* 135 */     this.animTime = 0L;
/* 136 */     this.index = 0;
/* 137 */     this.currFrame = ((Frame)this.frames.get(0));
/*     */   }
/*     */   
/*     */   public int getWidth() {
/* 141 */     return this.currFrame == null ? 0 : this.currFrame.width;
/*     */   }
/*     */   
/*     */   public int getHeight() {
/* 145 */     return this.currFrame == null ? 0 : this.currFrame.height;
/*     */   }
/*     */   
/*     */   public int getCenterX() {
/* 149 */     return this.currFrame == null ? 0 : this.currFrame.centerX;
/*     */   }
/*     */   
/*     */   public int getCenterY() {
/* 153 */     return this.currFrame == null ? 0 : this.currFrame.centerY;
/*     */   }
/*     */   
/*     */   public boolean canPlay() {
/* 157 */     return this.canPlay;
/*     */   }
/*     */   
/*     */   public int getRepeat() {
/* 161 */     return this.repeat;
/*     */   }
/*     */   
/*     */   public void setRepeat(int repeat) {
/* 165 */     this.repeat = repeat;
/* 166 */     this.canPlay = true;
/*     */   }
/*     */   
/*     */   public Vector<Frame> getFrames() {
/* 170 */     return this.frames;
/*     */   }
/*     */   
/*     */   public Animation clone() {
/* 174 */     return new Animation(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Image getBrightenImage()
/*     */   {
/* 184 */     if (this.biSrc == null) {
/* 185 */       this.biSrc = new BufferedImage(getWidth(), getHeight(), 2);
/* 186 */       Graphics2D g2 = this.biSrc.createGraphics();
/* 187 */       g2.drawImage(this.currFrame.image, 0, 0, null);
/*     */     }
/* 189 */     if (this.biDest == null)
/* 190 */       this.biDest = new BufferedImage(getWidth(), getHeight(), 2);
/* 191 */     float scaleFactor = 1.8F;
/* 192 */     float offset = 0.0F;
/* 193 */     RescaleOp rescale = new RescaleOp(scaleFactor, offset, null);
/* 194 */     rescale.filter(this.biSrc.getRaster(), this.biDest.getRaster());
/*     */     
/* 196 */     return this.biDest;
/*     */   }
/*     */   
/*     */   public int getFrameCount() {
/* 200 */     return this.frames.size();
/*     */   }
/*     */ }


/* Location:              D:\Desktop\WasTools2.0-all.jar!\com\jmhxy\animation\Animation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */