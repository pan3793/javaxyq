/*
 * JavaXYQ
 *
 * javaxyq 2008 all rights. http://www.javaxyq.com
 */

package open.xyq.core.cfg;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 龚德伟
 * @history 2008-5-21 龚德伟 新建
 */
@Getter
@Setter
public class PlayerConfig implements Config {

    public static final String STATE_STAND = "stand";
    public static final String STATE_WALK = "walk";

    private String id;
    private String character;
    private String name;
    private String state = STATE_STAND;
    private int direction;
    private int x;
    private int y;
    private String tints;
    private String movement;
    private long period;

    @Override
    public String getType() {
        return "PlayerConfig";
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "PlayerConfig[" +
                "id=" + this.id +
                "character=" + this.character +
                ",name=" + this.name +
                ",tints=" + this.tints +
                ",movement=" + this.movement +
                "]";
    }
}
