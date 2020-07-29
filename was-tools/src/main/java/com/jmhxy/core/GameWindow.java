/*     */ package com.jmhxy.core;
/*     */ 
/*     */ import com.jmhxy.animation.Player;
/*     */ import com.jmhxy.animation.Sprite;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.DisplayMode;
/*     */ import java.awt.GraphicsDevice;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.util.Vector;
/*     */ import javax.swing.JFrame;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GameWindow
/*     */   extends JFrame
/*     */ {
/*     */   private static final long serialVersionUID = -8317898227965628232L;
/*     */   private DisplayMode displayMode;
/*     */   private GameCanvas gameCanvas;
/*     */   private GraphicsDevice device;
/*     */   private JFrame fullScreenWindow;
/*  27 */   Thread drawThread = new Thread() {
/*     */     public void run() {
/*     */       for (;;) {
/*  30 */         GameWindow.this.gameCanvas.draw(GameWindow.this.gameCanvas.getGraphics());
/*     */         try {
/*  32 */           Thread.sleep(40L);
/*     */         } catch (InterruptedException e) {
/*  34 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */   };
/*     */   
/*  40 */   public GameWindow(DisplayMode displayMode) { this.displayMode = displayMode;
/*  41 */     setResizable(false);
/*     */     
/*  43 */     this.gameCanvas = new GameCanvas();
/*  44 */     this.gameCanvas.setPreferredSize(new Dimension(displayMode.getWidth(), displayMode.getHeight()));
/*  45 */     setContentPane(this.gameCanvas);
/*  46 */     pack();
/*  47 */     setTitle(GameMain.applicationName);
/*  48 */     setDefaultCloseOperation(0);
/*  49 */     GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
/*  50 */     this.device = environment.getDefaultScreenDevice();
/*  51 */     this.drawThread.setDaemon(true);
/*  52 */     this.drawThread.start();
/*     */   }
/*     */   
/*     */   public void setFullScreen() {
/*  56 */     setVisible(false);
/*  57 */     setState(1);
/*  58 */     this.fullScreenWindow = new JFrame(GameMain.applicationName);
/*  59 */     this.fullScreenWindow.setContentPane(this.gameCanvas);
/*  60 */     this.fullScreenWindow.setUndecorated(true);
/*  61 */     this.fullScreenWindow.setDefaultCloseOperation(0);
/*  62 */     this.fullScreenWindow.setCursor(getCursor());
/*  63 */     this.device.setFullScreenWindow(this.fullScreenWindow);
/*  64 */     if ((this.displayMode != null) && (this.device.isDisplayChangeSupported())) {
/*     */       try {
/*  66 */         this.device.setDisplayMode(this.displayMode);
/*     */       } catch (IllegalArgumentException ex) {
/*  68 */         ex.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void restoreScreen() {
/*  74 */     this.device.setFullScreenWindow(null);
/*  75 */     if (this.fullScreenWindow != null) {
/*  76 */       this.fullScreenWindow.dispose();
/*     */     }
/*  78 */     setState(0);
/*  79 */     setContentPane(this.gameCanvas);
/*  80 */     pack();
/*  81 */     setVisible(true);
/*     */   }
/*     */   
/*     */   public boolean isFullScreen() {
/*  85 */     return this.device.getFullScreenWindow() != null;
/*     */   }
/*     */   
/*     */   public void addSprite(Sprite sprite) {
/*  89 */     this.gameCanvas.sprites.add(sprite);
/*     */   }
/*     */   
/*     */   public void addSprite(Sprite sprite, int x, int y) {
/*  93 */     sprite.setX(x);
/*  94 */     sprite.setY(y);
/*  95 */     this.gameCanvas.sprites.add(sprite);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addNPC(Player npc, int x, int y)
/*     */   {
/* 105 */     npc.setLocation(x, y);
/* 106 */     this.gameCanvas.npcs.add(npc);
/*     */   }
/*     */   
/*     */   public void addNPC(Player npc) {
/* 110 */     this.gameCanvas.npcs.add(npc);
/*     */   }
/*     */   
/*     */   public void setPlayer(Player player, int x, int y) {
/* 114 */     player.setLocation(x, y);
/* 115 */     setPlayer(player);
/*     */   }
/*     */   
/*     */   public GameCanvas getGameCanvas() {
/* 119 */     return this.gameCanvas;
/*     */   }
/*     */   
/*     */   public void setPlayer(Player player) {
/* 123 */     player.stop();
/* 124 */     this.gameCanvas.player = player;
/*     */   }
/*     */ }


/* Location:              D:\Desktop\WasTools2.0-all.jar!\com\jmhxy\core\GameWindow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */