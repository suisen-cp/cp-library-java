package lib.io;

/**
 * @author https://atcoder.jp/users/suisen
 */
public class ByteBuffer {
    private static final int DEFAULT_BUF_SIZE = 1024;
    private byte[] buf;
    private int ptr = 0;
    public ByteBuffer(int capacity) {
        this.buf = new byte[capacity];
    }
    public ByteBuffer() {
        this(DEFAULT_BUF_SIZE);
    }
    public ByteBuffer append(byte b) {
        ensureCapacity(ptr + 1);
        buf[ptr++] = b;
        return this;
    }
    public ByteBuffer append(byte[] s) {
        return append(s, 0, s.length);
    }
    public ByteBuffer append(byte[] s, int begin, int end) {
        int l = end - begin;
        ensureCapacity(ptr + l);
        System.arraycopy(s, begin, buf, ptr, l);
        ptr += l;
        return this;
    }
    public int size() {
        return ptr;
    }
    public void clear() {
        ptr = 0;
    }
    public byte[] getRawBuffer() {
        return buf;
    }
    private void ensureCapacity(int cap) {
        if (cap >= buf.length) {
            int newLength = buf.length;
            while (newLength < cap) newLength <<= 1;
            byte[] newBuf = new byte[newLength];
            System.arraycopy(buf, 0, newBuf, 0, buf.length);
            buf = newBuf;
        }
    }
}