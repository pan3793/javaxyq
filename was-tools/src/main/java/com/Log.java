package com;

import org.slf4j.LoggerFactory;

/**
* @Description: 日志管理工具
* @author Miao
* @date 2015年12月15日 上午10:32:54
*
*/
public class Log {
	/**
	 * 错误
	 * @param name 名称
	 * @param message 内容
	 */
	public static void error(String name, String message) {
		LoggerFactory.getLogger(name).error(message);
	}

	/**
	 * 警告
	 * @param name 名称
	 * @param message 内容
	 */
	public static void warn(String name, String message) {
		LoggerFactory.getLogger(name).warn(message);
	}

	/**
	 * 信息
	 * @param name 名称
	 * @param message 内容
	 */
	public static void info(String name, String message) {
		LoggerFactory.getLogger(name).info(message);
	}

	/**
	 * 测试
	 * @param name 名称
	 * @param message 内容
	 */
	public static void debug(String name, String message) {
		LoggerFactory.getLogger(name).debug(message);
	}

}
