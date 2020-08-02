package open.xyq.core.cfg;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 龚德伟
 * @history 2008-5-21 龚德伟 新建
 */
@AllArgsConstructor
public class MapConfig implements Config {
    @Getter
    private final String id;
    @Getter
    private final String name;
    @Getter
    private final String path;
    @Getter
    private final String music;

    @Override
    public String getType() {
        return "map";
    }

    @Override
    public String toString() {
        return "MapConfig{" + id + "," + name + "," + path + "}";
    }
}
