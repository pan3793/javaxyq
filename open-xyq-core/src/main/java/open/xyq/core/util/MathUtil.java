package open.xyq.core.util;

import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class MathUtil {
    private static final MessageDigest MD5 = getMD5();

    @SneakyThrows
    private static MessageDigest getMD5() {
        return MessageDigest.getInstance("MD5");
    }

    public static String md5(String str) {
        byte[] md5Bytes = MD5.digest(str.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexSb = new StringBuilder();
        for (byte b : md5Bytes) {
            hexSb.append(String.format("%02x", b & 0xFF));
        }
        return hexSb.toString();
    }
}