/*
 * JavaXYQ Engine
 *
 * javaxyq@2008 all rights.
 * http://www.javaxyq.com
 */

package open.xyq.core.cfg;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 龚德伟
 * @history 2008-5-21 龚德伟 新建
 */
@Getter
@Setter
@AllArgsConstructor
public class StateConfig implements Config {
    private String id;
    private String character;
    private String weapon;

    @Override
    public String getType() {
        return "state";
    }
}
