package lib.io;

/**
 * @author https://atcoder.jp/users/suisen
 */
public class FastPrintStream implements AutoCloseable {
    private static final int INT_MAX_LEN = 11;
    private static final int LONG_MAX_LEN = 20;

    private int precision = 9;

    private static final int BUF_SIZE = 1 << 14;
    private static final int BUF_SIZE_MINUS_INT_MAX_LEN = BUF_SIZE - INT_MAX_LEN;
    private static final int BUF_SIZE_MINUS_LONG_MAX_LEN = BUF_SIZE - LONG_MAX_LEN;
    private final byte[] buf = new byte[BUF_SIZE];
    private int ptr = 0;
    private final java.lang.reflect.Field strField;
    private final java.nio.charset.CharsetEncoder encoder;

    private final java.io.OutputStream out;

    public FastPrintStream(java.io.OutputStream out) {
        this.out = out;
        java.lang.reflect.Field f;
        try {
            f = java.lang.String.class.getDeclaredField("value");
            f.setAccessible(true);
        } catch (NoSuchFieldException | SecurityException e) {
            f = null;
        }
        this.strField = f;
        this.encoder = java.nio.charset.StandardCharsets.US_ASCII.newEncoder();
    }

    public FastPrintStream(java.io.File file) throws java.io.IOException {
        this(new java.io.FileOutputStream(file));
    }

    public FastPrintStream(java.lang.String filename) throws java.io.IOException {
        this(new java.io.File(filename));
    }

    public FastPrintStream() {
        this(new java.io.FileOutputStream(java.io.FileDescriptor.out));
    }

    public FastPrintStream println() {
        if (ptr == BUF_SIZE) internalFlush();
        buf[ptr++] = (byte) '\n';
        return this;
    }

    public FastPrintStream println(java.lang.Object o) {
        return print(o).println();
    }

    public FastPrintStream println(java.lang.String s) {
        return print(s).println();
    }

    public FastPrintStream println(char[] s) {
        return print(s).println();
    }

    public FastPrintStream println(char c) {
        return print(c).println();
    }

    public FastPrintStream println(int x) {
        return print(x).println();
    }

    public FastPrintStream println(long x) {
        return print(x).println();
    }

    public FastPrintStream println(double d, int precision) {
        return print(d, precision).println();
    }

    public FastPrintStream println(double d) {
        return print(d).println();
    }

    private FastPrintStream print(byte[] bytes) {
        int n = bytes.length;
        if (ptr + n > BUF_SIZE) {
            internalFlush();
            try {
                out.write(bytes);
            } catch (java.io.IOException e) {
                throw new java.io.UncheckedIOException(e);
            }
        } else {
            System.arraycopy(bytes, 0, buf, ptr, n);
            ptr += n;
        }
        return this;
    }

    public FastPrintStream print(java.lang.Object o) {
        return print(o.toString());
    }

    public FastPrintStream print(java.lang.String s) {
        if (strField == null) {
            return print(s.getBytes());
        } else {
            try {
                Object value = strField.get(s);
                if (value instanceof byte[]) {
                    return print((byte[]) value);
                } else {
                    return print((char[]) value);
                }
            } catch (IllegalAccessException e) {
                return print(s.getBytes());
            }
        }
    }

    public FastPrintStream print(char[] s) {
        try {
            return print(encoder.encode(java.nio.CharBuffer.wrap(s)).array());
        } catch (java.nio.charset.CharacterCodingException e) {
            byte[] bytes = new byte[s.length];
            for (int i = 0; i < s.length; i++) {
                bytes[i] = (byte) s[i];
            }
            return print(bytes);
        }
    }

    public FastPrintStream print(char c) {
        if (ptr == BUF_SIZE) internalFlush();
        buf[ptr++] = (byte) c;
        return this;
    }

