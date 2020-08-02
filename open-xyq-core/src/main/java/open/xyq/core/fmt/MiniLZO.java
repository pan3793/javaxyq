/* LZOConstants.java -- various constants (Original file)
   This file is part of the LZO real-time data compression library.
   Copyright (C) 1999 Markus Franz Xaver Johannes Oberhumer
   Copyright (C) 1998 Markus Franz Xaver Johannes Oberhumer
   Copyright (C) 1997 Markus Franz Xaver Johannes Oberhumer
   Copyright (C) 1996 Markus Franz Xaver Johannes Oberhumer
   The LZO library is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public License as
   published by the Free Software Foundation; either version 2 of
   the License, or (at your option) any later version.
   The LZO library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
   You should have received a copy of the GNU General Public License
   along with the LZO library; see the file COPYING.
   If not, write to the Free Software Foundation, Inc.,
   59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
   Markus F.X.J. Oberhumer
   <markus.oberhumer@jk.uni-linz.ac.at>
   http://wildsau.idv.uni-linz.ac.at/mfx/lzo.html

   Java Porting of minilzo.c (2.03) by
   Copyright (C) 2010 Mahadevan Gorti Surya Srinivasa <sgorti@gmail.com>
 */

package open.xyq.core.fmt;


/**
 * Java ported code of minilzo.c(2.03).
 * <p>
 * All compress/decompress/decompress_safe were ported. Original java version of MiniLZO supported
 * only decompression via Lzo1xDecompressor.java and Lzo1xDecompressor ported way back in 1999.
 * <p>
 * This new MiniLZO.java based was taken from minilzo.c version 2.03.
 *
 * @author mahadevan.gss
 */
public final class MiniLZO {
    public static final int c_top_loop = 1;
    public static final int c_first_literal_run = 2;
    public static final int c_match = 3;
    public static final int c_copy_match = 4;
    public static final int c_match_done = 5;
    public static final int c_match_next = 6;

