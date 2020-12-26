package lib.datastructure.longs;

@SuppressWarnings("PointlessBitwiseExpression")
public final class LongRangeAffineRangeSum {

    private final long[] dat, lzA, lzB;
    private final int n;
    private final int log;

    public LongRangeAffineRangeSum(int n) {
        int k = 1; while (k < n) k <<= 1;
        this.dat = new long[k << 1];
        this.lzA = new long[k];
        this.lzB = new long[k];
        this.n = k;
        this.log = Integer.numberOfTrailingZeros(k);
        java.util.Arrays.fill(lzA, 1);
    }
 
    public LongRangeAffineRangeSum(long[] dat) {
        this(dat.length);
        build(dat);
    }
 
    private void build(long[] a) {
        System.arraycopy(a, 0, dat, n, a.length);
        for (int i = n - 1; i > 0; i--) {
            dat[i] = dat[i << 1 | 0] + dat[i << 1 | 1];
        }
    }
 
    private void push(int k, int h) {
        if (lzA[k] == 1 && lzB[k] == 0) return;
        int lk = k << 1 | 0, rk = k << 1 | 1;
        dat[lk] = lzA[k] * dat[lk] + (lzB[k] << h - 1);
        dat[rk] = lzA[k] * dat[rk] + (lzB[k] << h - 1);
        if (lk < n) {
            lzA[lk] = lzA[k] * lzA[lk];
            lzB[lk] = lzA[k] * lzB[lk] + lzB[k];
        }
        if (rk < n) {
            lzA[rk] = lzA[k] * lzA[rk];
            lzB[rk] = lzA[k] * lzB[rk] + lzB[k];
        }
        lzA[k] = 1;
        lzB[k] = 0;
    }
 
    private void pushTo(int k) {
        for (int i = log; i > 0; i--) push(k >> i, i);
    }
 
    private void pushTo(int lk, int rk) {
        for (int i = log; i > 0; i--) {
            if (((lk >> i) << i) != lk) push(lk >> i, i);
            if (((rk >> i) << i) != rk) push(rk >> i, i);
        }
    }
 
    private void updateFrom(int k) {
        k >>= 1;
        while (k > 0) {
            dat[k] = dat[k << 1 | 0] + dat[k << 1 | 1];
            k >>= 1;
        }
    }
 
    private void updateFrom(int lk, int rk) {
        for (int i = 1; i <= log; i++) {
            if (((lk >> i) << i) != lk) {
                int lki = lk >> i;
                dat[lki] = dat[lki << 1 | 0] + dat[lki << 1 | 1];
            }
            if (((rk >> i) << i) != rk) {
                int rki = (rk - 1) >> i;
                dat[rki] = dat[rki << 1 | 0] + dat[rki << 1 | 1];
            }
        }
    }
 
    public long get(int i) {
        pushTo(i + n);
        return dat[i + n];
    }
 
    public long sum(int l, int r) {
        if (l == r) return 0;
        l += n; r += n;
        pushTo(l, r);
        long sum = 0;
        while (l < r) {
            if ((l & 1) == 1) sum += dat[l++];
            if ((r & 1) == 1) sum += dat[--r];
            l >>= 1; r >>= 1;
        }
        return sum;
    }
 
    public long sum() {
        return dat[1];
    }
 
    public void affine(int i, long a, long b) {
        pushTo(i + n);
        dat[i + n] = a * dat[i + n] + b;
        updateFrom(i + n);
    }
 
    public void affine(int l, int r, long a, long b) {
        if (l == r) return;
        l += n; r += n;
        pushTo(l, r);
        for (int l2 = l, r2 = r, w = 1; l2 < r2; w <<= 1) {
            if ((l2 & 1) == 1) {
                dat[l2] = a * dat[l2] + b * w;
                if (l2 < n) {
                    lzA[l2] = a * lzA[l2];
                    lzB[l2] = a * lzB[l2] + b;
                }
                l2++;
            }
            if ((r2 & 1) == 1) {
                r2--;
                dat[r2] = a * dat[r2] + b * w;
                if (r2 < n) {
                    lzA[r2] = a * lzA[r2];
                    lzB[r2] = a * lzB[r2] + b;
                }
            }
            l2 >>= 1; r2 >>= 1;
        }
        updateFrom(l, r);
    }
}