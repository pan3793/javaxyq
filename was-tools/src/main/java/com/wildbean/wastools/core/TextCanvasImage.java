/*    */ package com.wildbean.wastools.core;
/*    */ 
/*    */ import com.jmhxy.comps.ChatFloatPanel;
/*    */ import com.jmhxy.util.Utils;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Image;
/*    */ import javax.swing.Icon;
/*    */ 
/*    */ public class TextCanvasImage extends AbstractCanvasImage
/*    */ {
/*    */   private static final long serialVersionUID = -7438090164278729921L;
/*    */   private ChatFloatPanel imageData;
/*    */   
/*    */   public TextCanvasImage(ChatFloatPanel chat)
/*    */   {
/* 16 */     this.imageData = chat;
/* 17 */     setName(getText());
/*    */   }
/*    */   
/*    */   public TextCanvasImage(String text) {
/* 21 */     this(new ChatFloatPanel(text));
/*    */   }
/*    */   
/*    */   public void draw(Image bgImage) {
/* 25 */     this.imageData.paint(bgImage.getGraphics().create(this.x, this.y, getWidth(), getHeight()));
/*    */   }
/*    */   
/*    */   public int getHeight() {
/* 29 */     return this.imageData.getHeight();
/*    */   }
/*    */   
/*    */   public Icon getIcon() {
/* 33 */     return Utils.loadIcon("text.png");
/*    */   }
/*    */   
/*    */   public int getWidth() {
/* 37 */     return this.imageData.getWidth();
/*    */   }
/*    */   
/*    */   public int getType()
/*    */   {
/* 42 */     return 2;
/*    */   }
/*    */   
/*    */   public void update(long elapsedTime)
/*    */   {
/* 47 */     this.imageData.update(elapsedTime);
/* 48 */     this.frameIndex = this.imageData.getFameIndex();
/*    */   }
/*    */   
/*    */   public int getFrameCount()
/*    */   {
/* 53 */     return this.imageData.getFrameCount();
/*    */   }
/*    */   
/*    */   public void setText(String text) {
/* 57 */     this.imageData.setText(text);
/* 58 */     setName(text);
/* 59 */     fireDataChanged();
/*    */   }
/*    */   
/*    */   public String getText() {
/* 63 */     return this.imageData.getText();
/*    */   }
/*    */   
/*    */   public void setFrameIndex(int index)
/*    */   {
/* 68 */     long totalTime = (index + 1) * 100 - 1;
/* 69 */     this.imageData.updateToTime(totalTime);
/* 70 */     this.frameIndex = index;
/*    */   }
/*    */   
/*    */   public CanvasImage clone()
/*    */   {
/* 75 */     TextCanvasImage newImage = new TextCanvasImage(getText());
/* 76 */     newImage.setLocation(this.x, this.y);
/* 77 */     newImage.setVisible(this.visible);
/* 78 */     newImage.setName(this.name);
/* 79 */     newImage.setFrameIndex(getFrameIndex());
/* 80 */     return newImage;
/*    */   }
/*    */   
/*    */   public String getInfo()
/*    */   {
/* 85 */     return "文本:\n" + getText();
/*    */   }
/*    */   
/*    */   public String getData() {
/* 89 */     return this.imageData.getText();
/*    */   }
/*    */   
/*    */   public void setData(Object imageData) {
/* 93 */     String text = (String)imageData;
/* 94 */     this.imageData.setText(text);
/*    */   }
/*    */ }


/* Location:              D:\Desktop\WasTools2.0-all.jar!\com\wildbean\wastools\core\TextCanvasImage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */