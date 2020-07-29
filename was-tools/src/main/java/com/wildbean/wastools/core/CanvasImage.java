package com.wildbean.wastools.core;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;

public abstract interface CanvasImage
  extends Cloneable
{
  public static final String SPRITE_INDEX_CHANGED = "sprite index changed";
  public static final String DATA_CHANGED = "image data changed";
  public static final int TYPE_STANDARD = 0;
  public static final int TYPE_SPRITE = 1;
  public static final int TYPE_CHAT_TEXT = 2;
  
  public abstract void draw(Image paramImage);
  
  public abstract void update(long paramLong);
  
  public abstract void setSpriteIndex(int paramInt);
  
  public abstract void setFrameIndex(int paramInt);
  
  public abstract void setData(Object paramObject);
  
  public abstract int getX();
  
  public abstract int getY();
  
  public abstract int getWidth();
  
  public abstract int getHeight();
  
  public abstract String getName();
  
  public abstract String getInfo();
  
  public abstract int getType();
  
  public abstract int getSpriteCount();
  
  public abstract int getSpriteIndex();
  
  public abstract int getFrameCount();
  
  public abstract int getFrameIndex();
  
  public abstract boolean isVisible();
  
  public abstract void setVisible(boolean paramBoolean);
  
  public abstract void setName(String paramString);
  
  public abstract boolean contain(Point paramPoint);
  
  public abstract void translate(int paramInt1, int paramInt2);
  
  public abstract CanvasImage clone();
  
  public abstract CanvasImage getLinkedBase();
  
  public abstract void setLinkedBase(CanvasImage paramCanvasImage);
  
  public abstract void setBase(boolean paramBoolean);
  
  public abstract void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener);
  
  public abstract void setLocation(int paramInt1, int paramInt2);
  
  public abstract void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener);
  
  public abstract void fireDataChanged();
  
  public abstract boolean isBase();
  
  public abstract void alignToCenter(CanvasImage paramCanvasImage);
  
  public abstract void alignToCenter(Rectangle paramRectangle);
  
  public abstract void fireSpriteIndexChanged();
  
  public abstract int getTotalDelay();
  
  public abstract Object getData();
  
  public abstract Icon getIcon();
  
  public abstract boolean isLinked();
  
  public abstract boolean isLinked(Object paramObject);
  
  public abstract Rectangle getBounds();
  
  public abstract String getTypeName();
}


/* Location:              D:\Desktop\WasTools2.0-all.jar!\com\wildbean\wastools\core\CanvasImage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */