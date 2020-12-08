package lib.algorithm.ints;

import java.util.NoSuchElementException;
import java.util.function.IntBinaryOperator;

/**
 * @author https://atcoder.jp/users/suisen
 * 
 * Sliding Window Aggregation.
 */
public class IntSWAG {
    private static final int DEFAULT_SIZE = 64;
    private final IntBinaryOperator op;
    private final int e;
    private int[] dat;
    private int[] sum;
    private int l, m, r;
    private int len;
    private int mask;
    public IntSWAG(IntBinaryOperator op, int e, int capacity) {
        this.op = op;
        this.e = e;
        int k = 1;
        while (k < capacity) k <<= 1;
        this.len = k;
        this.mask = k - 1;
        this.dat = new int[k];
        this.sum = new int[k];
    }
    public IntSWAG(IntBinaryOperator op, int e) {
        this(op, e, DEFAULT_SIZE);
    }
    public int fold() {
        int res = e;
        if (l < m) res = op.applyAsInt(res, sum[l     & mask]);
        if (m < r) res = op.applyAsInt(res, sum[r - 1 & mask]);
        return res;
    }
    public void add(int v) {
        if (r - l == dat.length) grow();
        dat[r & mask] = v;
        sum[r & mask] = m == r ? v : op.applyAsInt(sum[r - 1 & mask], v);
        r++;
    }
    public int poll() {
        if (l == r) throw new NoSuchElementException();
        if (l == m) { // The 1st queue is empty
            m = r;
            sum[r - 1 & mask] = dat[r - 1 & mask];
            for (int i = r - 2; i > l; i--) {
                sum[i & mask] = op.applyAsInt(dat[i & mask], sum[i + 1 & mask]);
            }
        }
        return dat[l++ & mask];
    }
    private void grow() {
        int newLen = len << 1;
        int[] newDat = new int[newLen];
        int[] newSum = new int[newLen];
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