/*     */ package com.jmhxy.comps;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Image;
/*     */ import java.awt.Point;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.awt.event.MouseMotionAdapter;
/*     */ import java.awt.event.MouseMotionListener;
/*     */ import java.util.ArrayList;
/*     */ import javax.swing.JPanel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GDialog
/*     */   extends JPanel
/*     */ {
/*     */   private static final long serialVersionUID = 3207034027692111969L;
/*  27 */   private ArrayList<BGImage> bgImages = new ArrayList();
/*     */   
/*     */   private boolean closable;
/*     */   
/*  31 */   private MouseListener dialogMouseListener = new MouseAdapter() {
/*     */     public void mousePressed(MouseEvent e) {
/*  33 */       if (e.getButton() == 1) {
/*  34 */         GDialog.this.oldPoint = e.getPoint();
/*  35 */         GDialog dlg = GDialog.this;
/*  36 */         dlg.getParent().setComponentZOrder(dlg, 0);
/*  37 */       } else if ((e.getButton() == 3) && 
/*  38 */         (GDialog.this.closable)) {
/*  39 */         Container p = GDialog.this.getParent();
/*  40 */         p.remove(GDialog.this);
/*     */       }
/*     */     }
/*     */     
/*     */     public void mouseReleased(MouseEvent e)
/*     */     {
/*  46 */       GDialog.this.oldPoint = null;
/*     */     }
/*     */   };
/*     */   
/*  50 */   private MouseMotionListener dialogMouseMotionListener = new MouseMotionAdapter() {
/*     */     public void mouseDragged(MouseEvent e) {
/*  52 */       if ((GDialog.this.oldPoint != null) && (GDialog.this.movable)) {
/*  53 */         Point location = GDialog.this.getLocation();
/*  54 */         location.translate(e.getX() - GDialog.this.oldPoint.x, e.getY() - GDialog.this.oldPoint.y);
/*  55 */         GDialog.this.setLocation(location);
/*     */       }
/*     */     }
/*     */   };
/*     */   
/*     */   private boolean movable;
/*     */   private Point oldPoint;
/*     */   
/*     */   public GDialog(int width, int height)
/*     */   {
/*  65 */     this(width, height, true, true);
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
/*     */   public GDialog(int width, int height, boolean closable, boolean movable)
/*     */   {
/*  78 */     this.closable = closable;
/*  79 */     this.movable = movable;
/*     */     
/*  81 */     setIgnoreRepaint(true);
/*  82 */     setBorder(null);
/*  83 */     setLayout(null);
/*     */     
/*  85 */     setFocusable(false);
/*  86 */     setPreferredSize(new Dimension(width, height));
/*  87 */     setSize(width, height);
/*  88 */     addMouseListener(this.dialogMouseListener);
/*  89 */     addMouseMotionListener(this.dialogMouseMotionListener);
/*     */   }
/*     */   
/*     */   public void addBGImage(Image image, int x, int y) {
/*  93 */     addBGImage(image, x, y, image.getWidth(null), image.getHeight(null));
/*     */   }
/*     */   
/*     */   public void addBGImage(Image image, int x, int y, int width, int height) {
/*  97 */     this.bgImages.add(new BGImage(image, x, y, width, height));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Component getComponentByName(String name)
/*     */   {
/* 107 */     if (name != null) {
/* 108 */       Component[] comps = getComponents();
/* 109 */       for (Component comp : comps) {
/* 110 */         if (name.equals(comp.getName()))
/* 111 */           return comp;
/*     */       }
/*     */     }
/* 114 */     return null;
/*     */   }
/*     */   
/*     */   public void paint(Graphics g) {
/* 118 */     for (BGImage bgImage : this.bgImages) {
/* 119 */       g.drawImage(bgImage.image, bgImage.x, bgImage.y, bgImage.x + bgImage.width, bgImage.y + bgImage.height, 0, 
/* 120 */         0, bgImage.width, bgImage.height, null);
/*     */     }
/* 122 */     Component[] comps = getComponents();
/* 123 */     for (Component c : comps) {
/* 124 */       Graphics g2 = g.create(c.getX(), c.getY(), c.getWidth(), c.getHeight());
/* 125 */       c.paint(g2);
/* 126 */       g2.dispose();
/*     */     }
/*     */   }
/*     */   
/*     */   public void setClosable(Boolean b) {
/* 131 */     this.closable = b.booleanValue();
/*     */   }
/*     */   
/*     */   public void setMovable(Boolean b) {
/* 135 */     this.movable = b.booleanValue();
/*     */   }
/*     */ }


/* Location:              D:\Desktop\WasTools2.0-all.jar!\com\jmhxy\comps\GDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */