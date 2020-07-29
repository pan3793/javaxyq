/*     */ package com.jmhxy.core;
/*     */ 
/*     */ import com.jmhxy.animation.Fireworks;
/*     */ import com.jmhxy.animation.Player;
/*     */ import com.jmhxy.animation.Sprite;
/*     */ import com.jmhxy.comps.NullRepaintManager;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Image;
/*     */ import java.awt.Point;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.util.Vector;
/*     */ import javax.swing.JPanel;
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
/*     */ public class GameCanvas
/*     */   extends JPanel
/*     */ {
/*     */   private static final long serialVersionUID = -5324089420192935553L;
/*     */   Image bgImage;
/*     */   Sprite clickAnim;
/*     */   Sprite cursor;
/*     */   BufferedImage offscreenImage;
/*     */   Graphics offsreenGraphics;
/*     */   long lastTime;
/*     */   Player player;
/*     */   Vector<Sprite> sprites;
/*     */   Vector<Player> npcs;
/*     */   private Fireworks fireworks;
/*     */   private long beginTime;
/*     */   
/*     */   public GameCanvas()
/*     */   {
/*  49 */     NullRepaintManager.install();
/*  50 */     this.sprites = new Vector();
/*  51 */     this.npcs = new Vector();
/*  52 */     this.offscreenImage = new BufferedImage(GameMain.windowWidth, GameMain.windowHeight, 8);
/*  53 */     this.offsreenGraphics = this.offscreenImage.getGraphics();
/*     */     
/*     */ 
/*  56 */     this.cursor = SpriteFactory.loadCursor("普通.was");
/*  57 */     this.clickAnim = SpriteFactory.loadCursor("水波.was");
/*  58 */     this.clickAnim.setVisible(false);
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
/*  88 */     setBackground(Color.BLACK);
/*  89 */     setForeground(Color.WHITE);
/*  90 */     setIgnoreRepaint(true);
/*  91 */     setFocusable(true);
/*  92 */     requestFocus(true);
/*  93 */     setLayout(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void draw(Graphics g)
/*     */   {
/* 102 */     if (this.bgImage != null)
/* 103 */       this.offsreenGraphics.drawImage(this.bgImage, 0, 0, null);
/* 104 */     long currTime = System.currentTimeMillis();
/* 105 */     if (this.lastTime == 0L) {
/* 106 */       this.lastTime = currTime;
/* 107 */       this.beginTime = currTime;
/*     */     }
/* 109 */     long elapsedTime = currTime - this.lastTime;
/*     */     
/* 111 */     for (Sprite sprite : this.sprites) {
/* 112 */       sprite.update(elapsedTime);
/* 113 */       sprite.draw(this.offsreenGraphics);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 119 */     for (Player npc : this.npcs) {
/* 120 */       npc.update(elapsedTime);
/* 121 */       npc.draw(this.offsreenGraphics);
/*     */     }
/*     */     
/* 124 */     if (this.player != null) {
/* 125 */       this.player.update(elapsedTime);
/* 126 */       this.player.draw(this.offsreenGraphics);
/*     */     }
/*     */     
/* 129 */     if (this.clickAnim != null) {
/* 130 */       this.clickAnim.update(elapsedTime);
/* 131 */       this.clickAnim.draw(this.offsreenGraphics);
/*     */     }
/*     */     
/*     */ 
/* 135 */     Component[] comps = getComponents();
/* 136 */     for (int i = comps.length - 1; i >= 0; i--) {
/* 137 */       Component c = comps[i];
/* 138 */       Graphics g2 = this.offsreenGraphics.create(c.getX(), c.getY(), c.getWidth(), c.getHeight());
/* 139 */       c.paint(g2);
/* 140 */       g2.dispose();
/*     */     }
/*     */     
/* 143 */     if (this.cursor != null) {
/* 144 */       Point p = getMousePosition();
/* 145 */       if (p != null) {
/* 146 */         this.cursor.setLocation(p);
/*     */       }
/* 148 */       this.cursor.update(elapsedTime);
/* 149 */       this.cursor.draw(this.offsreenGraphics);
/*     */     }
/* 151 */     this.lastTime = currTime;
/* 152 */     g.drawImage(this.offscreenImage, 0, 0, null);
/* 153 */     g.dispose();
/*     */   }
/*     */   
/*     */   public Player getPlayer()
/*     */   {
/* 158 */     return this.player;
/*     */   }
/*     */   
/*     */   public void click(Point p) {
/* 162 */     this.clickAnim.setLocation(p);
/* 163 */     this.clickAnim.setRepeat(1);
/*     */   }
/*     */ }


/* Location:              D:\Desktop\WasTools2.0-all.jar!\com\jmhxy\core\GameCanvas.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */