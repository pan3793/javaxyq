/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package open.xyq.core.fmt;

/**
 * 
 * @author 龚德伟
 * @history 2008-7-6 龚德伟 新建
 */
public interface FileSystem {

    FileObject getRoot();

    String getType();

    /**
     * save desc
     */
    void save(String filename);

    /**
     * load desc
     */
    void load(String filename);
}
