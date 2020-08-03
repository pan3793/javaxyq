/*
 * JavaXYQ Engine
 *
 * javaxyq@2008 all rights.
 * http://www.javaxyq.com
 */

package open.xyq.core.ui;

import open.xyq.core.cfg.MapConfig;
import open.xyq.core.fmt.map.MapUnit;
import open.xyq.core.fmt.map.MaskUnit;
import open.xyq.core.fmt.map.TileMapProvider;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.lang.ref.SoftReference;
import java.util.HashSet;
import java.util.Set;

/**
 * @author 龚德伟
 * @history 2008-5-22 龚德伟 新建
 * 2013-12-25 wpaul modify
 */
public class TileMap extends AbstractWidget {
    // 地图块像素宽度
    public static final int MAP_BLOCK_WIDTH = 320;
    // 地图块像素高度
    public static final int MAP_BLOCK_HEIGHT = 240;
    // 步长
    public static final int STEP_DISTANCE = 20;

    private MapConfig config;
    private TileMapProvider provider;
    private SoftReference<Image>[][] blockTable;

    // 地图总宽
    private int width;
    // 地图总高
    private int height;
    // 地图水平方向块数
    private int hBlockCount;
    // 地图垂直方向块数
    private int vBlockCount;
    // 子地图Cell参数
    private final byte[][] cellData;

    // 子地图及mask图 参数
    private final MaskUnit[] maskUnits;
    private final BufferedImage[] tileImageDes;
    private ColorModel cm;
    private Object data;
    private int[] des;

    /**
     * 构造TileMap
     * 生成子地图tileImage
     * 生成MASK遮掩图tileImageDes
     */
    public TileMap(TileMapProvider provider, MapConfig cfg) {
        // 水平方向w块，垂直方向h块
        this.config = cfg;
        this.hBlockCount = provider.getHBlockCount();
        this.vBlockCount = provider.getVBlockCount();
        this.width = provider.getWidth();
        this.height = provider.getHeight();
        this.blockTable = new SoftReference[this.hBlockCount][this.vBlockCount];
        this.provider = provider;

        // total cell初始化
        int sWidth = (int) Math.ceil(width / 320.0) * 320 / STEP_DISTANCE;
        int sHeight = (int) Math.ceil(height / 240.0) * 240 / STEP_DISTANCE;
        cellData = new byte[sHeight][sWidth];

        // mask像素
        tileImageDes = new BufferedImage[provider.getMaskCount()];
        maskUnits = new MaskUnit[provider.getMaskCount()];

        // 读取子地图
        for (int y = 0; y < vBlockCount; y++) {
            for (int x = 0; x < hBlockCount; x++) {
                MapUnit mapUnit = provider.readUnit(x, y);
                // 子地图合成cell
                byte[] cellData = mapUnit.getCellData();
                for (int ch = 0; ch < 12; ch++)
                    System.arraycopy(cellData, ch * 16, this.cellData[y * 12 + ch], x * 16, 16);
            }
        }
    }


    /**
     * 加载totalRaster中KEYX,KEYY处像素
     * 至rasterDes处 并判断是否drawmask
     */
    //mask图规则标签
    public void loadMask(int viewX, int viewY) {
        //初始化MASK遮掩图并输出
        int firstTileX = pixelsToTilesWidth(viewX);
        int lastTileX = pixelsToTilesWidth(viewX + MAP_BLOCK_WIDTH * 2);
        int firstTileY = pixelsToTilesHeight(viewY);
        int lastTileY = pixelsToTilesHeight(viewY + MAP_BLOCK_HEIGHT * 3);

        lastTileX = Math.min(lastTileX, provider.getHBlockCount() - 1);
        lastTileY = Math.min(lastTileY, provider.getVBlockCount());

        //读取地图mask信息;
        for (int y = firstTileY; y < lastTileY; y++) {
            for (int x = firstTileX; x <= lastTileX; x++) {
                MapUnit mapUnit = provider.readUnit(x, y);
                int[] masknum = mapUnit.getMaskIndexs();
                for (int maskIndex : masknum) {
                    if (tileImageDes[maskIndex] != null)
                        continue;
                    MaskUnit maskUnit = getMaskUnit(maskIndex);
                    createTileMaskImg(maskIndex, maskUnit.getX(), maskUnit.getY(),
                            maskUnit.getWidth(), maskUnit.getHeight(), maskUnit.getData());
                }
            }

        }
        // TODO 释放没显示的mask图像
    }

    private MaskUnit getMaskUnit(int maskIndex) {
        MaskUnit maskUnit = maskUnits[maskIndex];
        if (maskUnit == null) {
            maskUnit = provider.readMask(maskIndex);
            maskUnits[maskIndex] = maskUnit;
        }
        return maskUnit;
    }

