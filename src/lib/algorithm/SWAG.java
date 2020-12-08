package lib.algorithm;

import java.util.NoSuchElementException;
import java.util.function.BinaryOperator;

/**
 * @author https://atcoder.jp/users/suisen
 * 
 * Sliding Window Aggregation.
 */
@SuppressWarnings("unchecked")
public class SWAG<T> {
    private static final int DEFAULT_SIZE = 64;
    private final BinaryOperator<T> op;
    private final T e;
    private T[] dat;
    private T[] sum;
    private int l, m, r;
    private int len;
    private int mask;
    public SWAG(BinaryOperator<T> op, T e, int capacity) {
        this.op = op;
        this.e = e;
        int k = 1;
        while (k < capacity) k <<= 1;
        this.len = k;
        this.mask = k - 1;
        this.dat = (T[]) new Object[k];
        this.sum = (T[]) new Object[k];
    }
    public SWAG(BinaryOperator<T> op, T e) {
        this(op, e, DEFAULT_SIZE);
    }
    public T fold() {
        T res = e;
        if (l < m) res = op.apply(res, sum[l     & mask]);
        if (m < r) res = op.apply(res, sum[r - 1 & mask]);
        return res;
    }
    public void add(T v) {
        if (r - l == dat.length) grow();
        dat[r & mask] = v;
        sum[r & mask] = m == r ? v : op.apply(sum[r - 1 & mask], v);
        r++;
    }
    public T poll() {
        if (l == r) throw new NoSuchElementException();
        if (l == m) { // The 1st queue is empty
            m = r;
            sum[r - 1 & mask] = dat[r - 1 & mask];
            for (int i = r - 2; i > l; i--) {
                sum[i & mask] = op.apply(dat[i & mask], sum[i + 1 & mask]);
            }
        }
        return dat[l++ & mask];
    }
    private void grow() {
        int newLen = len << 1;
        T[] newDat = (T[]) new Object[newLen];
        T[] newSum = (T[]) new Object[newLen];
        int l1 = m - l, l2 = r - m;
        for (int i = 1; i <= l1; i++) {
            newDat[newLen - i] = dat[m - i & mask];
            newSum[newLen - i] = sum[m - i & mask];
        }
        for (int i = 0; i < l2; i++) {
            newDat[i] = dat[m + i & mask];
            newSum[i] = sum[m + i & mask];
        }
        this.l = -l1;
        this.m = 0;
        this.r = l2;
        this.len = newLen;
        this.dat = newDat;
        this.sum = newSum;
        this.mask = len - 1;
    }
}