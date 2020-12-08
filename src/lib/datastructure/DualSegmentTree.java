package lib.datastructure;

@SuppressWarnings({"unchecked", "PointlessBitwiseExpression"})
public class DualSegmentTree<F, S> {
    @FunctionalInterface
    public interface MappingUsingIndex<F, S> {
        S apply(F f, int index, S s);
    }

    final int MAX;

    final int N;
    final int Log;
    final MappingUsingIndex<F, S> Mapping;
    final java.util.function.BinaryOperator<F> Composition;
    final F Id;

    final S[] Dat;
    final F[] Laz;

    public DualSegmentTree(int n, S initailValue, java.util.function.BiFunction<F, S, S> mapping, java.util.function.BinaryOperator<F> composition, F id) {
        this((S[]) new Object[n], mapping, composition, id);
        java.util.Arrays.fill(Dat, initailValue);
    }

    public DualSegmentTree(int n, S initailValue, MappingUsingIndex<F, S> mapping, java.util.function.BinaryOperator<F> composition, F id) {
        this((S[]) new Object[n], mapping, composition, id);
        java.util.Arrays.fill(Dat, initailValue);
    }

    public DualSegmentTree(S[] dat, java.util.function.BiFunction<F, S, S> mapping, java.util.function.BinaryOperator<F> composition, F id) {
        this(dat, (f, i, s) -> mapping.apply(f, s), composition, id);
    }

    public DualSegmentTree(S[] dat, MappingUsingIndex<F, S> mapping, java.util.function.BinaryOperator<F> composition, F id) {
        this.MAX = dat.length;
        int k = 1;
        while (k < MAX) k <<= 1;
        this.N = k;
        this.Log = Integer.numberOfTrailingZeros(N);
        this.Mapping = mapping;
        this.Composition = composition;
        this.Id = id;
        this.Dat = java.util.Arrays.copyOf(dat, MAX);
        this.Laz = (F[]) new Object[N << 1];
        java.util.Arrays.fill(Laz, Id);
    }

    private void push(int k) {
        if (Laz[k] == Id) return;
        int lk = k << 1 | 0, rk = k << 1 | 1;
        Laz[lk] = Composition.apply(Laz[k], Laz[lk]);
        Laz[rk] = Composition.apply(Laz[k], Laz[rk]);
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

    public void set(int p, S x) {
        exclusiveRangeCheck(p);
        pushTo(p + N);
        Laz[p + N] = Id;
        Dat[p] = x;
    }

    public S get(int p) {
        exclusiveRangeCheck(p);
        pushTo(p + N);
        return Laz[p + N] == Id ? Dat[p] : Mapping.apply(Laz[p + N], p, Dat[p]);
    }

    public void apply(int p, F f) {
        exclusiveRangeCheck(p);
        p += N;
        pushTo(p);
        Laz[p] = Composition.apply(f, Laz[p]);
    }

    public void apply(int l, int r, F f) {
        if (l > r) throw new IllegalArgumentException(String.format("Invalid range: [%d, %d)", l, r));
        inclusiveRangeCheck(l);
        inclusiveRangeCheck(r);
        if (l == r) return;
        l += N; r += N;
        pushTo(l, r);
        for (; l < r; l >>= 1, r >>= 1) {
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
