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
 * @history 2008-6-7 龚德伟 新建
 */
@Getter
@Setter
public class ImageConfig implements Config {
    private String id;
    private String path;
    private int x;
    private int y;
    private int width;
    private int height;

    public ImageConfig(String path) {
        this.path = path;
    }

    @Override
    public String getType() {
        return "image";
    }
}
