package lib.datastructure.longs;

import java.util.function.LongBinaryOperator;

@SuppressWarnings("PointlessBitwiseExpression")
public class LongDualSegmentTree {
    @FunctionalInterface
    public interface MappingUsingIndex {
        long apply(long f, int index, long s);
    }
    @FunctionalInterface
    public interface Mapping {
        long apply(long f, long s);
    }

    final int MAX;

    final int N;
    final int Log;
    final MappingUsingIndex Mapping;
    final LongBinaryOperator Composition;
    final long Id;

    final long[] Dat;
    final long[] Laz;

    public LongDualSegmentTree(int n, long initailValue, Mapping mapping, LongBinaryOperator composition, long id) {
        this(new long[n], mapping, composition, id);
        java.util.Arrays.fill(Dat, initailValue);
    }

    public LongDualSegmentTree(int n, long initailValue, MappingUsingIndex mapping, LongBinaryOperator composition, long id) {
        this(new long[n], mapping, composition, id);
        java.util.Arrays.fill(Dat, initailValue);
    }

    public LongDualSegmentTree(long[] dat, Mapping mapping, LongBinaryOperator composition, long id) {
        this(dat, (f, i, s) -> mapping.apply(f, s), composition, id);
    }

    public LongDualSegmentTree(long[] dat, MappingUsingIndex mapping, LongBinaryOperator composition, long id) {
        this.MAX = dat.length;
        int k = 1;
        while (k < MAX) k <<= 1;
        this.N = k;
        this.Log = Integer.numberOfTrailingZeros(N);
        this.Mapping = mapping;
        this.Composition = composition;
        this.Id = id;
        this.Dat = java.util.Arrays.copyOf(dat, MAX);
        this.Laz = new long[N << 1];
        java.util.Arrays.fill(Laz, Id);
    }

    private void push(int k) {
        if (Laz[k] == Id) return;
        int lk = k << 1 | 0, rk = k << 1 | 1;
        Laz[lk] = Composition.applyAsLong(Laz[k], Laz[lk]);
        Laz[rk] = Composition.applyAsLong(Laz[k], Laz[rk]);
        Laz[k] = Id;
    }

    private void pushTo(int k) {
        for (int i = Log; i > 0; i--) push(k >> i);
    }

    private void pushTo(int lk, int rk) {
        for (int i = Log; i > 0; i--) {
            if (((lk >> i) << i) != lk) push(lk >> i);
            if (((rk >> i) << i) != rk) push(rk >> i);
        }
    }

    public void set(int p, long x) {
        exclusiveRangeCheck(p);
        pushTo(p + N);
        Laz[p + N] = Id;
        Dat[p] = x;
    }

    public long get(int p) {
        exclusiveRangeCheck(p);
        pushTo(p + N);
        return Laz[p + N] == Id ? Dat[p] : Mapping.apply(Laz[p + N], p, Dat[p]);
    }

    public void apply(int p, long f) {
        exclusiveRangeCheck(p);
        p += N;
        pushTo(p);
        Laz[p] = Composition.applyAsLong(f, Laz[p]);
    }

    public void apply(int l, int r, long f) {
        if (l > r) throw new IllegalArgumentException(String.format("Invalid range: [%d, %d)", l, r));
        inclusiveRangeCheck(l);
        inclusiveRangeCheck(r);
        if (l == r) return;
        l += N; r += N;
        pushTo(l, r);
        for (; l < r; l >>= 1, r >>= 1) {
            if ((l & 1) == 1) Laz[l] = Composition.applyAsLong(f, Laz[l++]);
            if ((r & 1) == 1) Laz[--r] = Composition.applyAsLong(f, Laz[r]);
        }
    }

    private void exclusiveRangeCheck(int p) {
        if (p >= 0 && p < MAX) return;
        throw new IndexOutOfBoundsException(String.format("Index %d is out of [%d, %d).", p, 0, MAX));
    }

    private void inclusiveRangeCheck(int p) {
        if (p >= 0 && p <= MAX) return;
        throw new IndexOutOfBoundsException(String.format("Index %d is out of [%d, %d].", p, 0, MAX));
    }

    // **************** DEBUG **************** //

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < MAX; i++) {
            sb.append(get(i));
            if (i < MAX - 1) sb.append(',').append(' ');
        }
        sb.append(']');
        return sb.toString();
    }
}
