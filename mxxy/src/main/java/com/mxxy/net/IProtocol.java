package com.mxxy.net;

import java.io.IOException;
import java.nio.channels.SelectionKey;

public interface IProtocol {
	/**
	 * 接收一个SocketChannel的处理
	 */
	void handleAccept(SelectionKey key) throws IOException;

	/**
	 * 从一个SocketChannel读取信息的处理
	 */
	void handleRead(SelectionKey key) throws IOException;

	/**
	 * 向一个SocketChannel写入信息的处理
	 */
	void handleWrite(SelectionKey key) throws IOException;
}
