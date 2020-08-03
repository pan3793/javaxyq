package open.xyq.core.fmt.wdf;

import lombok.Getter;
import lombok.Setter;
import open.xyq.core.fmt.FileObject;
import open.xyq.core.fmt.FileUtil;

import java.io.IOException;
import java.io.InputStream;

/**
 * 用于描述wdf文件内的文件结点对象
 *
 * @author 龚德伟
 * @history 2008-6-11 龚德伟 新建
 */
public class WdfFileNode implements FileObject {

    private static final long serialVersionUID = -307042623360452427L;

    @Getter // 文件ID
    private long id;
    @Getter
    @Setter // 文件大小
    private long size;
    @Getter
    @Setter // 文件数据偏移地址
    private int offset;
    @Getter
    @Setter // 文件剩余空间
    private int space;
    @Getter
    @Setter
    private String path;
    @Setter // 描述(注释)
    private String description;
    @Getter
    @Setter // 所在的容器(WdfFile)
    private WdfFile fileSystem;
    @Getter
    @Setter
    private FileObject parent;
    @Setter
    private String name;

    @Override
    public String getName() {
        if (name != null)
            return name;
        else if (description != null)
            return description;
        else
            return Long.toHexString(id);
    }

    @Override
    public String toString() {
        return "[id=" + Long.toHexString(id) + ", description=" + description + ", path=" + path + "]";
    }

    @Override
    public byte[] getData() throws IOException {
        return fileSystem.getNodeData(id);
    }

    @Override
    public InputStream getDataStream() throws IOException {
        return fileSystem.getNodeAsStream(id);
    }

    @Override
    public String getContentType() {
        return FileUtil.getContentType(this);
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public boolean isFile() {
        return true;
    }

    @Override
    public FileObject[] listFiles(String filter) {
        return null;
    }

    @Override
    public FileObject[] listFiles() {
        return null;
    }

    @Override
    public int compareTo(FileObject o) {
        if (this.isDirectory() && !o.isDirectory()) {
            return 1;
        } else if (!this.isDirectory() && o.isDirectory()) {
            return -1;
        }
        // FIXME 完善带数字的文件名的比较
        int len1 = this.path.length();
        int len2 = o.getPath().length();
        if (len1 != len2) {
            return len1 - len2;
        }
        return this.path.compareTo(o.getPath());
    }

    public void setId(long id) {
        this.id = id;
        if (this.description == null)
            this.description = String.valueOf(id);
    }

    public String getDescription() {
        if (description != null) {
            return description;
        } else {
            return Long.toHexString(id);
        }
    }
}
