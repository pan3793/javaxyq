/*
 * JavaXYQ Engine
 *
 * javaxyq@2008 all rights.
 * http://www.javaxyq.com
 */

package com.javaxyq.resources;

import com.javaxyq.ResourceProvider;
import open.xyq.core.Pluggable;
import com.javaxyq.TileMap;

/**
 * @author 龚德伟
 * @history 2008-5-22 龚德伟 新建
 */
public interface MapProvider extends ResourceProvider<TileMap>, Pluggable {

    int getWidth();

    int getHeight();
}
