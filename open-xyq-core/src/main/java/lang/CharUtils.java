/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lang;

/**
 * <p>Operations on char primitives and Character objects.</p>
 *
 * <p>This class tries to handle <code>null</code> input gracefully.
 * An exception will not be thrown for a <code>null</code> input.
 * Each method documents its behaviour in more detail.</p>
 * 
 * @author Stephen Colebourne
 * @since 2.1
 * @version $Id: CharUtils.java 437554 2006-08-28 06:21:41Z bayard $
 */
public class CharUtils {
    
    private static final String CHAR_STRING = 
        "\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007" +
        "\b\t\n\u000b\f\r\u000e\u000f" +
        "\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017" +
        "\u0018\u0019\u001a\u001b\u001c\u001d\u001e\u001f" +
        "\u0020\u0021\"\u0023\u0024\u0025\u0026\u0027" +
        "\u0028\u0029\u002a\u002b\u002c\u002d\u002e\u002f" +
        "\u0030\u0031\u0032\u0033\u0034\u0035\u0036\u0037" +
        "\u0038\u0039\u003a\u003b\u003c\u003d\u003e\u003f" +
        "\u0040\u0041\u0042\u0043\u0044\u0045\u0046\u0047" +
        "\u0048\u0049\u004a\u004b\u004c\u004d\u004e\u004f" +
        "\u0050\u0051\u0052\u0053\u0054\u0055\u0056\u0057" +
        "\u0058\u0059\u005a\u005b\\\u005d\u005e\u005f" +
        "\u0060\u0061\u0062\u0063\u0064\u0065\u0066\u0067" +
        "\u0068\u0069\u006a\u006b\u006c\u006d\u006e\u006f" +
        "\u0070\u0071\u0072\u0073\u0074\u0075\u0076\u0077" +
        "\u0078\u0079\u007a\u007b\u007c\u007d\u007e\u007f";
    
    private static final String[] CHAR_STRING_ARRAY = new String[128];
    private static final Character[] CHAR_ARRAY = new Character[128];
    
    /**
     * <code>\u000a</code> linefeed LF ('\n').
     * 
     * @see <a href="http://java.sun.com/docs/books/jls/third_edition/html/lexical.html#101089">JLF: Escape Sequences
     *      for Character and String Literals</a>
     * @since 2.2
     */
    public static final char LF = '\n';

    /**
     * <code>\u000d</code> carriage return CR ('\r').
     * 
     * @see <a href="http://java.sun.com/docs/books/jls/third_edition/html/lexical.html#101089">JLF: Escape Sequences
     *      for Character and String Literals</a>
     * @since 2.2
     */
    public static final char CR = '\r';
    

    static {
        for (int i = 127; i >= 0; i--) {
            CHAR_STRING_ARRAY[i] = CHAR_STRING.substring(i, i + 1);
            CHAR_ARRAY[i] = new Character((char) i);
        }
    }

    /**
     * <p><code>CharUtils</code> instances should NOT be constructed in standard programming.
     * Instead, the class should be used as <code>CharUtils.toString('c');</code>.</p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean instance
     * to operate.</p>
     */
    public CharUtils() {
      super();
    }

    
    //-----------------------------------------------------------------------
    /**
     * <p>Converts the character to a String that contains the one character.</p>
     * 
     * <p>For ASCII 7 bit characters, this uses a cache that will return the
     * same String object each time.</p>
     *
     * <pre>
     *   CharUtils.toString(' ')  = " "
     *   CharUtils.toString('A')  = "A"
     * </pre>
     *
     * @param ch  the character to convert
     * @return a String containing the one specified character
     */
    public static String toString(char ch) {
        if (ch < 128) {
            return CHAR_STRING_ARRAY[ch];
        }
        return new String(new char[] {ch});
    }
    
    /**
     * <p>Converts the character to a String that contains the one character.</p>
     * 
     * <p>For ASCII 7 bit characters, this uses a cache that will return the
     * same String object each time.</p>
     * 
     * <p>If <code>null</code> is passed in, <code>null</code> will be returned.</p>
     *
     * <pre>
     *   CharUtils.toString(null) = null
     *   CharUtils.toString(' ')  = " "
     *   CharUtils.toString('A')  = "A"
     * </pre>
     *
     * @param ch  the character to convert
     * @return a String containing the one specified character
     */
    public static String toString(Character ch) {
        if (ch == null) {
            return null;
        }
        return toString(ch.charValue());
    }
    
}
