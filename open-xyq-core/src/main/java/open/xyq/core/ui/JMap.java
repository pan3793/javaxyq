package open.xyq.core.ui;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import open.xyq.core.fmt.map.TileMapProvider;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.LinkedList;

/**
 * 显示游戏地图的控件
 */
@Slf4j
public class JMap extends JComponent {

    private static final long serialVersionUID = 1L;

    private final Rectangle maxVisibleRect = new Rectangle();

    private LinkedList<MapImage> loadedImages = new LinkedList<>();

    @Getter
    private TileMapProvider decoder;

    private MapImage[][] visibleImages;

    public JMap() {
    }

    public JMap(File file) {
        loadMap(file);
    }

    @Override
    public void paint(Graphics g) {
        paintComponent(g);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (decoder == null)
            return;
        Rectangle clipRect = getVisibleRect();
        if (!maxVisibleRect.contains(clipRect)) {
            loadRectMapData(clipRect);
        }
        drawMap(g);
    }

    /**
     * 加载一个文件
     */
    public boolean loadMap(File file) {
        if (file == null)
            return false;
        try {
            decoder = new TileMapProvider(file);
            loadedImages = new LinkedList<>();
            maxVisibleRect.height = 0;
            maxVisibleRect.width = 0;
            setSize(decoder.getWidth(), decoder.getHeight());
            setPreferredSize(new Dimension(decoder.getWidth(), decoder.getHeight()));
        } catch (Exception e) {
            log.error("打开地图文件出错: {}", file.getName(), e);
            return false;
        }
        return true;
    }

    /**
     * 在g上画出当前可见区域对应的地图图像
     */
    protected void drawMap(Graphics g) {
        g = g.create();
        MapImage img0 = visibleImages[0][0];
        int xOffset = img0.h * 320;
        int yOffset = img0.v * 240;
        g.translate(xOffset, yOffset);
        for (int h = 0; h < visibleImages.length; h++) {
            for (int v = 0; v < visibleImages[0].length; v++) {
                g.drawImage(visibleImages[h][v].image, h * 320, v * 240, this);
            }
        }
        g.dispose();
    }

    /**
     * 绘制指定区域
     */
    public void drawRect(Graphics g, Rectangle clipRect) {
        if (decoder == null)
            return;

        if (!maxVisibleRect.contains(clipRect))
            loadRectMapData(clipRect);

        g = g.create();
        MapImage img0 = visibleImages[0][0];
        int xOffset = img0.h * 320 - clipRect.x;
        int yOffset = img0.v * 240 - clipRect.y;
        g.translate(xOffset, yOffset);
        for (int h = 0; h < visibleImages.length; h++) {
            for (int v = 0; v < visibleImages[0].length; v++) {
                g.drawImage(visibleImages[h][v].image, h * 320, v * 240, this);
            }
        }
        g.dispose();
    }

    public Dimension getMapSize() {
        return new Dimension(decoder.getWidth(), decoder.getHeight());
    }

    public Dimension getSegments() {
        return new Dimension(decoder.getHBlockCount(), decoder.getVBlockCount());
    }

    /**
     * 加载clipRect用到的地图块
     */
    private void loadRectMapData(Rectangle clipRect) {
        MapArea mapArea = getMapArea(clipRect);
        MapImage mapimage;
        int rows = mapArea.right - mapArea.left + 1;
        int cols = mapArea.bottom - mapArea.top + 1;
        visibleImages = new MapImage[rows][cols];

        for (int h = mapArea.left; h <= mapArea.right; h++) {
            for (int v = mapArea.top; v <= mapArea.bottom; v++) {
                mapimage = removeMapImage(h, v);
                if (mapimage == null) {
                    Image image = decoder.getBlockImage(h, v);
                    mapimage = new MapImage(image, h, v);
                }
                loadedImages.add(0, mapimage);
                if (loadedImages.size() > 16) {
                    loadedImages.removeLast();
                }
                visibleImages[h - mapArea.left][v - mapArea.top] = mapimage;
            }
        }
        maxVisibleRect.x = mapArea.left * 320;
        maxVisibleRect.y = mapArea.top * 240;
        maxVisibleRect.width = (mapArea.right - mapArea.left + 1) * 320;
        maxVisibleRect.height = (mapArea.bottom - mapArea.top + 1) * 240;
    }

    /**
     * 获得clipRect对应地图数据块区域
     */
    private MapArea getMapArea(Rectangle clipRect) {
        MapArea mapArea = new MapArea();
        mapArea.left = (int) Math.ceil(clipRect.x / 320.0) - 1;
        mapArea.top = (int) Math.ceil(clipRect.y / 240.0) - 1;
        mapArea.right = (int) Math.ceil((clipRect.x + clipRect.width) / 320.0) - 1;
        mapArea.bottom = (int) Math.ceil((clipRect.y + clipRect.height) / 240.0) - 1;
        if (mapArea.left < 0)
            mapArea.left = 0;
        if (mapArea.top < 0)
            mapArea.top = 0;
        return mapArea;
    }

    /**
     * 从缓冲区获取某个图像块
     */
    private MapImage removeMapImage(int h, int v) {
        int index = -1;
        for (int i = 0; i < loadedImages.size(); i++) {
            MapImage mapImage = loadedImages.get(i);
            if (mapImage.h == h && mapImage.v == v) {
                index = i;
                break;
            }
        }
        return index == -1 ? null : loadedImages.remove(index);
    }

    /**
     * 用于描述游戏地图数据块区域
     */
    static class MapArea {
        public int left;
        public int right;
        public int top;
        public int bottom;
    }

    /**
     * 用于描述地图的一片解码后的数据
     */
    @AllArgsConstructor
    static class MapImage {
        public Image image;
        public int h;
        public int v;
    }
}
