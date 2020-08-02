/*
 * JavaXYQ Engine
 *
 * javaxyq@2008 all rights.
 * http://www.javaxyq.com
 */

package open.xyq.core.config;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 龚德伟
 * @history 2008-6-9 龚德伟 新建
 */
@Getter
@Setter
public class LabelConfig implements Config {
    private int x;
    private int y;
    private int width;
    private int height;
    private String name;
    private String text;
    private String color;
    private String align;

    public LabelConfig(int x, int y, int width, int height, String text) {
        this.setLocation(x, y);
        this.setSize(width, height);
        this.text = text;
    }

    @Override
    public String getType() {
        return "label";
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
