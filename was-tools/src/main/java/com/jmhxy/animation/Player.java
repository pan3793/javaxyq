/*     */ package com.jmhxy.animation;
/*     */ 
/*     */ import com.jmhxy.comps.ChatFloatPanel;
/*     */ import com.jmhxy.core.GameMain;
/*     */ import com.jmhxy.core.SpriteFactory;
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Point;
/*     */ import java.awt.RenderingHints;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
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
/*     */ public class Player
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   public static final float NORMAL_SPEED = 0.1F;
/*     */   public static final int STATE_MOVING = 1;
/*     */   public static final int STATE_NORMAL = 0;
/*     */   private Sprite character;
/*     */   private Sprite weapon;
/*     */   private Sprite shadow;
/*     */   HashMap<Integer, Sprite> characters;
/*     */   HashMap<Integer, Sprite> weapons;
/*     */   private String name;
/*     */   private int state;
/*     */   private int x;
/*     */   private int y;
/*     */   private int angle;
/*     */   private Color nameForeground;
/*     */   private Color nameBackground;
/*     */   private ChatFloatPanel chatPanel;
/*     */   private String chatText;
/*     */   private Font nameFont;
/*     */   
/*     */   public Player(String name)
/*     */   {
/*  63 */     this.name = name;
/*  64 */     this.characters = new HashMap();
/*  65 */     this.weapons = new HashMap();
/*  66 */     this.shadow = SpriteFactory.loadSprite("/Resources/character/shadow.was");
/*  67 */     this.nameFont = GameMain.TEXT_NAME_FONT;
/*  68 */     this.nameForeground = GameMain.TEXT_NAME_COLOR;
/*  69 */     this.nameBackground = GameMain.TEXT_NAME_BGCOLOR;
/*  70 */     this.chatPanel = new ChatFloatPanel();
/*     */   }
/*     */   
/*     */   public void changeAngle(Point mouse) {
/*  74 */     Point center = this.character.getLocation();
/*  75 */     int angle = Sprite.computeAngle(center, mouse);
/*  76 */     setAngle(angle);
/*     */   }
/*     */   
/*     */   public boolean contains(Point p) {
/*  80 */     boolean b = (this.character.contains(p)) || (this.shadow.contains(p));
/*  81 */     if ((this.weapon != null) && (!b))
/*  82 */       b = this.weapon.contains(p);
/*  83 */     return b;
/*     */   }
/*     */   
/*     */   public void draw(Graphics g) {
/*  87 */     this.shadow.draw(g);
/*  88 */     this.character.draw(g);
/*  89 */     if (this.weapon != null)
/*  90 */       this.weapon.draw(g);
/*  91 */     g.setFont(this.nameFont);
/*  92 */     int textY = this.character.getY() + 30;
/*  93 */     int textX = this.character.x - g.getFontMetrics().stringWidth(this.name) / 2;
/*  94 */     Graphics2D g2d = (Graphics2D)g.create();
/*  95 */     g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
/*  96 */     g2d.setColor(this.nameBackground);
/*  97 */     g2d.drawString(this.name, textX + 1, textY + 1);
/*  98 */     g2d.setColor(this.nameForeground);
/*  99 */     g2d.drawString(this.name, textX, textY);
/* 100 */     g2d.dispose();
/*     */     
/* 102 */     if ((this.chatText != null) && (this.chatText.length() > 0)) {
/* 103 */       int chatX = this.x - this.chatPanel.getWidth() / 2;
/* 104 */       int chatY = this.y - this.character.centerY - 10 - this.chatPanel.getHeight();
/* 105 */       Graphics g2 = g.create(chatX, chatY, this.chatPanel.getWidth(), this.chatPanel.getHeight());
/* 106 */       this.chatPanel.paint(g2);
/* 107 */       g2.dispose();
/*     */     }
/*     */   }
/*     */   
/*     */   public Point getLocation() {
/* 112 */     return this.character.getLocation();
/*     */   }
/*     */   
/*     */   public String getName() {
/* 116 */     return this.name;
/*     */   }
/*     */   
/*     */   public String getChatText() {
/* 120 */     return this.chatText;
/*     */   }
/*     */   
/*     */   public void setChatText(String chatText) {
/* 124 */     this.chatText = chatText;
/* 125 */     this.chatPanel.setText(chatText);
/*     */   }
/*     */   
/*     */   public void move() {
/* 129 */     setState(1);
/* 130 */     setAngle(this.angle);
/* 131 */     switch (this.angle) {
/*     */     case 7: 
/* 133 */       setVelocityX(0.1F);
/* 134 */       setVelocityY(0.0F);
/* 135 */       break;
/*     */     case 4: 
/* 137 */       setVelocityX(0.0F);
/* 138 */       setVelocityY(0.1F);
/* 139 */       break;
/*     */     case 5: 
/* 141 */       setVelocityX(-0.1F);
/* 142 */       setVelocityY(0.0F);
/* 143 */       break;
/*     */     case 6: 
/* 145 */       setVelocityX(0.0F);
/* 146 */       setVelocityY(-0.1F);
/* 147 */       break;
/*     */     case 0: 
/* 149 */       setVelocityX(0.1F);
/* 150 */       setVelocityY(0.1F);
/* 151 */       break;
/*     */     case 1: 
/* 153 */       setVelocityX(-0.1F);
/* 154 */       setVelocityY(0.1F);
/* 155 */       break;
/*     */     case 3: 
/* 157 */       setVelocityX(0.1F);
/* 158 */       setVelocityY(-0.1F);
/* 159 */       break;
/*     */     case 2: 
/* 161 */       setVelocityX(-0.1F);
/* 162 */       setVelocityY(-0.1F);
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void put(int state, Sprite character, Sprite weapon)
/*     */   {
/* 175 */     if (character == null)
/* 176 */       throw new IllegalArgumentException("character cann't be null!");
/* 177 */     this.characters.put(Integer.valueOf(state), character);
/* 178 */     this.weapons.put(Integer.valueOf(state), weapon);
/*     */   }
/*     */   
/*     */   public void finish() {
/* 182 */     this.character = ((Sprite)this.characters.get(Integer.valueOf(0)));
/* 183 */     this.weapon = ((Sprite)this.weapons.get(Integer.valueOf(0)));
/*     */   }
/*     */   
/*     */   public void setAngle(int angle) {
/* 187 */     this.angle = angle;
/* 188 */     this.character.setAngle(angle);
/* 189 */     if (this.weapon != null) {
/* 190 */       this.weapon.setAngle(angle);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setLocation(Point location) {
/* 195 */     setLocation(location.x, location.y);
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/* 199 */     this.name = name;
/*     */   }
/*     */   
/*     */   public void setState(int state) {
/* 203 */     if (state < this.characters.size()) {
/* 204 */       this.state = state;
/* 205 */       this.character = ((Sprite)this.characters.get(Integer.valueOf(state)));
/* 206 */       this.weapon = ((Sprite)this.weapons.get(Integer.valueOf(state)));
/*     */     }
/*     */   }
/*     */   
/*     */   public int getState() {
/* 211 */     return this.state;
/*     */   }
/*     */   
/*     */   public void setVelocityX(float dx) {
/* 215 */     this.character.setVelocityX(dx);
/*     */   }
/*     */   
/*     */   public void setVelocityY(float dy) {
/* 219 */     this.character.setVelocityY(dy);
/*     */   }
/*     */   
/*     */   public void stop() {
/* 223 */     setState(0);
/* 224 */     setVelocityX(0.0F);
/* 225 */     setVelocityY(0.0F);
/*     */   }
/*     */   
/*     */   public void update(long elapsedTime) {
/* 229 */     this.character.x = this.x;
/* 230 */     this.character.y = this.y;
/* 231 */     this.shadow.update(elapsedTime);
/* 232 */     this.character.update(elapsedTime);
/* 233 */     this.shadow.x = this.character.x;
/* 234 */     this.shadow.y = this.character.y;
/* 235 */     if (this.weapon != null) {
/* 236 */       this.weapon.update(elapsedTime);
/* 237 */       this.weapon.x = this.character.x;
/* 238 */       this.weapon.y = this.character.y;
/*     */     }
/* 240 */     this.x = this.character.x;
/* 241 */     this.y = this.character.y;
/*     */   }
/*     */   
/*     */   public boolean isMoving() {
/* 245 */     return this.state == 1;
/*     */   }
/*     */   
/*     */   public Color getNameBackground() {
/* 249 */     return this.nameBackground;
/*     */   }
/*     */   
/*     */   public void setNameBackground(Color textBackground) {
/* 253 */     this.nameBackground = textBackground;
/*     */   }
/*     */   
/*     */   public Color getNameForeground() {
/* 257 */     return this.nameForeground;
/*     */   }
/*     */   
/*     */   public void setNameForeground(Color textForeground) {
/* 261 */     this.nameForeground = textForeground;
/*     */   }
/*     */   
/*     */   public int getAngle() {
/* 265 */     return this.angle;
/*     */   }
/*     */   
/*     */   public void setLocation(int x, int y) {
/* 269 */     this.x = x;
/* 270 */     this.y = y;
/*     */   }
/*     */ }


/* Location:              D:\Desktop\WasTools2.0-all.jar!\com\jmhxy\animation\Player.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */