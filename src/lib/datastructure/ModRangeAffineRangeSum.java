package lib.datastructure;

import lib.math.number.ModArithmetic;

@SuppressWarnings("PointlessBitwiseExpression")
public final class ModRangeAffineRangeSum {
    private final ModArithmetic ma;
    private final long[] dat, lzA, lzB;
    private final int n;
    private final int log;

    public ModRangeAffineRangeSum(int n, ModArithmetic ma) {
        this.ma = ma;
        int k = 1; while (k < n) k <<= 1;
        this.dat = new long[k << 1];
        this.lzA = new long[k];
        this.lzB = new long[k];
        this.n = k;
        this.log = Integer.numberOfTrailingZeros(k);
        java.util.Arrays.fill(lzA, 1);
    }
 
    public ModRangeAffineRangeSum(long[] dat, ModArithmetic ma) {
        this(dat.length, ma);
        build(dat);
    }
 
    private void build(long[] a) {
        System.arraycopy(a, 0, dat, n, a.length);
        for (int i = n - 1; i > 0; i--) {
            dat[i] = ma.add(dat[i << 1 | 0], dat[i << 1 | 1]);
        }
    }
 
    private void push(int k, int h) {
        if (lzA[k] == 1 && lzB[k] == 0) return;
        int lk = k << 1 | 0, rk = k << 1 | 1;
        dat[lk] = ma.mod(lzA[k] * dat[lk] + (lzB[k] << h - 1));
        dat[rk] = ma.mod(lzA[k] * dat[rk] + (lzB[k] << h - 1));
        if (lk < n) {
            lzA[lk] = ma.mod(lzA[k] * lzA[lk]);
            lzB[lk] = ma.mod(lzA[k] * lzB[lk] + lzB[k]);
        }
        if (rk < n) {
            lzA[rk] = ma.mod(lzA[k] * lzA[rk]);
            lzB[rk] = ma.mod(lzA[k] * lzB[rk] + lzB[k]);
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
            dat[k] = ma.add(dat[k << 1 | 0], dat[k << 1 | 1]);
            k >>= 1;
        }
    }
 
    private void updateFrom(int lk, int rk) {
        for (int i = 1; i <= log; i++) {
            if (((lk >> i) << i) != lk) {
                int lki = lk >> i;
                dat[lki] = ma.add(dat[lki << 1 | 0], dat[lki << 1 | 1]);
            }
            if (((rk >> i) << i) != rk) {
                int rki = (rk - 1) >> i;
                dat[rki] = ma.add(dat[rki << 1 | 0], dat[rki << 1 | 1]);
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
        return ma.mod(sum);
    }
 
    public long sum() {
        return dat[1];
    }
 
    public void affine(int i, long a, long b) {
        a = ma.mod(a);
        b = ma.mod(b);
        pushTo(i + n);
        dat[i + n] = ma.mod(a * dat[i + n] + b);
        updateFrom(i + n);
    }
 
    public void affine(int l, int r, long a, long b) {
        if (l == r) return;
        a = ma.mod(a);
        b = ma.mod(b);
        l += n; r += n;
        pushTo(l, r);
        for (int l2 = l, r2 = r, w = 1; l2 < r2; w <<= 1) {
            if ((l2 & 1) == 1) {
                dat[l2] = ma.mod(a * dat[l2] + b * w);
                if (l2 < n) {
                    lzA[l2] = ma.mod(a * lzA[l2]);
                    lzB[l2] = ma.mod(a * lzB[l2] + b);
                }
                l2++;
            }
            if ((r2 & 1) == 1) {
                r2--;
                dat[r2] = ma.mod(a * dat[r2] + b * w);
                if (r2 < n) {
                    lzA[r2] = ma.mod(a * lzA[r2]);
                    lzB[r2] = ma.mod(a * lzB[r2] + b);
                }
            }
            l2 >>= 1; r2 >>= 1;
        }
        updateFrom(l, r);
    }
}