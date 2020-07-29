/*     */ package com.wildbean.wastools.core;
/*     */ 
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ public abstract class AbstractCanvasImage
/*     */   implements CanvasImage, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected String name;
/*  15 */   protected boolean visible = true;
/*     */   
/*     */   protected int spriteIndex;
/*     */   
/*     */   protected int frameIndex;
/*     */   
/*     */   protected int x;
/*     */   
/*     */   protected int y;
/*     */   
/*     */   protected long animTime;
/*     */   
/*     */   protected long totalDuration;
/*     */   protected CanvasImage linkedBase;
/*     */   protected boolean isBase;
/*     */   private PropertyChangeSupport changeSupport;
/*     */   
/*     */   public String getName()
/*     */   {
/*  34 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/*  38 */     this.name = name;
/*  39 */     fireDataChanged();
/*     */   }
/*     */   
/*     */   public boolean isVisible() {
/*  43 */     return this.visible;
/*     */   }
/*     */   
/*     */   public void setVisible(boolean visible) {
/*  47 */     this.visible = visible;
/*  48 */     fireDataChanged();
/*     */   }
/*     */   
/*     */   public boolean contain(Point p) {
/*  52 */     return (p.x >= this.x) && (p.x <= this.x + getWidth()) && (p.y >= this.y) && (p.y <= this.y + getHeight());
/*     */   }
/*     */   
/*     */   public void translate(int dx, int dy) {
/*  56 */     this.x += dx;
/*  57 */     this.y += dy;
/*  58 */     fireDataChanged();
/*     */   }
/*     */   
/*     */   public int getSpriteIndex() {
/*  62 */     return this.spriteIndex;
/*     */   }
/*     */   
/*     */   public void setSpriteIndex(int index) {
/*  66 */     this.totalDuration = (100 * getFrameCount());
/*  67 */     fireDataChanged();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void alignToCenter(CanvasImage base)
/*     */   {
/*  76 */     this.x = (base.getX() + base.getWidth() / 2 - getWidth() / 2);
/*  77 */     this.y = (base.getY() + base.getHeight() / 2 - getHeight() / 2);
/*  78 */     fireDataChanged();
/*     */   }
/*     */   
/*     */   public synchronized void update(long elapsedTime) {}
/*     */   
/*     */   public String toString()
/*     */   {
/*  85 */     return this.name;
/*     */   }
/*     */   
/*     */   public String getInfo() {
/*  89 */     return "Name: " + this.name + "\nType: " + getType();
/*     */   }
/*     */   
/*     */   public void setLocation(int x, int y) {
/*  93 */     this.x = x;
/*  94 */     this.y = y;
/*  95 */     fireDataChanged();
/*     */   }
/*     */   
/*     */   public int getX() {
/*  99 */     return this.x;
/*     */   }
/*     */   
/*     */   public int getY() {
/* 103 */     return this.y;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFrameIndex(int index)
/*     */   {
/* 113 */     this.frameIndex = index;
/*     */   }
/*     */   
/*     */   public int getFrameIndex() {
/* 117 */     return this.frameIndex;
/*     */   }
/*     */   
/*     */   public int getFrameCount() {
/* 121 */     return 1;
/*     */   }
/*     */   
/*     */   public int getTotalDelay() {
/* 125 */     return getFrameCount();
/*     */   }
/*     */   
/*     */   public boolean isLinked(Object base) {
/* 129 */     return this.linkedBase == base;
/*     */   }
/*     */   
/*     */   public void setLinkedBase(CanvasImage image) {
/* 133 */     this.linkedBase = image;
/* 134 */     fireDataChanged();
/*     */   }
/*     */   
/*     */   public CanvasImage getLinkedBase() {
/* 138 */     return this.linkedBase;
/*     */   }
/*     */   
/*     */   public boolean isLinked() {
/* 142 */     return this.linkedBase != null;
/*     */   }
/*     */   
/*     */   public boolean isBase() {
/* 146 */     return this.isBase;
/*     */   }
/*     */   
/*     */   public void setBase(boolean isBase) {
/* 150 */     this.isBase = isBase;
/* 151 */     fireDataChanged();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void fireSpriteIndexChanged()
/*     */   {
/* 171 */     firePropertyChange("sprite index changed", null, Integer.valueOf(this.spriteIndex));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void firePropertyChange(String propertyName, Object oldValue, Object newValue)
/*     */   {
/* 189 */     if ((this.changeSupport == null) || (
/* 190 */       (oldValue != null) && (newValue != null) && (oldValue.equals(newValue)))) {
/* 191 */       return;
/*     */     }
/* 193 */     this.changeSupport.firePropertyChange(propertyName, oldValue, newValue);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void addPropertyChangeListener(PropertyChangeListener listener)
/*     */   {
/* 234 */     if (listener == null) {
/* 235 */       return;
/*     */     }
/* 237 */     if (this.changeSupport == null) {
/* 238 */       this.changeSupport = new PropertyChangeSupport(this);
/*     */     }
/* 240 */     this.changeSupport.addPropertyChangeListener(listener);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void removePropertyChangeListener(PropertyChangeListener listener)
/*     */   {
/* 258 */     if ((listener == null) || (this.changeSupport == null)) {
/* 259 */       return;
/*     */     }
/* 261 */     this.changeSupport.removePropertyChangeListener(listener);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized PropertyChangeListener[] getPropertyChangeListeners()
/*     */   {
/* 279 */     if (this.changeSupport == null) {
/* 280 */       return new PropertyChangeListener[0];
/*     */     }
/* 282 */     return this.changeSupport.getPropertyChangeListeners();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
/*     */   {
/* 323 */     if (listener == null) {
/* 324 */       return;
/*     */     }
/* 326 */     if (this.changeSupport == null) {
/* 327 */       this.changeSupport = new PropertyChangeSupport(this);
/*     */     }
/* 329 */     this.changeSupport.addPropertyChangeListener(propertyName, listener);
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
/*     */   public synchronized void removePropertyChangeListener(String propertyName, PropertyChangeListener listener)
/*     */   {
/* 353 */     if ((listener == null) || (this.changeSupport == null)) {
/* 354 */       return;
/*     */     }
/* 356 */     this.changeSupport.removePropertyChangeListener(propertyName, listener);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized PropertyChangeListener[] getPropertyChangeListeners(String propertyName)
/*     */   {
/* 376 */     if (this.changeSupport == null) {
/* 377 */       return new PropertyChangeListener[0];
/*     */     }
/* 379 */     return this.changeSupport.getPropertyChangeListeners(propertyName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void alignToCenter(Rectangle rect)
/*     */   {
/* 389 */     this.x = (rect.x + rect.width / 2 - getWidth() / 2);
/* 390 */     this.y = (rect.y + rect.height / 2 - getHeight() / 2);
/* 391 */     fireDataChanged();
/*     */   }
/*     */   
/*     */   public int getType() {
/* 395 */     return 0;
/*     */   }
/*     */   
/*     */   public CanvasImage clone() {
/*     */     try {
/* 400 */       return (CanvasImage)super.clone();
/*     */     } catch (CloneNotSupportedException e) {
/* 402 */       e.printStackTrace(); }
/* 403 */     return null;
/*     */   }
/*     */   
/*     */   public int getSpriteCount() {
/* 407 */     return 1;
/*     */   }
/*     */   
/* 410 */   public Rectangle getBounds() { return new Rectangle(getX(), getY(), getWidth(), getHeight()); }
/*     */   
/*     */ 
/*     */ 
/*     */   public void fireDataChanged()
/*     */   {
/* 416 */     firePropertyChange("image data changed", null, this);
/*     */   }
/*     */   
/*     */   public String getTypeName() {
/* 420 */     String typename = "未知";
/* 421 */     switch (getType()) {
/*     */     case 0: 
/* 423 */       typename = "标准图像";
/* 424 */       break;
/*     */     case 1: 
/* 426 */       typename = "精灵图像";
/* 427 */       break;
/*     */     case 2: 
/* 429 */       typename = "文本动画";
/* 430 */       break;
/*     */     }
/*     */     
/*     */     
/* 434 */     return typename;
/*     */   }
/*     */ }


/* Location:              D:\Desktop\WasTools2.0-all.jar!\com\wildbean\wastools\core\AbstractCanvasImage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */