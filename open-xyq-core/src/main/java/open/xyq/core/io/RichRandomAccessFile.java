package open.xyq.core.io;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class RichRandomAccessFile extends RandomAccessFile {

    public RichRandomAccessFile(File file, String mode) throws FileNotFoundException {
        super(file, mode);
    }

    /**
     * 以小端字节序读取 u_int32
     */
    public int readInt2() throws IOException {
        int ch1 = this.read();
        int ch2 = this.read();
        int ch3 = this.read();
        int ch4 = this.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new EOFException();
        return ((ch1) + (ch2 << 8) + (ch3 << 16) + (ch4 << 24));
    }

    public String readUtf8(int len) throws IOException {
        byte[] buf = new byte[len];
        this.read(buf);
        return new String(buf, StandardCharsets.UTF_8);
    }
}