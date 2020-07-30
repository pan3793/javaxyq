package open.xyq.core.io;

import java.io.*;

public class RichRandomAccessFile extends RandomAccessFile {

    public RichRandomAccessFile(String name, String mode) throws FileNotFoundException {
        super(name, mode);
    }

    public RichRandomAccessFile(File file, String mode) throws FileNotFoundException {
        super(file, mode);
    }

    public int readInt2() throws IOException {
        int ch1 = this.read();
        int ch2 = this.read();
        int ch3 = this.read();
        int ch4 = this.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new EOFException();
        return ((ch1) + (ch2 << 8) + (ch3 << 16) + (ch4 << 24));
    }
}