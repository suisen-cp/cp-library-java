package lib.algorithm;

import java.util.NoSuchElementException;
import java.util.function.BinaryOperator;

/**
 * @author https://atcoder.jp/users/suisen
 * 
 * Sliding Window Aggregation
 */
@SuppressWarnings("unchecked")
public final class ArraySWAG<T> {
    private final BinaryOperator<T> op;
    private final T e;
    private final int n;
    private final T[] a;
    private final T[] s;
    private int l, m, r = 0;
    public ArraySWAG(T[] a, BinaryOperator<T> op, T e) {
        this.op = op;
        this.e = e;
        this.n = a.length;
        this.a = a;
        this.s = (T[]) new Object[n];
    }
    public T fold() {
        T res = e;
        if (l < m) res = op.apply(res, s[l]);
        if (m < r) res = op.apply(res, s[r - 1]);
        return res;
    }
    public void proceedRight() {
        if (r == n) throw new NoSuchElementException();
        s[r] = m == r ? a[r] : op.apply(s[r - 1], a[r]);
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
                s[i] = op.apply(a[i], s[i + 1]);
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