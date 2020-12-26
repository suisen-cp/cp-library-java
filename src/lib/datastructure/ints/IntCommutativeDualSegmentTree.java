package lib.datastructure.ints;

import java.util.function.IntBinaryOperator;

public class IntCommutativeDualSegmentTree {
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
    final MappingUsingIndex Mapping;
    final IntBinaryOperator Composition;
    final int Id;

    final int[] Dat;
    final int[] Laz;

    public IntCommutativeDualSegmentTree(int n, int initailValue, Mapping mapping, IntBinaryOperator composition, int id) {
        this(new int[n], mapping, composition, id);
        java.util.Arrays.fill(Dat, initailValue);
    }

    public IntCommutativeDualSegmentTree(int n, int initailValue, MappingUsingIndex mapping, IntBinaryOperator composition, int id) {
        this(new int[n], mapping, composition, id);
        java.util.Arrays.fill(Dat, initailValue);
    }

    public IntCommutativeDualSegmentTree(int[] dat, Mapping mapping, java.util.function.IntBinaryOperator composition, int id) {
        this(dat, (f, i, s) -> mapping.apply(f, s), composition, id);
    }

    public IntCommutativeDualSegmentTree(int[] dat, MappingUsingIndex mapping, java.util.function.IntBinaryOperator composition, int id) {
        this.MAX = dat.length;
        int k = 1;
        while (k < MAX) k <<= 1;
        this.N = k;
        this.Mapping = mapping;
        this.Composition = composition;
        this.Id = id;
        this.Dat = java.util.Arrays.copyOf(dat, MAX);
        this.Laz = new int[N << 1];
        java.util.Arrays.fill(Laz, Id);
    }

    public int get(int p) {
        exclusiveRangeCheck(p);
        int f = Id;
        for (int k = p + N; k > 0; k >>= 1) {
            f = Composition.applyAsInt(f, Laz[k]);
        }
        return Mapping.apply(f, p, Dat[p]);
    }

    public void apply(int p, int f) {
        exclusiveRangeCheck(p);
        Laz[p + N] = Composition.applyAsInt(f, Laz[p + N]);
    }

    public void apply(int l, int r, int f) {
        if (l > r) throw new IllegalArgumentException(String.format("Invalid range: [%d, %d)", l, r));
        inclusiveRangeCheck(l);
        inclusiveRangeCheck(r);
        for (l += N, r += N; l < r; l >>= 1, r >>= 1) {
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

