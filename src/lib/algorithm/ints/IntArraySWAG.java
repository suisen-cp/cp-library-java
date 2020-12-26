package lib.algorithm.ints;

import java.util.NoSuchElementException;
import java.util.function.IntBinaryOperator;

/**
 * @author https://atcoder.jp/users/suisen
 * 
 * Sliding Window Aggregation
 */
public final class IntArraySWAG {
    private final IntBinaryOperator op;
    private final int e;
    private final int n;
    private final int[] a;
    private final int[] s;
    private int l, m, r = 0;
    public IntArraySWAG(int[] a, IntBinaryOperator op, int e) {
        this.op = op;
        this.e = e;
        this.n = a.length;
        this.a = a;
        this.s = new int[n];
    }
    public int fold() {
        int res = e;
        if (l < m) res = op.applyAsInt(res, s[l]);
        if (m < r) res = op.applyAsInt(res, s[r - 1]);
        return res;
    }
    public void proceedRight() {
        if (r == n) throw new NoSuchElementException();
        s[r] = m == r ? a[r] : op.applyAsInt(s[r - 1], a[r]);
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
                s[i] = op.applyAsInt(a[i], s[i + 1]);
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