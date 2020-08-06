package open.xyq.core.util;

import javax.annotation.Nullable;

public class StrUtil {

    public static final String EMPTY = "";

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

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static String substringAfterLast(String str, String separator) {
        if (isEmpty(str)) {
            return str;
        }
        if (isEmpty(separator)) {
            return EMPTY;
        }
        int pos = str.lastIndexOf(separator);
        if (pos == -1 || pos == (str.length() - separator.length())) {
            return EMPTY;
        }
        return str.substring(pos + separator.length());
    }

    /**
     *  Check if pattern string matches text string.
     *    At the beginning of iteration i of main loop
     *      old[j] = true if pattern[0..j] matches text[0..i-1]
     *    By comparing pattern[j] with text[i], the main loop computes
     *      states[j] = true if pattern[0..j] matches text[0..i]
     */
    public static boolean wildcardMatches(String pattern, String text) {
        // add sentinel so don't need to worry about *'s at end of pattern
        text += '\0';
        pattern += '\0';

        int N = pattern.length();

        boolean[] states = new boolean[N + 1];
        boolean[] old = new boolean[N + 1];
        old[0] = true;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            states = new boolean[N + 1];       // initialized to false
            for (int j = 0; j < N; j++) {
                char p = pattern.charAt(j);
                // hack to handle *'s that match 0 characters
                if (old[j] && (p == '*')) old[j + 1] = true;
                if (old[j] && (p == c)) states[j + 1] = true;
                if (old[j] && (p == '?')) states[j + 1] = true;
                if (old[j] && (p == '*')) states[j] = true;
                if (old[j] && (p == '*')) states[j + 1] = true;
            }
            old = states;
        }
        return states[N];
    }
}
