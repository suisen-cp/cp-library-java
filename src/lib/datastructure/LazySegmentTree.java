package lib.datastructure;

@SuppressWarnings("PointlessBitwiseExpression")
public class LazySegmentTree<S, F> {

    @FunctionalInterface
    public interface FunMergerLR<Func> {
        Func apply(Func func, int left, int right);
    }

    @FunctionalInterface
    public interface FunMergerWidth<Func> {
        Func apply(Func func, int w);
    }

    final int MAX;

    final int N;
    final int Log;
    final java.util.function.BinaryOperator<S> Op;
    final S E;
    final java.util.function.BiFunction<F, S, S> Mapping;
    final java.util.function.BinaryOperator<F> Composition;
    final FunMergerLR<F> ProdComposition;
    final F Id;

    final S[] Dat;
    final F[] Laz;

    @SuppressWarnings("unchecked")
    public LazySegmentTree(int n, java.util.function.BinaryOperator<S> op, S e, java.util.function.BiFunction<F, S, S> mapping, java.util.function.BinaryOperator<F> composition, FunMergerLR<F> prodComposition, F id) {
        this.MAX = n;
        int k = 1;
        while (k < n) k <<= 1;
        this.N = k;
        this.Log = Integer.numberOfTrailingZeros(N);
        this.Op = op;
        this.E = e;
        this.Mapping = mapping;
        this.Composition = composition;
        this.ProdComposition = prodComposition;
        this.Id = id;
        this.Dat = (S[]) new Object[N << 1];
        this.Laz = (F[]) new Object[N];
        java.util.Arrays.fill(Dat, E);
        java.util.Arrays.fill(Laz, Id);
    }

    public LazySegmentTree(int n, java.util.function.BinaryOperator<S> op, S e, java.util.function.BiFunction<F, S, S> mapping, java.util.function.BinaryOperator<F> composition, FunMergerWidth<F> prodComposition, F id) {
        this(n, op, e, mapping, composition, (f, l, r) -> prodComposition.apply(f, r - l), id);
    }

    public LazySegmentTree(int n, java.util.function.BinaryOperator<S> op, S e, java.util.function.BiFunction<F, S, S> mapping, java.util.function.BinaryOperator<F> composition, F id) {
        this(n, op, e, mapping, composition, (f, l, r) -> f, id);
    }

    public LazySegmentTree(S[] dat, java.util.function.BinaryOperator<S> op, S e, java.util.function.BiFunction<F, S, S> mapping, java.util.function.BinaryOperator<F> composition, FunMergerLR<F> prodComposition, F id) {
        this(dat.length, op, e, mapping, composition, prodComposition, id);
        build(dat);
    }

    public LazySegmentTree(S[] dat, java.util.function.BinaryOperator<S> op, S e, java.util.function.BiFunction<F, S, S> mapping, java.util.function.BinaryOperator<F> composition, FunMergerWidth<F> prodComposition, F id) {
        this(dat.length, op, e, mapping, composition, prodComposition, id);
        build(dat);
    }

    public LazySegmentTree(S[] dat, java.util.function.BinaryOperator<S> op, S e, java.util.function.BiFunction<F, S, S> mapping, java.util.function.BinaryOperator<F> composition, F id) {
        this(dat.length, op, e, mapping, composition, id);
        build(dat);
    }

    private void build(S[] dat) {
        int l = dat.length;
        System.arraycopy(dat, 0, Dat, N, l);
        for (int i = N - 1; i > 0; i--) {
            Dat[i] = Op.apply(Dat[i << 1 | 0], Dat[i << 1 | 1]);
        }
    }

    private void push(int k, int h) {
        if (Laz[k] == Id) return;
        int w = 1 << h - 1;
        int l = (k << h) ^ N;
        int lk = k << 1 | 0, rk = k << 1 | 1;
        Dat[lk] = Mapping.apply(ProdComposition.apply(Laz[k], l, l += w), Dat[lk]);
        Dat[rk] = Mapping.apply(ProdComposition.apply(Laz[k], l, l += w), Dat[rk]);
        if (lk < N) Laz[lk] = Composition.apply(Laz[k], Laz[lk]);
        if (rk < N) Laz[rk] = Composition.apply(Laz[k], Laz[rk]);
        Laz[k] = Id;
    }

