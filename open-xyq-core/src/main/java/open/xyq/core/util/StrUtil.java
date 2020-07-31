package open.xyq.core.util;

import javax.annotation.Nullable;

public class StrUtil {

    /**
     * ref: Arrays.toString(byte[])
     */
    public static String prettyBytes(@Nullable byte[] bytes) {
        if (bytes == null)
            return "null";
        int iMax = bytes.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(String.format("%02x", bytes[i]));
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }
}
