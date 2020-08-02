package open.xyq.core.fmt.was;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.image.BufferedImage;
import java.lang.ref.WeakReference;

@Getter
@Setter
@NoArgsConstructor
public class WasFrame {

    // 帧数据偏移
    private int frameOffset;
    // 行数据偏移
    private int[] lineOffsets;
    // 延时帧数
    private int delay = 1;
    // 图像大小
    private int height;
    private int width;
    // 图像偏移
    private int x;
    private int y;

    // Image格式图像
    private WeakReference<BufferedImage> image;

    /**
     * 图像原始数据<br>
     * 0-15位RGB颜色(565)<br>
     * 16-20为alpha值<br>
     * pixels[x+y*width]
     */
    private int[] pixels;

    public WasFrame(int x, int y, int width, int height, int delay, int frameOffset, int[] lineOffset) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.delay = delay;
        this.frameOffset = frameOffset;
        this.lineOffsets = lineOffset;
    }

//    /**
//     * 将图像数据画到Image上
//     */
//    public void draw(WritableRaster raster, int x, int y) {
//        int iArray[] = new int[4];
//        for (int y1 = 0; y1 < height; y1++) {
//            for (int x1 = 0; x1 < width; x1++) {
//                iArray[0] = ((pixels[y1][x1] >>> 11) & 0x1F) << 3;
//                iArray[1] = ((pixels[y1][x1] >>> 5) & 0x3f) << 2;
//                iArray[2] = (pixels[y1][x1] & 0x1F) << 3;
//                iArray[3] = ((pixels[y1][x1] >>> 16) & 0x1f) << 3;
//                raster.setPixel(x1 + x, y1 + y, iArray);
//            }
//        }
//    }
//
//    public BufferedImage getImage() {
//        if (image == null || image.get() == null) {
//            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//            draw(img.getRaster(), 0, 0);
//            image = new WeakReference<BufferedImage>(img);
//        }
//        return image.get();
//    }
}
