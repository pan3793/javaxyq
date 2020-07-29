package com.jmhxy.encoder;

import java.awt.*;
import java.io.OutputStream;
import java.util.List;
import java.util.Vector;

public class WasEncoder {

    short imageHeaderSize;
    short[] palette;
    int width;
    int height;
    int centerX;
    int centerY;
    int frameCount;
    int spriteCount;
    Vector<Image> images;
    Vector<Integer> delays;

    public WasEncoder(int x, int y, int width, int height, int spriteCount, int frameCount) {
        /* 51 */
        this.centerX = x;
        /* 52 */
        this.centerY = y;
        /* 53 */
        this.width = width;
        /* 54 */
        this.height = height;
        /* 55 */
        this.spriteCount = spriteCount;
        /* 56 */
        this.frameCount = frameCount;
    }

    public WasEncoder(int x, int y, int width, int height) {
        /* 68 */
        this(x, y, width, height, 1, -1);

    }

    public WasEncoder(int width, int height) {
        /* 79 */
        this(0, 0, width, height, 1, -1);
    }

    public void addFrame(Image image) {
        /* 83 */
        addFrame(image, 1);
    }

    public void addFrames(List<Image> images) {
        /* 87 */
        for (Image image : images) {
            /* 88 */
            addFrame(image, 1);
        }
    }

    public void addFrame(Image image, int delay) {
        /* 93 */
        this.images.add(image);
        /* 94 */
        this.delays.add(Integer.valueOf(delay));
    }

    public void encode(OutputStream out) {
    }
}