    /**
     * Decompress the data. Error codes would be returned (@see LZOConstants).
     * Decompressed length is returned via out_len.
     *
     * @param in  Input byte array to be decompressed
     * @param out decompressed output byte array. Ensure that out array has sufficient length
     */
    public static int lzo1x_decompress(final byte[] in, final byte[] out) {
        int op = 0;
        int ip = 0;
        int t;
        int state = c_top_loop;
        int max = 0, diff, min = 0;
        int m_pos = 0;

        t = in[ip] & 0xff;
        if (t > 17) {
            ip++;
            t -= 17;
            if (t < 4) {
                state = c_match_next; //goto match_next;
            } else {
                do out[op++] = in[ip++]; while (--t > 0);
                state = c_first_literal_run;//goto first_literal_run;
            }
        }
        top_loop_ori:
        do {
            boolean if_block = false;
            switch (state) {
                //while (true)  top_loop_ori
                case c_top_loop:
                    t = in[ip++] & 0xff;
                    if (t >= 16) {
                        state = c_match;
                        continue top_loop_ori; //goto match;
                    }
                    if (t == 0) {
                        while (in[ip] == 0) {
                            t += 255;
                            ip++;
                        }
                        t += 15 + (in[ip++] & 0xff);
                    }

                    //s=3; do out[op++] = in[ip++]; while(--s > 0);//* (lzo_uint32 *)(op) = * (const lzo_uint32 *)(ip);op += 4; ip += 4;
                    out[op] = in[ip];
                    out[op + 1] = in[ip + 1];
                    out[op + 2] = in[ip + 2];
                    out[op + 3] = in[ip + 3];
                    op += 4;
                    ip += 4;
                    //op++; ip++; //GSSM ?? for the forth byte

                    if (--t > 0) {
                        if (t >= 4) {
                            do {
                                //* (lzo_uint32 *)(op) = * (const lzo_uint32 *)(ip);
                                //op += 4; ip += 4; t -= 4;
                                out[op] = in[ip];
                                out[op + 1] = in[ip + 1];
                                out[op + 2] = in[ip + 2];
                                out[op + 3] = in[ip + 3];
                                op += 4;
                                ip += 4;
                                t -= 4;
                            } while (t >= 4);
                            if (t > 0) do out[op++] = in[ip++]; while (--t > 0);
                        } else
                            do out[op++] = in[ip++]; while (--t > 0);
                    }
                case c_first_literal_run: /*first_literal_run: */
                    t = in[ip++] & 0xff;
                    if (t >= 16) {
                        state = c_match;
                        continue top_loop_ori;  //goto match;
                    }
                    //m_pos = op - (1 + 0x0800);
                    //m_pos -= t >> 2;
                    //m_pos -= U(in[ip++]) << 2;
                    m_pos = op - 0x801 - (t >> 2) - ((in[ip++] & 0xff) << 2);
                    diff = Math.abs(m_pos - op);
                    if (diff > max) max = diff;
                    diff = m_pos - op;
                    if (diff < min) min = diff;
                    //*op++ = *m_pos++; *op++ = *m_pos++; *op++ = *m_pos;
                    out[op++] = out[m_pos++];
                    out[op++] = out[m_pos++];
                    out[op++] = out[m_pos];

                    state = c_match_done;
                    continue top_loop_ori;//goto match_done;
                case c_match:
                    //do {
                    //match:
                    if (t >= 64) {
                        m_pos = op - 1;
                        m_pos -= t >> 2 & 7;
                        m_pos -= (in[ip++] & 0xff) << 3;
                        diff = Math.abs(m_pos - op);
                        if (diff > max) max = diff;
                        diff = m_pos - op;
                        if (diff < min) min = diff;
                        t = (t >> 5) - 1;
                        state = c_copy_match;
                        continue top_loop_ori;//goto copy_match;

                    } else if (t >= 32) {
                        t &= 31;
                        if (t == 0) {
                            while (in[ip] == 0) {
                                t += 255;
                                ip++;
                            }
                            t += 31 + (in[ip++] & 0xff);
                        }
                        m_pos = op - 1;
                        m_pos -= (in[ip] & 0xff) + ((in[ip + 1] & 0xff) << 8) >> 2;//m_pos -= (* (const unsigned short *) ip) >> 2;
                        diff = Math.abs(m_pos - op);
                        if (diff > max) max = diff;
                        diff = m_pos - op;
                        if (diff < min) min = diff;

                        ip += 2;
                    } else if (t >= 16) {
                        m_pos = op;
                        m_pos -= (t & 8) << 11;
                        diff = Math.abs(m_pos - op);
                        if (diff > max) max = diff;
                        diff = m_pos - op;
                        if (diff < min) min = diff;

                        t &= 7;
                        if (t == 0) {
                            while (in[ip] == 0) {
                                t += 255;
                                ip++;
                            }
                            t += 7 + (in[ip++] & 0xff);
                        }
                        m_pos -= (in[ip] & 0xff) + ((in[ip + 1] & 0xff) << 8) >> 2;//m_pos -= (* (const unsigned short *) ip) >> 2;
                        diff = Math.abs(m_pos - op);
                        if (diff > max) max = diff;
                        diff = m_pos - op;
                        if (diff < min) min = diff;
                        ip += 2;
                        if (m_pos == op) {
                            break top_loop_ori;//goto eof_found;
                        }
                        m_pos -= 0x4000;
                    } else {
                        m_pos = op - 1;
                        m_pos -= t >> 2;
                        m_pos -= (in[ip++] & 0xff) << 2;
                        diff = Math.abs(m_pos - op);
                        if (diff > max) max = diff;
                        diff = m_pos - op;
                        if (diff < min) min = diff;

                        out[op++] = out[m_pos++];
                        out[op++] = out[m_pos];//*op++ = *m_pos++; *op++ = *m_pos;
                        state = c_match_done;
                        continue top_loop_ori;//goto match_done;
                    }
                    if (t >= 2 * 4 - (3 - 1) && op - m_pos >= 4) {
                        if_block = true;
                        //* (lzo_uint32 *)(op) = * (const lzo_uint32 *)(m_pos);
                        out[op] = out[m_pos];
                        out[op + 1] = out[m_pos + 1];
                        out[op + 2] = out[m_pos + 2];
                        out[op + 3] = out[m_pos + 3];
                        op += 4;
                        m_pos += 4;
                        t -= 2;
                        do {
                            /// * (lzo_uint32 *)(op) = * (const lzo_uint32 *)(m_pos);
                            out[op] = out[m_pos];
                            out[op + 1] = out[m_pos + 1];
                            out[op + 2] = out[m_pos + 2];
                            out[op + 3] = out[m_pos + 3];
                            op += 4;
                            m_pos += 4;
                            t -= 4;
                        } while (t >= 4);
                        if (t > 0) do out[op++] = out[m_pos++]; while (--t > 0);
                    }// else
                case c_copy_match:
                    if (!if_block) {
                        //*op++ = *m_pos++; *op++ = *m_pos++;
                        out[op++] = out[m_pos++];
                        out[op++] = out[m_pos++];
                        //do *op++ = *m_pos++; while (--t > 0);
                        do out[op++] = out[m_pos++]; while (--t > 0);
                    }
                case c_match_done:
                    t = in[ip - 2] & 0xff & 3;
                    if (t == 0) {
                        state = c_top_loop;
                        continue top_loop_ori; //break;
                    }
                case c_match_next:
                    //*op++ = *ip++;
                    out[op++] = in[ip++];
                    //if (t > 1) { *op++ = *ip++; if (t > 2) { *op++ = *ip++; } }
                    if (t > 1) {
                        out[op++] = in[ip++];
                        if (t > 2) {
                            out[op++] = in[ip++];
                        }
                    }
                    t = in[ip++] & 0xff;
                    state = c_match;
                    //}// while (1);
                    //// state=c_top_loop; continue top_loop_ori;
            }
        } while (true);

        return ip == in.length ? 0 : ip < in.length ? -8 : -4;
    }
}