package com.mxxy.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import com.mxxy.protocol.Message;

public interface IClient {

	void connect(String HostIp, int HostListenningPort);

	void initialize() throws IOException;

	void send(Message paramMessage) throws IOException;

	void send(ByteBuffer bufOut) throws IOException;

	boolean isThreadStart();

	/**
	 * 开启接收线程
	 */
	void openReadThread() throws InterruptedException;

	/**
	 * 关闭接收线程
	 */
	void closeReadThread();

	String getIp();

	int getPort();

	InetSocketAddress getInetSocketAddress();
}
