package com.mxxy.game.resources;

import com.mxxy.game.widget.TileMap;

/**
 * Provider
 */
public interface IMapProvider extends ResourceProvider<TileMap> {

	int getWidth();

	int getHeight();
}