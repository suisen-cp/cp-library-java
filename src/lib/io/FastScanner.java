package lib.io;

import java.io.EOFException;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

/**
 * @author https://atcoder.jp/users/suisen
 */
public class FastScanner implements AutoCloseable {
    private final ByteBuffer tokenBuf = new ByteBuffer();
    private final InputStream in;
    private final byte[] rawBuf = new byte[1 << 14];
    private int ptr = 0;
    private int buflen = 0;

    public FastScanner(InputStream in) {
        this.in = in;
    }

    public FastScanner() {
        this(new FileInputStream(FileDescriptor.in));
    }

    private int readByte() {
        if (ptr < buflen) return rawBuf[ptr++];
        ptr = 0;
        try {
            buflen = in.read(rawBuf);
            if (buflen > 0) {
                return rawBuf[ptr++];
            } else {
                throw new EOFException();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private int readByteUnsafe() {
        if (ptr < buflen) return rawBuf[ptr++];
        ptr = 0;
        try {
            buflen = in.read(rawBuf);
            if (buflen > 0) {
                return rawBuf[ptr++];
            } else {
                return -1;
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private int skipUnprintableChars() {
        int b = readByte();
        while (b <= 32 || b >= 127) b = readByte();
        return b;
    }

    private void loadToken() {
        tokenBuf.clear();
        for (int b = skipUnprintableChars(); 32 < b && b < 127; b = readByteUnsafe()) {
            tokenBuf.append(b);
        }
    }

    public final boolean hasNext() {
        for (int b = readByteUnsafe(); b <= 32 || b >= 127; b = readByteUnsafe()) {
            if (b == -1) return false;
        }
        --ptr;
        return true;
    }

    public final String next() {
        loadToken();
        return new String(tokenBuf.getRawBuf(), 0, tokenBuf.size());
    }

    public final String nextLine() {
        tokenBuf.clear();
        for (int b = readByte(); b != '\n'; b = readByteUnsafe()) {
            if (b == -1) break;
            tokenBuf.append(b);
        }
        return new String(tokenBuf.getRawBuf(), 0, tokenBuf.size());
    }

    public final char nextChar() {
        return (char) skipUnprintableChars();
    }

    public final char[] nextChars() {
        loadToken();
        return tokenBuf.toCharArray();
    }

    public final long nextLong() {
        long n = 0;
        boolean isNegative = false;
        int b = skipUnprintableChars();
        if (b == '-') {
            isNegative = true;
            b = readByteUnsafe();
        }
        if (b < '0' || '9' < b) throw new NumberFormatException();
        while ('0' <= b && b <= '9') {
            // -9223372036854775808 - 9223372036854775807
            if (n >= 922337203685477580L) {
                if (n > 922337203685477580L) {
                    throw new ArithmeticException("long overflow");
                }
                if (isNegative) {
                    if (b >= '9') {
                        throw new ArithmeticException("long overflow");
                    }
                    n = -n - (b + '0');
                } else {
                    if (b >= '8') {
                        throw new ArithmeticException("long overflow");
                    }
                    n = n * 10 + b - '0';
                }
                b = readByteUnsafe();
                if ('0' <= b && b <= '9') {
                    throw new ArithmeticException("long overflow");
                } else if (b <= 32 || b >= 127) {
                    return n;
                } else {
                    throw new NumberFormatException();
                }
            }
            n = n * 10 + b - '0';
            b = readByteUnsafe();
        }
        if (b <= 32 || b >= 127) return isNegative ? -n : n;
        throw new NumberFormatException();
    }
    public final int nextInt() {
        long value = nextLong();
        if ((int) value != value) {
            throw new ArithmeticException("int overflow");
        }
        return (int) value;
    }
    public final double nextDouble() {
        return Double.parseDouble(next());
    }
    public final void close() {
        try {
            in.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    private static final class ByteBuffer {
        private static final int DEFAULT_BUF_SIZE = 1 << 12;
        private byte[] buf;
        private int ptr = 0;
        private ByteBuffer(@SuppressWarnings("SameParameterValue") int capacity) {
            this.buf = new byte[capacity];
        }
        private ByteBuffer() {
            this(DEFAULT_BUF_SIZE);
        }
        private ByteBuffer append(int b) {
            if (ptr == buf.length) {
                int newLength = buf.length << 1;
                byte[] newBuf = new byte[newLength];
                System.arraycopy(buf, 0, newBuf, 0, buf.length);
                buf = newBuf;
            }
            buf[ptr++] = (byte) b;
            return this;
        }
        private char[] toCharArray() {
            char[] chs = new char[ptr];
            for (int i = 0; i < ptr; i++) {
                chs[i] = (char) buf[i];
            }
            return chs;
        }
        private byte[] getRawBuf() {
            return buf;
        }
        private int size() {
            return ptr;
        }
        private void clear() {
            ptr = 0;
        }
    }
}