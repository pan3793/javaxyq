/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2010-3-4
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.event;

import lombok.Getter;

import java.util.EventObject;

/**
 * 下载事件对象
 *
 * @author gongdewei
 * @date 2010-3-4 create
 */
@Getter
public class DownloadEvent extends EventObject {

    private static final long serialVersionUID = -4581277706915926395L;

    public static final int DOWNLOAD_STARTED = 1;
    public static final int DOWNLOAD_COMPLETED = 2;
    public static final int DOWNLOAD_INTERRUPTED = 3;
    public static final int DOWNLOAD_UPDATE = 4;

    private final int id;
    private final String resource;
    private final int size;
    private final int received;

    public DownloadEvent(Object source, int id, String resource) {
        this(source, id, resource, -1, 0);
    }

    public DownloadEvent(Object source, int id, String resource, int size, int received) {
        super(source);
        this.id = id;
        this.resource = resource;
        this.size = size;
        this.received = received;
    }
}