    private void pushTo(int k) {
        for (int i = Log; i > 0; i--) push(k >> i, i);
    }

    private void pushTo(int lk, int rk) {
        for (int i = Log; i > 0; i--) {
            if (((lk >> i) << i) != lk) push(lk >> i, i);
            if (((rk >> i) << i) != rk) push(rk >> i, i);
        }
    }

    private void updateFrom(int k) {
        k >>= 1;
        while (k > 0) {
            Dat[k] = Op.apply(Dat[k << 1 | 0], Dat[k << 1 | 1]);
            k >>= 1;
        }
    }

    private void updateFrom(int lk, int rk) {
        for (int i = 1; i <= Log; i++) {
            if (((lk >> i) << i) != lk) {
                int lki = lk >> i;
                Dat[lki] = Op.apply(Dat[lki << 1 | 0], Dat[lki << 1 | 1]);
            }
            if (((rk >> i) << i) != rk) {
                int rki = (rk - 1) >> i;
                Dat[rki] = Op.apply(Dat[rki << 1 | 0], Dat[rki << 1 | 1]);
            }
        }
    }

    public void set(int p, S x) {
        exclusiveRangeCheck(p);
        p += N;
        pushTo(p);
        Dat[p] = x;
        updateFrom(p);
    }

    public S get(int p) {
        exclusiveRangeCheck(p);
        p += N;
        pushTo(p);
        return Dat[p];
    }

    public S prod(int l, int r) {
        if (l > r) {
            throw new IllegalArgumentException(
                String.format("Invalid range: [%d, %d)", l, r)
            );
        }
        inclusiveRangeCheck(l);
        inclusiveRangeCheck(r);
        if (l == r) return E;
        l += N; r += N;
        pushTo(l, r);
        S sumLeft = E, sumRight = E;
        while (l < r) {
            if ((l & 1) == 1) sumLeft = Op.apply(sumLeft, Dat[l++]);
            if ((r & 1) == 1) sumRight = Op.apply(Dat[--r], sumRight);
            l >>= 1; r >>= 1;
        }
        return Op.apply(sumLeft, sumRight);
    }

    public S allProd() {
        return Dat[1];
    }

    public void apply(int p, F f) {
        exclusiveRangeCheck(p);
        int pk = p + N;
        pushTo(pk);
        Dat[pk] = Mapping.apply(ProdComposition.apply(f, p, p + 1), Dat[pk]);
        updateFrom(pk);
    }

    public void apply(int l, int r, F f) {
        if (l > r) {
            throw new IllegalArgumentException(
                String.format("Invalid range: [%d, %d)", l, r)
            );
        }
        inclusiveRangeCheck(l);
        inclusiveRangeCheck(r);
        if (l == r) return;
        l += N; r += N;
        pushTo(l, r);
        for (int l2 = l, r2 = r, h = 0; l2 < r2; h++) {
            if ((l2 & 1) == 1) {
                int l2l = (l2 << h) ^ N;
                Dat[l2] = Mapping.apply(ProdComposition.apply(f, l2l, l2l + (1 << h)), Dat[l2]);
                if (l2 < N) Laz[l2] = Composition.apply(f, Laz[l2]);
                l2++;
            }
            if ((r2 & 1) == 1) {
                r2--;
                int r2l = (r2 << h) ^ N;
                Dat[r2] = Mapping.apply(ProdComposition.apply(f, r2l, r2l + (1 << h)), Dat[r2]);
                if (r2 < N) Laz[r2] = Composition.apply(f, Laz[r2]);
            }
            l2 >>= 1; r2 >>= 1;
        }
        updateFrom(l, r);
    }