    public FastPrintStream print(int x) {
        if (ptr > BUF_SIZE_MINUS_INT_MAX_LEN) internalFlush();
        if (-10 < x && x < 10) {
            if (x < 0) {
                buf[ptr++] = '-';
                x = -x;
            }
            buf[ptr++] = (byte) ('0' + x);
            return this;
        }
        int d;
        if (x < 0) {
            if (x == Integer.MIN_VALUE) {
                buf[ptr++] = '-'; buf[ptr++] = '2'; buf[ptr++] = '1'; buf[ptr++] = '4';
                buf[ptr++] = '7'; buf[ptr++] = '4'; buf[ptr++] = '8'; buf[ptr++] = '3';
                buf[ptr++] = '6'; buf[ptr++] = '4'; buf[ptr++] = '8';
                return this;
            }
            d = len(x = -x);
            buf[ptr++] = '-';
        } else {
            d = len(x);
        }
        int j = ptr += d; 
        while (x > 0) {
            buf[--j] = (byte) ('0' + (x % 10));
            x /= 10;
        }
        return this;
    }

    public FastPrintStream print(long x) {
        if ((int) x == x) return print((int) x);
        if (ptr > BUF_SIZE_MINUS_LONG_MAX_LEN) internalFlush();
        int d;
        if (x < 0) {
            if (x == Long.MIN_VALUE) {
                buf[ptr++] = '-'; buf[ptr++] = '9'; buf[ptr++] = '2'; buf[ptr++] = '2';
                buf[ptr++] = '3'; buf[ptr++] = '3'; buf[ptr++] = '7'; buf[ptr++] = '2';
                buf[ptr++] = '0'; buf[ptr++] = '3'; buf[ptr++] = '6'; buf[ptr++] = '8';
                buf[ptr++] = '5'; buf[ptr++] = '4'; buf[ptr++] = '7'; buf[ptr++] = '7';
                buf[ptr++] = '5'; buf[ptr++] = '8'; buf[ptr++] = '0'; buf[ptr++] = '8';
                return this;
            }
            d = len(x = -x);
            buf[ptr++] = '-';
        } else {
            d = len(x);
        }
        int j = ptr += d; 
        while (x > 0) {
            buf[--j] = (byte) ('0' + (x % 10));
            x /= 10;
        }
        return this;
    }

    public FastPrintStream print(double d, int precision) {
        if (d < 0) {
            print('-');
            d = -d;
        }
        d += Math.pow(10, -precision) / 2;
        print((long) d).print('.');
        d -= (long) d;
        for(int i = 0; i < precision; i++){
            d *= 10;
            print((int) d);
            d -= (int) d;
        }
        return this;
    }

    public FastPrintStream print(double d) {
        return print(d, precision);
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    private void internalFlush() {
        try {
            out.write(buf, 0, ptr);
            ptr = 0;
        } catch (java.io.IOException e) {
            throw new java.io.UncheckedIOException(e);
        }
    }

    public void flush() {
        try {
            out.write(buf, 0, ptr);
            out.flush();
            ptr = 0;
        } catch (java.io.IOException e) {
            throw new java.io.UncheckedIOException(e);
        }
    }

    public void close() {
        try {
            out.close();
        } catch (java.io.IOException e) {
            throw new java.io.UncheckedIOException(e);
        }
    }

    private static int len(int x) {
        return
            x >= 1000000000 ? 10 :
            x >= 100000000  ?  9 :
            x >= 10000000   ?  8 :
            x >= 1000000    ?  7 :
            x >= 100000     ?  6 :
            x >= 10000      ?  5 :
            x >= 1000       ?  4 :
            x >= 100        ?  3 :
            x >= 10         ?  2 : 1;
    }

    private static int len(long x) {
        return
            x >= 1000000000000000000L ? 19 :
            x >= 100000000000000000L ? 18 :
            x >= 10000000000000000L ? 17 :
            x >= 1000000000000000L ? 16 :
            x >= 100000000000000L ? 15 :
            x >= 10000000000000L ? 14 :
            x >= 1000000000000L ? 13 :
            x >= 100000000000L ? 12 :
            x >= 10000000000L ? 11 : 10;
    }
}