package lib.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class FastScanner2 {
    private static final int BUFFER_SIZE = 1 << 13;

    private final InputStream in;

    private final byte[] buf = new byte[BUFFER_SIZE];
    private int bufptr = 0;
    private int buflen = 0;

    public FastScanner2(InputStream in) {
        Objects.requireNonNull(in);
        this.in = in;
    }

    public FastScanner2() {
        this(System.in);
    }

    private boolean hasNextByte() {
        if (bufptr < buflen) return true;
        bufptr = 0;
        try {
            buflen = in.read(buf);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return buflen > 0;
    }

    private int nextByte() {
        if (hasNextByte()) {
            return buf[bufptr++];
        } else {
            throw new java.util.NoSuchElementException();
        }
    }

    public char nextChar() {
        return (char) skipUnprintableCharacters();
    }

    public String nextLine() {
        StringBuilder sb = new StringBuilder();
        for (int b = nextByte(); b != '\n'; b = nextByte()) {
            sb.appendCodePoint(b);
        }
        return sb.toString();
    }

    public String next() {
        StringBuilder sb = new StringBuilder();
        for (int b = skipUnprintableCharacters(); isPrintableAsciiCharacter(b); b = nextByte()) {
            sb.appendCodePoint(b);
        }
        return sb.toString();
    }

    public String[] next(int size) {
        String[] tokens = new String[size];
        java.util.Arrays.setAll(tokens, i -> next());
        return tokens;
    }

    public long nextLong() {
        boolean isNegetive = false;
        long n = 0;
        int b = skipUnprintableCharacters();
        if (b == '-') {
            isNegetive = true;
            b = nextByte();
        }
        while ('0' <= b && b <= '9') {
            n = n * 10 + b - '0';
            b = nextByte();
        }
        return isNegetive ? -n : n;
    }

    public long nextLongStrict() {
        boolean isNegetive = false;
        long n = 0;
        int b = skipUnprintableCharacters();
        if (b == '-') {
            isNegetive = true;
            b = nextByte();
        }
        if (!isDigit(b)) {
            throw new NumberFormatException();
        }
        if (b == '0') {
            b = nextByte();
            if (isDigit(b)) {
                throw new NumberFormatException();
            } else if (!isPrintableAsciiCharacter(b)) {
                return 0;
            } else {
                throw new NumberFormatException();
            }
        }
        while (isDigit(b)) {
            // long : -9223372036854775808 ~ 9223372036854775807
            if (n < 922337203685477580L) {
                n = n * 10 + b - '0';
            } else if (n == 922337203685477580L) {
                if (isNegetive) {
                    if (b > '8') {
                        throw new NumberFormatException("big integer");
                    } else {
                        n = -n * 10 - (b - '0');
                    }
                } else {
                    if (b > '7') {
                        throw new NumberFormatException("big integer");
                    } else {
                        n = n * 10 + b - '0';
                    }
                }
                b = nextByte();
                if (isDigit(b)) {
                    throw new NumberFormatException("big integer");
                } else if (!isPrintableAsciiCharacter(b)) {
                    return n;
                } else {
                    throw new NumberFormatException();
                }
            } else {
                throw new NumberFormatException("big integer");
            }
            b = nextByte();
        }
        if (isPrintableAsciiCharacter(b)) {
            throw new NumberFormatException();
        }
        return isNegetive ? -n : n;
    }

    public long[] nextLongs(int size) {
        long[] longs = new long[size];
        java.util.Arrays.setAll(longs, i -> nextLong());
        return longs;
    }

    public int nextInt() {
        return Math.toIntExact(nextLong());
    }

    public int[] nextInts(int size) {
        int[] ints = new int[size];
        java.util.Arrays.setAll(ints, i -> nextInt());
        return ints;
    }

    public double nextDouble() {
        StringBuilder sb = new StringBuilder();
        boolean isNegetive = false;
        int b = skipUnprintableCharacters();
        if (b == '-') {
            isNegetive = true;
            b = nextByte();
        }
        if (b == '0') {
            sb.appendCodePoint(b);
            b = nextByte();
            if (isDigit(b)) {
                throw new NumberFormatException();
            }
        } else if (isDigit(b)) {
            do {
                sb.appendCodePoint(b);
                b = nextByte();
            } while (isDigit(b));
        } else {
            throw new NumberFormatException();
        }
        boolean point = false;
        if (b == '.') {
            point = true;
            do {
                sb.appendCodePoint(b);
                b = nextByte();
            } while (isDigit(b));
        }
        if (isPrintableAsciiCharacter(b)) {
            throw new NumberFormatException();
        }
        int i = sb.length() - 1;
        double d = 0;
        if (point) {
            while (true) {
                char c = sb.charAt(i--);
                if (c == '.') break;
                d = (d + c - '0') / 10.;
            }
        }
        double pow = 1.;
        for (int j = i; j >= 0; j--) {
            d += pow * (sb.charAt(j) - '0');
            pow *= 10;
        }
        return isNegetive ? -d : d;
    }

    public double[] nextDoubles(int size) {
        double[] doubles = new double[size];
        java.util.Arrays.setAll(doubles, i -> nextDouble());
        return doubles;
    }

    private static boolean isDigit(int codePoint) {
        return '0' <= codePoint && codePoint <= '9';
    }

    private static boolean isPrintableAsciiCharacter(int codePoint) {
        return 32 < codePoint && codePoint < 127;
    }

    private int skipUnprintableCharacters() {
        int b = nextByte();
        while (!isPrintableAsciiCharacter(b)) {
            b = nextByte();
        }
        return b;
    }

    public void close() {
        try {
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
