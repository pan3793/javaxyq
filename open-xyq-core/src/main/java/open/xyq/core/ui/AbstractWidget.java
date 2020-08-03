/*
 * JavaXYQ Engine
 *
 * javaxyq@2008 all rights.
 * http://www.javaxyq.com
 */

package open.xyq.core.ui;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import open.xyq.core.ui.Widget;

import java.awt.*;

/**
 * Widget 的抽象类
 *
 * @author 龚德伟
 * @history 2008-5-30 龚德伟 新建
 */
@Slf4j
@Getter
@Setter
public abstract class AbstractWidget implements Widget {
    private float alpha = 1.0f;
    private int width;
    private int height;

    protected abstract void doDraw(Graphics2D g2, int x, int y, int width, int height);

    @Override
    public void draw(Graphics g, int x, int y) {
        this.draw(g, x, y, width, height);
    }

    @Override
    public void draw(Graphics g, Rectangle rect) {
        this.draw(g, rect.x, rect.y, rect.width, rect.height);
    }

    @Override
    public void draw(Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            doDraw(g2, x, y, width, height);
        } catch (Exception e) {
            log.warn("", e);
        } finally {
            g2.dispose();
        }
    }

    @Override
    public void fadeIn(long t) {
    }

    @Override
    public void fadeOut(long t) {
        long duration = t;
        long interval = t / 10;
        AnimatorThread thread = new AnimatorThread(duration, interval);
        thread.start();
    }

    private class AnimatorThread extends Thread {
        /**
         * 动画刷新的间隔
         */
        private long interval;

        /**
         * 动画总时间
         */
        private long duration;

        private long passTime;

        public AnimatorThread(long duration, long interval) {
            this.duration = duration;
            this.interval = interval;
            setName("animator");
        }

        @Override
        public void run() {
            while (passTime < duration) {
                //System.out.println(this.getId()+" "+this.getName());
                passTime += interval;
                alpha = 1.0f - 1.0f * passTime / duration;
                if (alpha < 0) {
                    alpha = 0;
                }
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
