/*
 * JavaXYQ Engine
 *
 * javaxyq@2008 all rights.
 * http://www.javaxyq.com
 */

package open.xyq.core;

import open.xyq.core.ui.TileMap;

/**
 * @author 龚德伟
 * @history 2008-5-22 龚德伟 新建
 */
public interface MapProvider extends ResourceProvider<TileMap>, Pluggable {

    int getWidth();

    int getHeight();
}