    public int maxRight(int l, java.util.function.Predicate<S> g) {
        inclusiveRangeCheck(l);
        if (!g.test(E)) {
            throw new IllegalArgumentException("Identity element must satisfy the condition.");
        }
        if (l == MAX) return MAX;
        l += N;
        pushTo(l);
        S sum = E;
        do {
            l >>= Integer.numberOfTrailingZeros(l);
            if (!g.test(Op.apply(sum, Dat[l]))) {
                while (l < N) {
                    push(l, 31 - Integer.numberOfLeadingZeros(l));
                    l = l << 1;
                    if (g.test(Op.apply(sum, Dat[l]))) {
                        sum = Op.apply(sum, Dat[l]);
                        l++;
                    }
                }
                return l - N;
            }
            sum = Op.apply(sum, Dat[l]);
            l++;
        } while ((l & -l) != l);
        return MAX;
    }

    public int minLeft(int r, java.util.function.Predicate<S> g) {
        inclusiveRangeCheck(r);
        if (!g.test(E)) {
            throw new IllegalArgumentException("Identity element must satisfy the condition.");
        }
        if (r == 0) return 0;
        r += N;
        pushTo(r - 1);
        S sum = E;
        do {
            r--;
            while (r > 1 && (r & 1) == 1) r >>= 1;
            if (!g.test(Op.apply(Dat[r], sum))) {
                while (r < N) {
                    push(r, 31 - Integer.numberOfLeadingZeros(r));
                    r = r << 1 | 1;
                    if (g.test(Op.apply(Dat[r], sum))) {
                        sum = Op.apply(Dat[r], sum);
                        r--;
                    }
                }
                return r + 1 - N;
            }
            sum = Op.apply(Dat[r], sum);
        } while ((r & -r) != r);
        return 0;
    }

    private void exclusiveRangeCheck(int p) {
        if (p < 0 || p >= MAX) {
            throw new IndexOutOfBoundsException(
                String.format("Index %d is not in [%d, %d).", p, 0, MAX)
            );
        }
    }

    private void inclusiveRangeCheck(int p) {
        if (p < 0 || p > MAX) {
            throw new IndexOutOfBoundsException(
                String.format("Index %d is not in [%d, %d].", p, 0, MAX)
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

    private S[] simulatePushAll() {
        S[] simDat = java.util.Arrays.copyOf(Dat, 2 * N);
        F[] simLaz = java.util.Arrays.copyOf(Laz, 2 * N);
        for (int k = 1; k < N; k++) {
            if (simLaz[k] == Id) continue;
            int lk = k << 1 | 0, rk = k << 1 | 1;
            simDat[lk] = Mapping.apply(simLaz[k], simDat[lk]);
            simDat[rk] = Mapping.apply(simLaz[k], simDat[rk]);
            if (lk < N) simLaz[lk] = Composition.apply(simLaz[k], simLaz[lk]);
            if (rk < N) simLaz[rk] = Composition.apply(simLaz[k], simLaz[rk]);
            simLaz[k] = Id;
        }
        return simDat;
    }

    public String toDetailedString() {
        return toDetailedString(1, 0, simulatePushAll());
    }

    private String toDetailedString(int k, int sp, S[] dat) {
        if (k >= N) return indent(sp) + dat[k];
        String s = "";
        s += toDetailedString(k << 1 | 1, sp + indent, dat);
        s += "\n";
        s += indent(sp) + dat[k];
        s += "\n";
        s += toDetailedString(k << 1 | 0, sp + indent, dat);
        return s;
    }

    private static String indent(int n) {
        StringBuilder sb = new StringBuilder();
        while (n --> 0) sb.append(' ');
        return sb.toString();
    }

    public String toSimpleString() {
        S[] dat = simulatePushAll();
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < N; i++) {
            sb.append(dat[i + N]);
            if (i < N - 1) sb.append(',').append(' ');
        }
        sb.append(']');
        return sb.toString();
    }
}