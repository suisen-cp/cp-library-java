package lib.datastructure.ints;

import java.util.function.IntBinaryOperator;

@SuppressWarnings("PointlessBitwiseExpression")
public class IntDualSegmentTree {
    @FunctionalInterface
    public interface MappingUsingIndex {
        int apply(int f, int index, int s);
    }
    @FunctionalInterface
    public interface Mapping {
        int apply(int f, int s);
    }

    final int MAX;

    final int N;
    final int Log;
    final MappingUsingIndex Mapping;
    final IntBinaryOperator Composition;
    final int Id;

    final int[] Dat;
    final int[] Laz;

    public IntDualSegmentTree(int n, int initailValue, Mapping mapping, IntBinaryOperator composition, int id) {
        this(new int[n], mapping, composition, id);
        java.util.Arrays.fill(Dat, initailValue);
    }

    public IntDualSegmentTree(int n, int initailValue, MappingUsingIndex mapping, IntBinaryOperator composition, int id) {
        this(new int[n], mapping, composition, id);
        java.util.Arrays.fill(Dat, initailValue);
    }

    public IntDualSegmentTree(int[] dat, Mapping mapping, IntBinaryOperator composition, int id) {
        this(dat, (f, i, s) -> mapping.apply(f, s), composition, id);
    }

    public IntDualSegmentTree(int[] dat, MappingUsingIndex mapping, IntBinaryOperator composition, int id) {
        this.MAX = dat.length;
        int k = 1;
        while (k < MAX) k <<= 1;
        this.N = k;
        this.Log = Integer.numberOfTrailingZeros(N);
        this.Mapping = mapping;
        this.Composition = composition;
        this.Id = id;
        this.Dat = java.util.Arrays.copyOf(dat, MAX);
        this.Laz = new int[N << 1];
        java.util.Arrays.fill(Laz, Id);
    }

    private void push(int k) {
        if (Laz[k] == Id) return;
        int lk = k << 1 | 0, rk = k << 1 | 1;
        Laz[lk] = Composition.applyAsInt(Laz[k], Laz[lk]);
        Laz[rk] = Composition.applyAsInt(Laz[k], Laz[rk]);
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

    public void set(int p, int x) {
        exclusiveRangeCheck(p);
        pushTo(p + N);
        Laz[p + N] = Id;
        Dat[p] = x;
    }

    public int get(int p) {
        exclusiveRangeCheck(p);
        pushTo(p + N);
        return Laz[p + N] == Id ? Dat[p] : Mapping.apply(Laz[p + N], p, Dat[p]);
    }

    public void apply(int p, int f) {
        exclusiveRangeCheck(p);
        p += N;
        pushTo(p);
        Laz[p] = Composition.applyAsInt(f, Laz[p]);
    }

    public void apply(int l, int r, int f) {
        if (l > r) throw new IllegalArgumentException(String.format("Invalid range: [%d, %d)", l, r));
        inclusiveRangeCheck(l);
        inclusiveRangeCheck(r);
        if (l == r) return;
        l += N; r += N;
        pushTo(l, r);
        for (; l < r; l >>= 1, r >>= 1) {
            if ((l & 1) == 1) Laz[l] = Composition.applyAsInt(f, Laz[l++]);
            if ((r & 1) == 1) Laz[--r] = Composition.applyAsInt(f, Laz[r]);
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
