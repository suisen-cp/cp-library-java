package lib.algorithm.longs;

import java.util.NoSuchElementException;
import java.util.function.LongBinaryOperator;

/**
 * @author https://atcoder.jp/users/suisen
 * 
 * Sliding Window Aggregation
 */
public final class LongArraySWAG {
    private final LongBinaryOperator op;
    private final long e;
    private final int n;
    private final long[] a;
    private final long[] s;
    private int l, m, r = 0;
    public LongArraySWAG(long[] a, LongBinaryOperator op, long e) {
        this.op = op;
        this.e = e;
        this.n = a.length;
        this.a = a;
        this.s = new long[n];
    }
    public long fold() {
        long res = e;
        if (l < m) res = op.applyAsLong(res, s[l]);
        if (m < r) res = op.applyAsLong(res, s[r - 1]);
        return res;
    }
    public void proceedRight() {
        if (r == n) throw new NoSuchElementException();
        s[r] = m == r ? a[r] : op.applyAsLong(s[r - 1], a[r]);
        r++;
    }
    public void proceedRight(int dstR) {
        if (r > dstR) throw new IllegalArgumentException("Cannot proceed backward!");
        while (r < dstR) proceedRight();
    }
    public void proceedLeft() {
        if (l == r) throw new NoSuchElementException();
        if (l == m) {
            m = r;
            s[r - 1] = a[r - 1];
            for (int i = r - 2; i > l; i--) {
                s[i] = op.applyAsLong(a[i], s[i + 1]);
            }
        }
        l++;
    }
    public void proceedLeft(int dstL) {
        if (l > dstL) throw new IllegalArgumentException("Cannot proceed backward!");
        while (l < dstL) proceedLeft();
    }
    public void proceed(int dstL, int dstR) {
        proceedRight(dstR);
        proceedLeft(dstL);
    }
    public int getLeft() {
        return l;
    }
    public int getRight() {
        return r;
    }
}