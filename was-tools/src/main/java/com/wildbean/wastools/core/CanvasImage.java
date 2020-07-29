package com.wildbean.wastools.core;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;

public interface CanvasImage extends Cloneable {
    String SPRITE_INDEX_CHANGED = "sprite index changed";
    String DATA_CHANGED = "image data changed";
    int TYPE_STANDARD = 0;
    int TYPE_SPRITE = 1;
    int TYPE_CHAT_TEXT = 2;

    void draw(Image paramImage);

    void update(long paramLong);

    void setSpriteIndex(int paramInt);

    void setFrameIndex(int paramInt);

    void setData(Object paramObject);

    int getX();

    int getY();

    int getWidth();

    int getHeight();

    String getName();

    String getInfo();

    int getType();

    int getSpriteCount();

    int getSpriteIndex();

    int getFrameCount();

    int getFrameIndex();

    boolean isVisible();

    void setVisible(boolean paramBoolean);

    void setName(String paramString);

    boolean contain(Point paramPoint);

    void translate(int paramInt1, int paramInt2);

    CanvasImage clone();

    CanvasImage getLinkedBase();

    void setLinkedBase(CanvasImage paramCanvasImage);

    void setBase(boolean paramBoolean);

    void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener);

    void setLocation(int paramInt1, int paramInt2);

    void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener);

    void fireDataChanged();

    boolean isBase();

    void alignToCenter(CanvasImage paramCanvasImage);

    void alignToCenter(Rectangle paramRectangle);

    void fireSpriteIndexChanged();

    int getTotalDelay();

    Object getData();

    Icon getIcon();

    boolean isLinked();

    boolean isLinked(Object paramObject);

    Rectangle getBounds();

    String getTypeName();
}