package lib.datastructure.longs;

import java.util.function.LongBinaryOperator;
import java.util.function.LongPredicate;

@SuppressWarnings("PointlessBitwiseExpression")
public class LongSegmentTree {
    final int MAX;

    final int N;
    final LongBinaryOperator op;
    final long E;

    final long[] data;

    public LongSegmentTree(int n, LongBinaryOperator op, long e) {
        this.MAX = n;
        int k = 1;
        while (k < n) k <<= 1;
        this.N = k;
        this.E = e;
        this.op = op;
        this.data = new long[N << 1];
        java.util.Arrays.fill(data, E);
    }

    public LongSegmentTree(long[] dat, LongBinaryOperator op, long e) {
        this(dat.length, op, e);
        build(dat);
    }

    private void build(long[] dat) {
        int l = dat.length;
        System.arraycopy(dat, 0, data, N, l);
        for (int i = N - 1; i > 0; i--) {
            data[i] = op.applyAsLong(data[i << 1 | 0], data[i << 1 | 1]);
        }
    }

    public void set(int p, long x) {
        exclusiveRangeCheck(p);
        data[p += N] = x;
        p >>= 1;
        while (p > 0) {
            data[p] = op.applyAsLong(data[p << 1 | 0], data[p << 1 | 1]);
            p >>= 1;
        }
    }

    public long get(int p) {
        exclusiveRangeCheck(p);
        return data[p + N];
    }

    public long prod(int l, int r) {
        if (l > r) {
            throw new IllegalArgumentException(
                String.format("Invalid range: [%d, %d)", l, r)
            );
        }
        inclusiveRangeCheck(l);
        inclusiveRangeCheck(r);
        long sumLeft = E;
        long sumRight = E;
        l += N; r += N;
        while (l < r) {
            if ((l & 1) == 1) sumLeft = op.applyAsLong(sumLeft, data[l++]);
            if ((r & 1) == 1) sumRight = op.applyAsLong(data[--r], sumRight);
            l >>= 1; r >>= 1;
        }
        return op.applyAsLong(sumLeft, sumRight);
    }

    public long allProd() {
        return data[1];
    }

    public int maxRight(int l, LongPredicate f) {
        inclusiveRangeCheck(l);
        if (!f.test(E)) {
            throw new IllegalArgumentException("Identity element must satisfy the condition.");
        }
        if (l == MAX) return MAX;
        l += N;
        long sum = E;
        do {
            l >>= Integer.numberOfTrailingZeros(l);
            if (!f.test(op.applyAsLong(sum, data[l]))) {
                while (l < N) {
                    l = l << 1;
                    if (f.test(op.applyAsLong(sum, data[l]))) {
                        sum = op.applyAsLong(sum, data[l]);
                        l++;
                    }
                }
                return l - N;
            }
            sum = op.applyAsLong(sum, data[l]);
            l++;
        } while ((l & -l) != l);
        return MAX;
    }

    public int minLeft(int r, LongPredicate f) {
        inclusiveRangeCheck(r);
        if (!f.test(E)) {
            throw new IllegalArgumentException("Identity element must satisfy the condition.");
        }
        if (r == 0) return 0;
        r += N;
        long sum = E;
        do {
            r--;
            while (r > 1 && (r & 1) == 1) r >>= 1;
            if (!f.test(op.applyAsLong(data[r], sum))) {
                while (r < N) {
                    r = r << 1 | 1;
                    if (f.test(op.applyAsLong(data[r], sum))) {
                        sum = op.applyAsLong(data[r], sum);
                        r--;
                    }
                }
                return r + 1 - N;
            }
            sum = op.applyAsLong(data[r], sum);
        } while ((r & -r) != r);
        return 0;
    }

    private void exclusiveRangeCheck(int p) {
        if (p < 0 || p >= MAX) {
            throw new IndexOutOfBoundsException(
                String.format("Index %d out of bounds for the range [%d, %d).", p, 0, MAX)
            );
        }
    }

    private void inclusiveRangeCheck(int p) {
        if (p < 0 || p > MAX) {
            throw new IndexOutOfBoundsException(
                String.format("Index %d out of bounds for the range [%d, %d].", p, 0, MAX)
            );
        }
    }

    // **************** DEBUG **************** //

    private int indent = 6;

    public void setIndent(int newIndent) {
        this.indent = newIndent;
    }

    @Override
    public String toString() {
        return toSimpleString();
    }

    public String toDetailedString() {
        return toDetailedString(1, 0);
    }

    private String toDetailedString(int k, int sp) {
        if (k >= N) return indent(sp) + data[k];
        String s = "";
        s += toDetailedString(k << 1 | 1, sp + indent);
        s += "\n";
        s += indent(sp) + data[k];
        s += "\n";
        s += toDetailedString(k << 1 | 0, sp + indent);
        return s;
    }

    private static String indent(int n) {
        StringBuilder sb = new StringBuilder();
        while (n --> 0) sb.append(' ');
        return sb.toString();
    }

    public String toSimpleString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < N; i++) {
            sb.append(data[i + N]);
            if (i < N - 1) sb.append(',').append(' ');
        }
        sb.append(']');
        return sb.toString();
    }
}