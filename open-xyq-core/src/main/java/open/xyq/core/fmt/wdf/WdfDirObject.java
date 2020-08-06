/*
 * JavaXYQ Engine
 *
 * javaxyq@2008 all rights.
 * http://www.javaxyq.com
 */

package open.xyq.core.fmt.wdf;

import open.xyq.core.fmt.FileObject;
import open.xyq.core.fmt.FileSystem;
import open.xyq.core.util.StrUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author 龚德伟
 * @history 2008-7-6 龚德伟
 */
public class WdfDirObject implements FileObject {

    private final String path;
    private WdfFile fileSystem;
    private final String name;
    private FileObject[] subFiles;
    private final FileObject parent;

    public WdfDirObject(WdfDirObject parent, String path) {
        this.path = path;
        this.parent = parent;
        if (parent != null) {
            this.fileSystem = (WdfFile) parent.getFileSystem();
        }
        String[] paths = path.split("/");
        if (paths.length == 0) {// root
            this.name = StrUtil.substringAfterLast(fileSystem.getName(), "/");
        } else {// last part name
            this.name = paths[paths.length - 1];
        }
    }

    public WdfDirObject(WdfFile wdfFile) {
        this.fileSystem = wdfFile;
        this.path = "/";
        this.parent = null;
        this.name = StrUtil.substringAfterLast(fileSystem.getName(), "/");
    }

    public String getContentType() {
        return FileObject.DIRECTORY;
    }

    public byte[] getData() throws IOException {
        return null;
    }

    public InputStream getDataStream() throws IOException {
        return null;
    }

    public FileSystem getFileSystem() {
        return fileSystem;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public boolean isDirectory() {
        return true;
    }

    public boolean isFile() {
        return false;
    }

    @Override
    public FileObject[] listFiles(String filter) {
        //FileObject[] allfiles = listFiles();
        Collection<WdfFileNode> allfiles = fileSystem.fileNodes();
        if (filter != null && filter.trim().length() != 0 && !filter.trim().equals("*")) {
            if (filter.indexOf('*') == -1) {
                filter = "*" + filter + "*";
            }
            final String pattern = filter.toLowerCase();
            List<FileObject> result = new ArrayList<FileObject>();
            for (WdfFileNode fileObj : allfiles) {
                if (StrUtil.wildcardMatches(pattern, fileObj.getName().toLowerCase())) {
                    result.add(fileObj);
                } else {
                    String _id = Long.toHexString(fileObj.getId()).toLowerCase();
                    String _desc = fileObj.getDescription().toLowerCase();
                    if (StrUtil.wildcardMatches(pattern, _desc) || StrUtil.wildcardMatches(pattern, _id)) {
                        result.add(fileObj);
                    }
                }
            }
            FileObject[] files = new FileObject[result.size()];
            return result.toArray(files);
        } else {
            return listFiles();
        }
    }

    @Override
    public FileObject[] listFiles() {
        if (subFiles == null) {
            subFiles = listFiles0();
        }
        return subFiles;
    }

    private FileObject[] listFiles0() {
        Collection<WdfFileNode> allnodes = fileSystem.fileNodes();
        List<FileObject> files = new ArrayList<FileObject>();
        List<String> dirs = new ArrayList<String>();
        int len = path.length();
        for (WdfFileNode node : allnodes) {
            String subpath = node.getPath();
            if (subpath != null && subpath.startsWith(path)) {
                int p = subpath.indexOf('/', len);
                if (p != -1) {// directory
                    String dir = subpath.substring(0, p + 1);
                    if (!dirs.contains(dir)) {
                        dirs.add(dir);
                    }
                } else {// file
                    files.add(node);
                }
            }
        }
        // sort by name
        Collections.sort(dirs);
        Collections.sort(files);
        int dirCount = dirs.size();
        FileObject[] fileObjs = new FileObject[dirCount + files.size()];
        // copy dirs
        for (int i = 0; i < dirCount; i++) {
            fileObjs[i] = new WdfDirObject(this, dirs.get(i));
        }
        // copy files
        for (int i = 0, iSize = files.size(); i < iSize; i++) {
            fileObjs[dirCount + i] = files.get(i);
        }
        return fileObjs;
    }

    public int compareTo(FileObject o) {
        if (this.isDirectory() && !o.isDirectory()) {
            return 1;
        } else if (!this.isDirectory() && o.isDirectory()) {
            return -1;
        }
        return this.path.compareTo(o.getPath());
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public FileObject getParent() {
        return parent;
    }

    @Override
    public long getSize() {
        return 0;
    }
}
