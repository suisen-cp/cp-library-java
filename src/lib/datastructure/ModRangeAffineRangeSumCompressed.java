package lib.datastructure;

import java.util.Arrays;

import lib.math.number.ModArithmetic;

public class ModRangeAffineRangeSumCompressed {
    private final ModArithmetic ma;
    private final long[] dat, lzA, lzB, w;
    private final int n;
    private final int log;

    public ModRangeAffineRangeSumCompressed(int n, ModArithmetic ma, long[] w) {
        this.ma = ma;
        int k = 1; while (k < n) k <<= 1;
        this.n = k;
        this.log = Integer.numberOfTrailingZeros(k);
        this.dat = new long[k << 1];
        this.lzA = new long[k];
        this.lzB = new long[k];
        this.w = new long[k << 1];

        Arrays.fill(lzA, 1);
        for (int i = 0; i < w.length; i++) {
            this.w[k + i] = ma.mod(w[i]);
        }
        for (int i = k - 1; i > 0; i--) {
            this.w[i] = ma.add(this.w[i << 1 | 0], this.w[i << 1 | 1]);
        }
    }

    public void clear() {
        Arrays.fill(dat, 0);
        Arrays.fill(lzA, 1);
        Arrays.fill(lzB, 0);
    }

    public void build(long[] dat) {
        clear();
        System.arraycopy(dat, 0, this.dat, this.n, dat.length);
        for (int i = n - 1; i > 0; i--) update(i);
    }
 
    private void push(int k) {
        if (lzA[k] == 1 && lzB[k] == 0) return;
        int lk = k << 1 | 0, rk = k << 1 | 1;
        dat[lk] = ma.mod(lzA[k] * dat[lk] + lzB[k] * w[lk]);
        dat[rk] = ma.mod(lzA[k] * dat[rk] + lzB[k] * w[rk]);
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
 
    private void pushTo(int k) { for (int i = log; i > 0; i--) push(k >> i); }
    private void pushTo(int lk, int rk) {
        for (int i = log; i > 0; i--) {
            if (((lk >> i) << i) != lk) push(lk >> i);
            if (((rk >> i) << i) != rk) push(rk >> i);
        }
    }

    private void update(int k) {
        dat[k] = ma.add(dat[k << 1 | 0], dat[k << 1 | 1]);
    }
 
    private void updateFrom(int k) { while (k > 1) update(k >>= 1); }
    private void updateFrom(int lk, int rk) {
        for (int i = 1; i <= log; i++) {
            if (((lk >> i) << i) != lk) update(lk >> i);
            if (((rk >> i) << i) != rk) update(rk - 1 >> i);
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
    public long sumRight(int l) { return sum(l, n); }
    public long sumLeft(int r) { return sum(0, r); }
    public long sum() { return dat[1]; }
 
    public void affine(int i, long a, long b) {
        a = ma.mod(a);
        b = ma.mod(b);
        pushTo(i + n);
        dat[i + n] = ma.mod(a * dat[i + n] + b * w[i + n]);
        updateFrom(i + n);
    }
 
    public void affine(int l, int r, long a, long b) {
        if (l == r) return;
        a = ma.mod(a);
        b = ma.mod(b);
        l += n; r += n;
        pushTo(l, r);
        for (int l2 = l, r2 = r; l2 < r2;) {
            if ((l2 & 1) == 1) {
                dat[l2] = ma.mod(a * dat[l2] + b * w[l2]);
                if (l2 < n) {
                    lzA[l2] = ma.mod(a * lzA[l2]);
                    lzB[l2] = ma.mod(a * lzB[l2] + b);
                }
                l2++;
            }
            if ((r2 & 1) == 1) {
                r2--;
                dat[r2] = ma.mod(a * dat[r2] + b * w[r2]);
                if (r2 < n) {
                    lzA[r2] = ma.mod(a * lzA[r2]);
                    lzB[r2] = ma.mod(a * lzB[r2] + b);
                }
            }
            l2 >>= 1; r2 >>= 1;
        }
        updateFrom(l, r);
    }
    public void affineRight(int l, long a, long b) { affine(l, n, a, b); }
    public void affineLeft(int r, long a, long b) { affine(0, r, a, b); }
    public void affine(long a, long b) { affine(0, n, a, b); }
}
