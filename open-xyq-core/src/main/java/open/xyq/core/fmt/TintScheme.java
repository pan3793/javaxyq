/*
 * JavaXYQ Engine
 *
 * javaxyq@2008 all rights.
 * http://www.javaxyq.com
 */

package open.xyq.core.fmt;

/**
 * 染色方案
 */
public class TintScheme {

    private final short[][] colors = new short[3][3];

    public TintScheme(String[] schemes) {
        for (int r = 0; r < schemes.length; r++) {
            String[] colors = schemes[r].split(" ");
            for (int c = 0; c < colors.length; c++)
                this.colors[r][c] = Short.parseShort(colors[c]);
        }
    }

    /**
     * 假设用于变换调色板的参数矩阵是
     * C11 C12 C13
     * C21 C22 C23
     * C31 C32 C33
     * <p>
     * 颜色混合公式
     * R2=R*C11+G*C12+B*C13
     * G2=R*C21+G*C22+B*C23
     * B2=R*C31+G*C32+B*C33
     * R2=R2>>8
     * G2=G2>>8
     * B2=B2>>8
     */
    public byte[] mix(byte r, byte g, byte b) {
        byte[] comps = new byte[3];
        comps[0] = (byte) Math.min(r * colors[0][0] + g * colors[0][1] + b * colors[0][2] >>> 8, 0x1f);
        comps[1] = (byte) Math.min(r * colors[1][0] + g * colors[1][1] + b * colors[1][2] >>> 8, 0x3f);
        comps[2] = (byte) Math.min(r * colors[2][0] + g * colors[2][1] + b * colors[2][2] >>> 8, 0x1f);
        return comps;
    }

    public short mix(short color) {
        byte r = (byte) (color >>> 11 & 0x1F);
        byte g = (byte) (color >>> 5 & 0x3f);
        byte b = (byte) (color & 0x1F);
        byte[] rgb = this.mix(r, g, b);
        return (short) (rgb[0] << 11 | rgb[1] << 5 | rgb[2]);
    }
}
