/*
 * JavaXYQ Engine
 *
 * javaxyq@2008 all rights.
 * http://www.javaxyq.com
 */

package open.xyq.core.fmt;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Comparator;

import lombok.Getter;
import lombok.Setter;
import open.xyq.core.Wildcard;

/**
 * @author 龚德伟
 * @history 2008-7-6 龚德伟 新建
 */
public class DefaultFileObject implements FileObject {

    @Getter
    private final DefaultFileSystem fileSystem;

    private final Comparator<File> fileComparator = (o1, o2) -> {
        int result = (o1.isDirectory() ? 0 : 1) - (o2.isDirectory() ? 0 : 1);
        if (result == 0) {
            return o1.getName().compareToIgnoreCase(o2.getName());
        }
        return result;
    };

    @Getter
    @Setter
    private File file;

    public DefaultFileObject(DefaultFileSystem filesystem, String pathname) {
        this.fileSystem = filesystem;
        this.file = new File(pathname);
    }

    public DefaultFileObject(DefaultFileSystem filesystem, URI uri) {
        this.fileSystem = filesystem;
        this.file = new File(uri);
    }

    public DefaultFileObject(DefaultFileSystem filesystem, File file) {
        this.fileSystem = filesystem;
        this.file = file;
    }

    @Override
    public byte[] getData() throws IOException {
        DataInputStream is = getDataStream();
        byte[] data = new byte[(int) file.length()];
        is.readFully(data);
        return data;
    }

    @Override
    public DataInputStream getDataStream() throws FileNotFoundException {
        return new DataInputStream(new FileInputStream(file));
    }

    public String getName() {
        return file.getName();
    }

    public String getPath() {
        return file.getPath();
    }

    public String getContentType() {
        return FileUtil.getContentType(this);
    }

    public boolean isDirectory() {
        return file.isDirectory();
    }

    public boolean isFile() {
        return file.isFile();
    }

    @Override
    public FileObject[] listFiles(String filter) {
        File[] allFiles;
        if (filter != null && filter.trim().length() != 0 && !filter.trim().equals("*")) {
            if (filter.indexOf('*') == -1)
                filter = "*" + filter + "*";
            final String pattern = filter.toLowerCase();
            FilenameFilter nameFilter = (dir, name) -> Wildcard.matches(pattern, name.toLowerCase());
            allFiles = file.listFiles(nameFilter);
        } else {
            allFiles = file.listFiles();
        }
        if (allFiles == null)
            return new FileObject[0];

        Arrays.sort(allFiles, fileComparator);
        FileObject[] fileObjects = new FileObject[allFiles.length];
        for (int i = 0; i < allFiles.length; i++) {
            DefaultFileObject fileObj = new DefaultFileObject(fileSystem, allFiles[i]);
            fileObjects[i] = fileObj;
        }
        return fileObjects;
    }

    @Override
    public FileObject[] listFiles() {
        return listFiles(null);
    }

    @Override
    public int compareTo(FileObject o) {
        if (this.isDirectory() && !o.isDirectory()) {
            return 1;
        } else if (!this.isDirectory() && o.isDirectory()) {
            return -1;
        }
        return this.getPath().compareTo(o.getPath());
    }

    @Override
    public FileObject getParent() {
        return new DefaultFileObject(this.fileSystem, this.file.getParent());
    }

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public long getSize() {
        return this.file.length();
    }
}
