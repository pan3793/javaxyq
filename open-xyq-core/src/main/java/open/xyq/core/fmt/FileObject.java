/*
 * JavaXYQ Engine
 *
 * javaxyq@2008 all rights.
 * http://www.javaxyq.com
 */

package open.xyq.core.fmt;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

/**
 * @author 龚德伟
 * @history 2008-7-6 龚德伟
 */
public interface FileObject extends Comparable<FileObject>, Serializable {
    String BMP_FILE = "bmp";
    String MIDI_FILE = "midi";
    String GIF_FILE = "gif";
    String TGA_RLE_FILE = "tga(rle)";
    String TGA_FILE = "tga";
    String PNG_FILE = "png";
    String MP3_FILE = "mp3";
    String WAV_FILE = "wav";
    String JPG_FILE = "jpg";
    String TCP_FILE = "tca/tcp";
    String WDF_FILE = "wdf";
    String UNKNOWN_FILE = "unknown";
    String DIRECTORY = "directory";
    String MAP_FILE = "map";

    boolean isDirectory();

    boolean isFile();

    FileObject[] listFiles(String filter);

    FileObject[] listFiles();

    String getName();

    String getPath();

    FileObject getParent();

    byte[] getData() throws IOException;

    InputStream getDataStream() throws IOException;

    String getContentType();

    FileSystem getFileSystem();

    long getSize();
}
