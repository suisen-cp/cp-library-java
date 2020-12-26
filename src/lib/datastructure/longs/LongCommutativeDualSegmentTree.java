package lib.datastructure.longs;

import java.util.function.LongBinaryOperator;

public class LongCommutativeDualSegmentTree {
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
    final MappingUsingIndex Mapping;
    final LongBinaryOperator Composition;
    final long Id;

    final long[] Dat;
    final long[] Laz;

    public LongCommutativeDualSegmentTree(int n, long initailValue, Mapping mapping, LongBinaryOperator composition, long id) {
        this(new long[n], mapping, composition, id);
        java.util.Arrays.fill(Dat, initailValue);
    }

    public LongCommutativeDualSegmentTree(int n, long initailValue, MappingUsingIndex mapping, LongBinaryOperator composition, long id) {
        this(new long[n], mapping, composition, id);
        java.util.Arrays.fill(Dat, initailValue);
    }

    public LongCommutativeDualSegmentTree(long[] dat, Mapping mapping, LongBinaryOperator composition, long id) {
        this(dat, (f, i, s) -> mapping.apply(f, s), composition, id);
    }

    public LongCommutativeDualSegmentTree(long[] dat, MappingUsingIndex mapping, LongBinaryOperator composition, long id) {
        this.MAX = dat.length;
        int k = 1;
        while (k < MAX) k <<= 1;
        this.N = k;
        this.Mapping = mapping;
        this.Composition = composition;
        this.Id = id;
        this.Dat = java.util.Arrays.copyOf(dat, MAX);
        this.Laz = new long[N << 1];
        java.util.Arrays.fill(Laz, Id);
    }

    public long get(int p) {
        exclusiveRangeCheck(p);
        long f = Id;
        for (int k = p + N; k > 0; k >>= 1) {
            f = Composition.applyAsLong(f, Laz[k]);
        }
        return Mapping.apply(f, p, Dat[p]);
    }

    public void apply(int p, long f) {
        exclusiveRangeCheck(p);
        Laz[p + N] = Composition.applyAsLong(f, Laz[p + N]);
    }

    public void apply(int l, int r, long f) {
        if (l > r) throw new IllegalArgumentException(String.format("Invalid range: [%d, %d)", l, r));
        inclusiveRangeCheck(l);
        inclusiveRangeCheck(r);
        for (l += N, r += N; l < r; l >>= 1, r >>= 1) {
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

