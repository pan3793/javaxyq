/*    */ package com.jmhxy.comps;
/*    */ 
/*    */ import java.awt.AlphaComposite;
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Graphics2D;
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutputStream;
/*    */ import javax.swing.JPanel;
/*    */ 
/*    */ 
/*    */ public class ChatFloatPanel
/*    */   extends JPanel
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private transient GFormattedLabel label;
/*    */   private String text;
/*    */   
/*    */   public ChatFloatPanel(String text)
/*    */   {
/* 24 */     this.text = text;
/* 25 */     init();
/*    */   }
/*    */   
/*    */   private void init() {
/* 29 */     setBorder(null);
/* 30 */     setLayout(null);
/* 31 */     setOpaque(false);
/* 32 */     setIgnoreRepaint(true);
/* 33 */     setFocusable(false);
/* 34 */     this.label = new GFormattedLabel(this.text);
/* 35 */     add(this.label);
/* 36 */     this.label.setLocation(4, 2);
/* 37 */     setText(this.text);
/*    */   }
/*    */   
/*    */   public ChatFloatPanel() {
/* 41 */     this(null);
/*    */   }
/*    */   
/*    */   public void paint(Graphics g)
/*    */   {
/* 46 */     Graphics2D g2d = (Graphics2D)g.create();
/* 47 */     g2d.setComposite(AlphaComposite.getInstance(3, 0.5F));
/* 48 */     g2d.setColor(Color.BLACK);
/* 49 */     g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
/* 50 */     g2d.dispose();
/* 51 */     Component[] comps = getComponents();
/* 52 */     for (Component c : comps) {
/* 53 */       Graphics g2 = g.create(c.getX(), c.getY(), c.getWidth(), c.getHeight());
/* 54 */       c.paint(g2);
/* 55 */       g2.dispose();
/*    */     }
/*    */   }
/*    */   
/*    */   public void setText(String chatText) {
/* 60 */     this.text = chatText;
/* 61 */     this.label.setFormattedText(chatText);
/* 62 */     Dimension d = this.label.computeSize(98);
/* 63 */     this.label.setSize(d);
/* 64 */     Dimension size = new Dimension(8 + d.width, 6 + d.height);
/* 65 */     setSize(size);
/* 66 */     setPreferredSize(size);
/*    */   }
/*    */   
/*    */   public String getText() {
/* 70 */     return this.text;
/*    */   }
/*    */   
/*    */   public int getFrameCount() {
/* 74 */     return this.label.getFrameCount();
/*    */   }
/*    */   
/*    */   public void update(long elapsedTime) {
/* 78 */     this.label.update(elapsedTime);
/*    */   }
/*    */   
/*    */ 
/* 82 */   public void updateToTime(long totalTime) { this.label.updateToTime(totalTime); }
/*    */   
/*    */   private void readObject(ObjectInputStream s) throws IOException {
/*    */     try {
/* 86 */       this.text = ((String)s.readObject());
/*    */     } catch (ClassNotFoundException e) {
/* 88 */       e.printStackTrace();
/* 89 */       this.text = "#17加载失败,读取不到文字";
/*    */     }
/* 91 */     init();
/*    */   }
/*    */   
/*    */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 95 */     s.writeObject(this.text);
/*    */   }
/*    */   
/*    */   public int getFameIndex() {
/* 99 */     return this.label.getFrameIndex();
/*    */   }
/*    */ }


/* Location:              D:\Desktop\WasTools2.0-all.jar!\com\jmhxy\comps\ChatFloatPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */