package com.mxxy.game.resources;

public interface ResourceProvider<E> {

	E getResource(String paramString);

	void dispose();
}