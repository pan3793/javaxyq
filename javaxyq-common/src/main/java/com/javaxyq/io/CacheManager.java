/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2010-3-4
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.io;

import lombok.extern.slf4j.Slf4j;
import open.xyq.core.util.IoUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 缓存管理器
 *
 * @author gongdewei
 * @date 2010-3-4 create
 */
@Slf4j
public class CacheManager {

    public static CacheManager getInstance() {
        return instance;
    }

    private static final CacheManager instance = new CacheManager();

    private String cacheBase;
    private URL documentBase;

    private CacheManager() {
    }

    public String getCacheBase() {
        if (cacheBase == null) {
            if (documentBase == null) {
                cacheBase = new File(".").getAbsolutePath();
            } else {
                cacheBase = System.getProperty("user.home") + "/javaxyq";
            }
        }
        return cacheBase;
    }

    /**
     * 创建文件 如果文件已存在，则删除旧的并重新创建一个
     */
    public File createFile(String filename) throws IOException {
        File file = null;
        if (documentBase == null) {
            file = new File(".", filename);
        } else {
            file = new File(cacheBase, filename);
        }
        if (file.exists()) {
            file.delete();
        }
        file.getParentFile().mkdirs();
        file.createNewFile();
        log.info("createFile: " + file.getAbsolutePath());
        return file;
    }

    public File getFile(String filename) {
        try {
            return IoUtil.loadFile(filename);
        } catch (Exception ex) {
            log.error("failed to load file: {}", filename);
            return null;
        }
    }

    public InputStream getResourceAsStream(String path) throws IOException {
        File file = getFile(path);
        if (file != null) {
            return new FileInputStream(file);
        }
        return null;
    }

    public void deleteFile(String filename) {
        File file = null;
        if (filename.charAt(0) == '/') {
            filename = filename.substring(1);
        }
        if (documentBase == null) {
            file = new File(filename);
        } else {
            file = new File(cacheBase, filename);
        }
        try {
            if (file.exists()) {
                file.delete();
                log.info("删除文件：" + filename);
            }
        } catch (Exception e) {
            log.info("删除文件失败：" + filename);
            e.printStackTrace();
        }
    }
}