    private void createTileMaskImg(int unitnum, int keyx, int keyy, int width, int height, int[] mask) {
        if (tileImageDes[unitnum] != null) {
            return;
        }

        int firstTileX = pixelsToTilesWidth(keyx);
        int lastTileX = pixelsToTilesWidth(keyx + width - 1);
        int firstTileY = pixelsToTilesHeight(keyy);
        int lastTileY = pixelsToTilesHeight(keyy + height - 1);

        //合并子地图并输出像素值；
        BufferedImage tempMaskImage = new BufferedImage(
                (lastTileX + 1 - firstTileX) * 320,
                (lastTileY + 1 - firstTileY) * 240,
                BufferedImage.TYPE_3BYTE_BGR);
        WritableRaster tempMaskRaster = tempMaskImage.getRaster();
        for (int y = firstTileY; y < lastTileY + 1; y++) {
            for (int x = firstTileX; x < lastTileX + 1; x++) {
                //读取IMAGE数据
                BufferedImage Image = provider.getBlockImage(x, y);
                Raster raster = Image.getRaster();
                tempMaskRaster.setRect(320 * (x - firstTileX), 240 * (y - firstTileY), raster);
            }
        }
        writerRaster(tempMaskImage, tempMaskRaster, unitnum, keyx, keyy, width, height, mask);
    }

