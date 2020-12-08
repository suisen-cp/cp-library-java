package lib.datastructure.ints;

import java.util.Arrays;
import java.util.function.IntBinaryOperator;
import java.util.function.IntPredicate;

@SuppressWarnings("PointlessBitwiseExpression")
public class IntSegmentTree {
    final int MAX;

    final int N;
    final IntBinaryOperator op;
    final int E;

    final int[] data;

    public IntSegmentTree(int n, IntBinaryOperator op, int e) {
        this.MAX = n;
        int k = 1;
        while (k < n) k <<= 1;
        this.N = k;
        this.E = e;
        this.op = op;
        this.data = new int[N << 1];
        Arrays.fill(data, E);
    }

    public IntSegmentTree(int[] dat, IntBinaryOperator op, int e) {
        this(dat.length, op, e);
        build(dat);
    }

    private void build(int[] dat) {
        int l = dat.length;
        System.arraycopy(dat, 0, data, N, l);
        for (int i = N - 1; i > 0; i--) {
            data[i] = op.applyAsInt(data[i << 1 | 0], data[i << 1 | 1]);
        }
    }

    public void set(int p, int x) {
        exclusiveRangeCheck(p);
        data[p += N] = x;
        p >>= 1;
        while (p > 0) {
            data[p] = op.applyAsInt(data[p << 1 | 0], data[p << 1 | 1]);
            p >>= 1;
        }
    }

    public int get(int p) {
        exclusiveRangeCheck(p);
        return data[p + N];
    }

    public int prod(int l, int r) {
        if (l > r) {
            throw new IllegalArgumentException(
                String.format("Invalid range: [%d, %d)", l, r)
            );
        }
        inclusiveRangeCheck(l);
        inclusiveRangeCheck(r);
        int sumLeft = E;
        int sumRight = E;
        l += N; r += N;
        while (l < r) {
            if ((l & 1) == 1) sumLeft = op.applyAsInt(sumLeft, data[l++]);
            if ((r & 1) == 1) sumRight = op.applyAsInt(data[--r], sumRight);
            l >>= 1; r >>= 1;
        }
        return op.applyAsInt(sumLeft, sumRight);
    }

    public int allProd() {
        return data[1];
    }

    public int maxRight(int l, IntPredicate f) {
        inclusiveRangeCheck(l);
        if (!f.test(E)) {
            throw new IllegalArgumentException("Identity element must satisfy the condition.");
        }
        if (l == MAX) return MAX;
        l += N;
        int sum = E;
        do {
            l >>= Integer.numberOfTrailingZeros(l);
            if (!f.test(op.applyAsInt(sum, data[l]))) {
                while (l < N) {
                    l = l << 1;
                    if (f.test(op.applyAsInt(sum, data[l]))) {
                        sum = op.applyAsInt(sum, data[l]);
                        l++;
                    }
                }
                return l - N;
            }
            sum = op.applyAsInt(sum, data[l]);
            l++;
        } while ((l & -l) != l);
        return MAX;
    }

    public int minLeft(int r, IntPredicate f) {
        inclusiveRangeCheck(r);
        if (!f.test(E)) {
            throw new IllegalArgumentException("Identity element must satisfy the condition.");
        }
        if (r == 0) return 0;
        r += N;
        int sum = E;
        do {
            r--;
            while (r > 1 && (r & 1) == 1) r >>= 1;
            if (!f.test(op.applyAsInt(data[r], sum))) {
                while (r < N) {
                    r = r << 1 | 1;
                    if (f.test(op.applyAsInt(data[r], sum))) {
                        sum = op.applyAsInt(data[r], sum);
                        r--;
                    }
                }
                return r + 1 - N;
            }
            sum = op.applyAsInt(data[r], sum);
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