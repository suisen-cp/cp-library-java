package lib.datastructure;

@SuppressWarnings("unchecked")
public class CommutativeDualSegmentTree<F, S> {
    @FunctionalInterface
    public interface MappingUsingIndex<F, S> {
        S apply(F f, int index, S s);
    }

    final int MAX;

    final int N;
    final MappingUsingIndex<F, S> Mapping;
    final java.util.function.BinaryOperator<F> Composition;
    final F Id;

    final S[] Dat;
    final F[] Laz;

    public CommutativeDualSegmentTree(int n, S initailValue, java.util.function.BiFunction<F, S, S> mapping, java.util.function.BinaryOperator<F> composition, F id) {
        this((S[]) new Object[n], mapping, composition, id);
        java.util.Arrays.fill(Dat, initailValue);
    }

    public CommutativeDualSegmentTree(int n, S initailValue, MappingUsingIndex<F, S> mapping, java.util.function.BinaryOperator<F> composition, F id) {
        this((S[]) new Object[n], mapping, composition, id);
        java.util.Arrays.fill(Dat, initailValue);
    }

    public CommutativeDualSegmentTree(S[] dat, java.util.function.BiFunction<F, S, S> mapping, java.util.function.BinaryOperator<F> composition, F id) {
        this(dat, (f, i, s) -> mapping.apply(f, s), composition, id);
    }

    public CommutativeDualSegmentTree(S[] dat, MappingUsingIndex<F, S> mapping, java.util.function.BinaryOperator<F> composition, F id) {
        this.MAX = dat.length;
        int k = 1;
        while (k < MAX) k <<= 1;
        this.N = k;
        this.Mapping = mapping;
        this.Composition = composition;
        this.Id = id;
        this.Dat = java.util.Arrays.copyOf(dat, MAX);
        this.Laz = (F[]) new Object[N << 1];
        java.util.Arrays.fill(Laz, Id);
    }

    public S get(int p) {
        exclusiveRangeCheck(p);
        F f = Id;
        for (int k = p + N; k > 0; k >>= 1) {
            f = Composition.apply(f, Laz[k]);
        }
        return Mapping.apply(f, p, Dat[p]);
    }

    public void apply(int p, F f) {
        exclusiveRangeCheck(p);
        Laz[p + N] = Composition.apply(f, Laz[p + N]);
    }

    public void apply(int l, int r, F f) {
        if (l > r) throw new IllegalArgumentException(String.format("Invalid range: [%d, %d)", l, r));
        inclusiveRangeCheck(l);
        inclusiveRangeCheck(r);
        for (l += N, r += N; l < r; l >>= 1, r >>= 1) {
            if ((l & 1) == 1) Laz[l] = Composition.apply(f, Laz[l++]);
            if ((r & 1) == 1) Laz[--r] = Composition.apply(f, Laz[r]);
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
