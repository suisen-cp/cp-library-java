package lib.util;

public final class Random {
    private static final double DOUBLE_UNIT = 0x1.0p-53;
    private int x = 123456789;
    private int y = 362436069;
    private int z = 521288629;
    private int w = 88675123;
    public int nextInt() {
        int t = x ^ (x << 11);
        x = y; y = z; z = w;
        return w = (w ^ (w >> 19)) ^ (t ^ (t >> 8));
    }
    public long nextLong() {
        return ((long) (nextInt()) << 32) + nextInt();
    }
    public int nextInt(int bound) {
        return nextInt() % bound;
    }
    public boolean nextBoolean() {
        return (nextInt() & 1) == 0;
    }
    public double nextDouble() {
        return (((long) (next(26)) << 27) + next(27)) * DOUBLE_UNIT;
    }
    private int next(int bits) {
        int mask = bits == 32 ? -1 : (1 << bits) - 1;
        return nextInt() & mask;
    }
}