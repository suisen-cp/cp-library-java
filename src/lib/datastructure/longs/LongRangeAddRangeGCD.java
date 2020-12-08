package lib.datastructure.longs;

@SuppressWarnings("PointlessBitwiseExpression")
public class LongRangeAddRangeGCD {
    final int m;
    final int n;
    final long[] dat;
    final LongBinaryIndexedTree bit;

    public LongRangeAddRangeGCD(long[] dat) {
        this.m = dat.length;
        int _n = 1;
        while (_n < m + 1) _n <<= 1;
        this.n = _n;
        this.dat = new long[n << 1];
        this.bit = new LongBinaryIndexedTree(build(dat));
    }
    public LongRangeAddRangeGCD(int m) {
        this.m = m;
        int _n = 1;
        while (_n < m + 1) _n <<= 1;
        this.n = _n;
        this.dat = new long[n << 1];
        this.bit = new LongBinaryIndexedTree(n);
    }
    private long[] build(long[] a) {
        int m = a.length;
        System.arraycopy(a, 0, dat, n, m);
        for (int i = 1; i <= m; i++) {
            dat[n + i] -= a[i - 1];
        }
        for (int i = n - 1; i > 0; i--) {
            dat[i] = gcd(dat[i << 1 | 0], dat[i << 1 | 1]);
        }
        return java.util.Arrays.copyOfRange(dat, n, n << 1);
    }
    private static long gcd(long a, long b) {
        if ((a = Math.abs(a)) < (b = Math.abs(b))) {long tmp = a; a = b; b = tmp;}
        if (a == 0 || b == 0) return a ^ b;
        for (long r = a % b; r != 0; a = b, b = r, r = a % b);
        return b;
    }
    public void add(int i, long v) {
        bit.add(i, v);
        dat[i += n] += v;
        for (i >>= 1; i > 0; i >>= 1) {
            dat[i] = gcd(dat[i << 1 | 0], dat[i << 1 | 1]);
        }
    }
    public void add(int l, int r, long v) {
        add(l, v); add(r, -v);
    }
    public long gcd(int l, int r) {
        if (l >= r) return 0;
        long gcd = bit.sum(0, ++l);
        for (l += n, r += n; l < r; l >>= 1, r >>= 1) {
            if ((l & 1) == 1) gcd = gcd(dat[l++], gcd);
            if ((r & 1) == 1) gcd = gcd(dat[--r], gcd);
        }
        return gcd;
    }
}
