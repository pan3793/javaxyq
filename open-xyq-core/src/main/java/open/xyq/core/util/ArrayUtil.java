package open.xyq.core.util;

import java.util.Objects;

public class ArrayUtil {

    public static boolean contains(String[] array, String value) {
        for (String s : array)
            if (Objects.equals(s, value)) return true;
        return false;
    }
}