    // mask图规则标签
    private void writerRaster(BufferedImage tempMaskImage, WritableRaster tempMaskRaster,
                              int num, int refH, int refV, int width, int height, int[] mask) {
        if (tileImageDes[num] != null)
            return;

        tileImageDes[num] = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        cm = tempMaskImage.getColorModel();
        des = new int[4];
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                data = tempMaskRaster.getDataElements(refH % 320 + w, refV % 240 + h, null);
                int rgb = cm.getRGB(data);
                des[0] = (rgb & 0xFF0000) >> 16;
                des[1] = (rgb & 0x00FF00) >> 8;
                des[2] = (rgb & 0x0000FF);
                if (mask[h * width + w] == 3) {
                    des[3] = 110;
                } else if (mask[h * width + w] == 1) {
                    des[0] = 0;
                    des[1] = 0;
                    des[2] = 0;
                    des[3] = 0;
                } else {
                    des[3] = 0;
                }
                WritableRaster raster = tileImageDes[num].getRaster();
                raster.setPixel(w, h, des);
            }
        }
    }


    @Override
    protected void doDraw(Graphics2D g2, int x, int y, int width, int height) {
        // 1.计算Rect落在的图块
        Point pFirstBlock = viewToBlock(x, y);
        // 2.计算第一块地图相对ViewRect的偏移量,并将Graphics偏移
        int dx = pFirstBlock.x * MAP_BLOCK_WIDTH - x;
        int dy = pFirstBlock.y * MAP_BLOCK_HEIGHT - y;
        g2.translate(dx, dy);
        // 3.计算X轴,Y轴方向需要的地图块数量
        int offsetX = x;
        int offsetY = y;
        int firstTileX = pixelsToTilesWidth(offsetX);
        int lastTileX = pixelsToTilesWidth(offsetX + MAP_BLOCK_WIDTH * 2);
        int firstTileY = pixelsToTilesHeight(offsetY);
        int lastTileY = pixelsToTilesHeight(offsetY + MAP_BLOCK_HEIGHT * 3);

        lastTileX = Math.min(lastTileX, provider.getHBlockCount() - 1);
        lastTileY = Math.min(lastTileY, provider.getVBlockCount());

        // 4.从缓存获取地图块,画到Graphics上
        for (int i = firstTileX; i <= lastTileX; i++) {
            for (int j = firstTileY; j < lastTileY; j++) {
                Image b = getBlock(i, j);
                g2.drawImage(b, (i - firstTileX) * MAP_BLOCK_WIDTH, (j - firstTileY) * MAP_BLOCK_HEIGHT, null);

            }
        }

        loadMask(x, y);
    }

    @Override
    public void dispose() {
        this.provider.dispose();
        this.provider = null;
        for (SoftReference<Image>[] refs : this.blockTable)
            for (SoftReference<Image> ref : refs)
                ref.clear();
        this.blockTable = null;
    }

    @Override
    public boolean contains(int x, int y) {
        return true;
    }

    /**
     * 根据MASK前后数据关系
     * 判断是否 drawmask
     */
    public void drawMask(int playerRefX, int playerRefY, int playerWeight, int playerHeight, int pcoordx, int pcoordy, Graphics g, int viewx, int viewy, int offsetX, int offsetY) {
        // 1.计算Rect落在的图块
        /* Point pFirstBlock = viewToBlock(x, y);
        // 2.计算第一块地图相对ViewRect的偏移量,并将Graphics偏移
        int dx = pFirstBlock.x * MAP_BLOCK_WIDTH - x;
        int dy = pFirstBlock.y * MAP_BLOCK_HEIGHT - y;*/
        //System.out.println("x,y is:"+pFirstBlock.x+","+pFirstBlock.y);
        //g2.translate(dx, dy);
        //System.out.printf("x=%s,y=%s,dx=%s,dy=%s,block=%s\n", x, y, dx, dy, pFirstBlock);
        // 3.计算X轴,Y轴方向需要的地图块数量
        // doDraw已计算
        //System.out.printf("xCount=%s,yCount=%s\n",xCount,yCount);
        // 4.从缓存获取地图块,画到Graphics上

        //读取player信息及参数；
        // int pkeyx = player.getRefPixelX(); //person RefPixelX
        int pOffsetX = pcoordx - playerRefX;
        // int pkeyy = player.getRefPixelY();//person RefPixelY
        int pOffsetY = pcoordy - playerRefY;
        // int pwidth = player.getWidth();//person width
        // int pheight = player.getHeight(); //person height

        int offsetx = viewx;
        int offsety = viewy;
        int firstTileX = pixelsToTilesWidth(offsetx);
        int lastTileX = pixelsToTilesWidth(offsetx + MAP_BLOCK_WIDTH * 2);
        int firstTileY = pixelsToTilesHeight(offsety);
        int lastTileY = pixelsToTilesHeight(offsety + MAP_BLOCK_HEIGHT * 3);

        lastTileX = Math.min(lastTileX, provider.getHBlockCount() - 1);
        lastTileY = Math.min(lastTileY, provider.getVBlockCount());

        Set<Integer> paintedMaskSet = new HashSet<Integer>();
        // 4.从缓存获取地图块,画到Graphics上
        for (int y = firstTileY; y < lastTileY; y++) {
            for (int x = firstTileX; x <= lastTileX; x++) {

                MapUnit mapUnit = provider.readUnit(x, y);
                int[] masknum = mapUnit.getMaskIndexs();
                for (int maskIndex : masknum) {
                    //防止重复绘制同一个mask
                    if (paintedMaskSet.contains(maskIndex)) {
                        continue;
                    }
                    paintedMaskSet.add(maskIndex);

                    MaskUnit maskUnit = getMaskUnit(maskIndex);
                    int[] maskdata = maskUnit.getData();
                    //this.createTileMaskImg(maskIndex, maskUnit.getX(), maskUnit.getY(), maskUnit.getWidth(),
                    //	   maskUnit.getHeight(), maskdata);
                    //检测mask是否盖住人物或者NPC
                    boolean bDrawMask = true;//false;
                    if (pcoordx <= maskUnit.getX() + maskUnit.getWidth() - 1 && pcoordx >= maskUnit.getX()) {
                        bDrawMask = true;
                        int length = maskUnit.getWidth() * maskUnit.getHeight();
                        if (maskUnit.getY() + maskUnit.getHeight() < pcoordy) {
                            for (int l = length - maskUnit.getWidth() - 1; l < length; l++) {
                                if (maskdata[l] == 1) {
                                    bDrawMask = false;
                                }
                            }
                        } else if (maskUnit.getY() + maskUnit.getHeight() > pcoordy) {
                            int row = pcoordy - maskUnit.getY();
                            for (int l = row * (maskUnit.getWidth() - 1); l < row * maskUnit.getWidth(); l++) {
                                if (maskdata[l] == 1) {
                                    bDrawMask = false;
                                }
                            }
                        }
                    } else if (pcoordx < maskUnit.getX() && pOffsetX + playerWeight > maskUnit.getX()) {
                        bDrawMask = true;
                        int length = maskUnit.getWidth() * maskUnit.getHeight();
                        if (maskUnit.getY() + maskUnit.getHeight() < pcoordy) {
                            int col = maskUnit.getX() + maskUnit.getWidth() - (pOffsetX + playerWeight);
                            for (int l = length - maskUnit.getWidth() - 1; l < Math.min(length - col, length); l++) {
                                if (maskdata[l] == 1) {
                                    bDrawMask = false;
                                }
                            }
                        } else if (maskUnit.getY() + maskUnit.getHeight() > pcoordy && pcoordy > maskUnit.getY()) {
                            int col = maskUnit.getX() + maskUnit.getWidth() - (pOffsetX + playerWeight);
                            int row = pcoordy - maskUnit.getY();
                            for (int l = row * (maskUnit.getWidth() - 1);
                                 l < Math.min(row * maskUnit.getWidth() - col, row * maskUnit.getWidth()); l++) {

                                if (maskdata[l] == 1) {
                                    bDrawMask = false;
                                }
                            }
                        }

                    } else if (pcoordx > maskUnit.getX() + maskUnit.getWidth() - 1 &&
                            pOffsetX < maskUnit.getX() + maskUnit.getWidth() - 1) {
                        bDrawMask = true;
                        int length = maskUnit.getWidth() * maskUnit.getHeight();
                        if (maskUnit.getY() + maskUnit.getHeight() < pcoordy) {
                            int col = maskUnit.getX() + maskUnit.getWidth() - pOffsetX;
                            for (int l = length - Math.min(col, maskUnit.getWidth()); l < length; l++) {
                                if (maskdata[l] == 1) {
                                    bDrawMask = false;
                                }
                            }
                        } else if (maskUnit.getY() + maskUnit.getHeight() > pcoordy && pcoordy > maskUnit.getY()) {
                            int col = maskUnit.getX() + maskUnit.getWidth() - pOffsetX;
                            int row = pcoordy - maskUnit.getY();
                            for (int l = Math.max(row * maskUnit.getWidth() - col, maskUnit.getX());
                                 l < row * maskUnit.getWidth(); l++) {
                                if (maskdata[l] == 1) {
                                    bDrawMask = false;
                                }
                            }
                        }
                    }
                    if (bDrawMask) {
                        g.drawImage(this.tileImageDes[maskIndex], maskUnit.getX() - viewx, maskUnit.getY() - viewy, null);
                    }
                }
            }
        }

    }

    /**
     * 预加载此区域的地图块
     */
    public void prepare(int x, int y, int width, int height) {
        // 1.计算Rect落在的图块
        Point pFirstBlock = viewToBlock(x, y);
        // 2.计算第一块地图相对ViewRect的偏移量,并将Graphics偏移
        int dx = pFirstBlock.x * MAP_BLOCK_WIDTH - x;
        int dy = pFirstBlock.y * MAP_BLOCK_HEIGHT - y;
        // 3.计算X轴,Y轴方向需要的地图块数量
        int xCount = 1 + (width - dx - 1) / MAP_BLOCK_WIDTH;
        int yCount = 1 + (height - dy - 1) / MAP_BLOCK_HEIGHT;
        // 4.缓存此区域的地图块
        Image[][] images = new Image[xCount][yCount];
        for (int i = 0; i < xCount; i++) {
            for (int j = 0; j < yCount; j++) {
                Image img = getBlock(i + pFirstBlock.x, j + pFirstBlock.y);
                images[i][j] = img;
            }
        }
    }

    private int checkTable() {
        int count = 0;
        int width = this.blockTable.length;
        int height = this.blockTable[0].length;
        for (SoftReference<Image>[] softReferences : this.blockTable) {
            for (int j = 0; j < height; j++) {
                SoftReference<Image> reference = softReferences[j];
                if (reference != null && reference.get() != null) {
                    count++;
                }
            }
        }
        return count;
    }

    private Image getBlock(int x, int y) {
        SoftReference<Image> reference = this.blockTable[x][y];
        // 如果此地图块还没加载,则取地图块数据并生成图像
        // 如果GC由于低内存,已释放image,需要重新装载
        if (reference == null || reference.get() == null) {
            reference = new SoftReference<>(provider.getBlockImage(x, y));
            this.blockTable[x][y] = reference;
        }
        this.checkTable();
        return reference.get();
    }

    public int getXBlockCount() {
        return hBlockCount;
    }

    public void setXBlockCount(int blockCount) {
        hBlockCount = blockCount;
    }

    public int getYBlockCount() {
        return vBlockCount;
    }

    public void setYBlockCount(int blockCount) {
        vBlockCount = blockCount;
    }

    public int tilesWidthToPixels(int numTiles) {
        return numTiles * MAP_BLOCK_WIDTH;
    }

    public int pixelsToTilesWidth(int pixelCoord) {
        return pixelCoord / MAP_BLOCK_WIDTH;
    }

    public int tilesHeightToPixels(int numTiles) {
        return numTiles * MAP_BLOCK_HEIGHT;
    }

    public int pixelsToTilesHeight(int pixelCoord) {
        return pixelCoord / MAP_BLOCK_HEIGHT;
    }

    /**
     * 计算view坐标vp点对应的地图数据块位置 （即vp点落在哪个地图块上）
     *
     * @return the map block index of the vp
     */
    private Point viewToBlock(int x, int y) {
        Point p = new Point();
        p.x = x / MAP_BLOCK_WIDTH;
        p.y = y / MAP_BLOCK_HEIGHT;
        if (p.x < 0)
            p.x = 0;
        if (p.y < 0)
            p.y = 0;
        return p;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public MapConfig getConfig() {
        return config;
    }

    public void setConfig(MapConfig config) {
        this.config = config;
    }

    public byte[][] getCellData() {
        return cellData;
    }
}
