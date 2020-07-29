/*     */ package com.jmhxy.comps;
/*     */ 
/*     */ import com.jmhxy.animation.Animation;
/*     */ import com.jmhxy.animation.Frame;
/*     */ import com.jmhxy.animation.Sprite;
/*     */ import com.jmhxy.core.GameMain;
/*     */ import java.awt.Color;
/*     */ import java.util.List;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JButton;
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
/*     */ public class GButton
/*     */   extends JButton
/*     */ {
/*     */   private static final long serialVersionUID = -325670333791283101L;
/*  33 */   private boolean autoOffset = false;
/*     */   
/*     */ 
/*     */   public GButton() {}
/*     */   
/*     */   public GButton(Action action)
/*     */   {
/*  40 */     super(action);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GButton(int width, int height, List<Frame> frames)
/*     */   {
/*  49 */     init(width, height, frames);
/*     */   }
/*     */   
/*     */   public GButton(Sprite sprite) {
/*  53 */     init(sprite);
/*     */   }
/*     */   
/*     */   public void init(int width, int height, List<Frame> frames) {
/*  57 */     setSize(width, height);
/*     */     
/*  59 */     setUI(new GButtonUI());
/*  60 */     setFont(GameMain.TEXT_FONT);
/*  61 */     setForeground(Color.WHITE);
/*  62 */     setHorizontalTextPosition(0);
/*  63 */     setVerticalTextPosition(0);
/*  64 */     setHorizontalAlignment(0);
/*  65 */     setVerticalAlignment(0);
/*     */     
/*  67 */     setIgnoreRepaint(true);
/*  68 */     setBorder(null);
/*     */     
/*  70 */     setFocusable(false);
/*  71 */     setContentAreaFilled(false);
/*     */     try
/*     */     {
/*  74 */       setIcon(new ImageIcon(((Frame)frames.get(0)).getFixedImage(width, height)));
/*  75 */       setPressedIcon(new ImageIcon(((Frame)frames.get(1)).getFixedImage(width, height)));
/*  76 */       setRolloverIcon(new ImageIcon(((Frame)frames.get(2)).getFixedImage(width, height)));
/*  77 */       setDisabledIcon(new ImageIcon(((Frame)frames.get(3)).getFixedImage(width, height)));
/*     */     }
/*     */     catch (Exception e) {
/*  80 */       if (frames.size() < 3)
/*  81 */         this.autoOffset = true;
/*     */     }
/*     */   }
/*     */   
/*     */   public void init(Sprite sprite) {
/*  86 */     List<Frame> frames = sprite.getAnimation(0).getFrames();
/*  87 */     int width = sprite.getWidth();int height = sprite.getHeight();
/*  88 */     init(width, height, frames);
/*     */   }
/*     */   
/*     */   public boolean isAutoOffset() {
/*  92 */     return this.autoOffset;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAutoOffset(boolean autoOffset)
/*     */   {
/* 104 */     this.autoOffset = autoOffset;
/*     */   }
/*     */ }


/* Location:              D:\Desktop\WasTools2.0-all.jar!\com\jmhxy\comps\GButton.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */