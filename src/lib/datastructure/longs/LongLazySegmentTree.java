package lib.datastructure.longs;

import java.util.function.LongBinaryOperator;

@SuppressWarnings("PointlessBitwiseExpression")
public class LongLazySegmentTree {

    @FunctionalInterface
    public interface FunMergerLR {
        long apply(long func, int left, int right);
    }

    @FunctionalInterface
    public interface FunMergerWidth {
        long apply(long f, int w);
    }

    final int MAX;

    final int N;
    final int Log;
    final LongBinaryOperator Op;
    final long E;
    final LongBinaryOperator Mapping;
    final LongBinaryOperator Composition;
    final FunMergerLR ProdComposition;
    final long Id;

    final long[] Dat;
    final long[] Laz;

    public LongLazySegmentTree(int n, LongBinaryOperator op, long e, LongBinaryOperator mapping, LongBinaryOperator composition, FunMergerLR prodComposition, long id) {
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
        this.Dat = new long[N << 1];
        this.Laz = new long[N];
        java.util.Arrays.fill(Dat, E);
        java.util.Arrays.fill(Laz, Id);
    }

    public LongLazySegmentTree(int n, LongBinaryOperator op, long e, LongBinaryOperator mapping, LongBinaryOperator composition, FunMergerWidth prodComposition, long id) {
        this(n, op, e, mapping, composition, (f, l, r) -> prodComposition.apply(f, r - l), id);
    }

    public LongLazySegmentTree(int n, LongBinaryOperator op, long e, LongBinaryOperator mapping, LongBinaryOperator composition, long id) {
        this(n, op, e, mapping, composition, (f, l, r) -> f, id);
    }

    public LongLazySegmentTree(long[] dat, LongBinaryOperator op, long e, LongBinaryOperator mapping, LongBinaryOperator composition, FunMergerLR prodComposition, long id) {
        this(dat.length, op, e, mapping, composition, prodComposition, id);
        build(dat);
    }

    public LongLazySegmentTree(long[] dat, LongBinaryOperator op, long e, LongBinaryOperator mapping, LongBinaryOperator composition, FunMergerWidth prodComposition, long id) {
        this(dat.length, op, e, mapping, composition, prodComposition, id);
        build(dat);
    }

    public LongLazySegmentTree(long[] dat, LongBinaryOperator op, long e, LongBinaryOperator mapping, LongBinaryOperator composition, long id) {
        this(dat.length, op, e, mapping, composition, id);
        build(dat);
    }

    private void build(long[] dat) {
        int l = dat.length;
        System.arraycopy(dat, 0, Dat, N, l);
        for (int i = N - 1; i > 0; i--) {
            Dat[i] = Op.applyAsLong(Dat[i << 1 | 0], Dat[i << 1 | 1]);
        }
    }

    private void push(int k, int h) {
        if (Laz[k] == Id) return;
        int w = 1 << h - 1;
        int l = (k << h) ^ N;
        int lk = k << 1 | 0, rk = k << 1 | 1;
        Dat[lk] = Mapping.applyAsLong(ProdComposition.apply(Laz[k], l, l += w), Dat[lk]);
        Dat[rk] = Mapping.applyAsLong(ProdComposition.apply(Laz[k], l, l += w), Dat[rk]);
        if (lk < N) Laz[lk] = Composition.applyAsLong(Laz[k], Laz[lk]);
        if (rk < N) Laz[rk] = Composition.applyAsLong(Laz[k], Laz[rk]);
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
            Dat[k] = Op.applyAsLong(Dat[k << 1 | 0], Dat[k << 1 | 1]);
            k >>= 1;
        }
    }

    private void updateFrom(int lk, int rk) {
        for (int i = 1; i <= Log; i++) {
            if (((lk >> i) << i) != lk) {
                int lki = lk >> i;
                Dat[lki] = Op.applyAsLong(Dat[lki << 1 | 0], Dat[lki << 1 | 1]);
            }
            if (((rk >> i) << i) != rk) {
                int rki = (rk - 1) >> i;
                Dat[rki] = Op.applyAsLong(Dat[rki << 1 | 0], Dat[rki << 1 | 1]);
            }
        }
    }

    public void set(int p, long x) {
        exclusiveRangeCheck(p);
        p += N;
        pushTo(p);
        Dat[p] = x;
        updateFrom(p);
    }

    public long get(int p) {
        exclusiveRangeCheck(p);
        p += N;
        pushTo(p);
        return Dat[p];
    }

    public long prod(int l, int r) {
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
        long sumLeft = E, sumRight = E;
        while (l < r) {
            if ((l & 1) == 1) sumLeft = Op.applyAsLong(sumLeft, Dat[l++]);
            if ((r & 1) == 1) sumRight = Op.applyAsLong(Dat[--r], sumRight);
            l >>= 1; r >>= 1;
        }
        return Op.applyAsLong(sumLeft, sumRight);
    }

    public long allProd() {
        return Dat[1];
    }

    public void apply(int p, long f) {
        exclusiveRangeCheck(p);
        int pk = p + N;
        pushTo(pk);
        Dat[pk] = Mapping.applyAsLong(ProdComposition.apply(f, p, p + 1), Dat[pk]);
        updateFrom(pk);
    }

    public void apply(int l, int r, long f) {
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
                Dat[l2] = Mapping.applyAsLong(ProdComposition.apply(f, l2l, l2l + (1 << h)), Dat[l2]);
                if (l2 < N) Laz[l2] = Composition.applyAsLong(f, Laz[l2]);
                l2++;
            }
            if ((r2 & 1) == 1) {
                r2--;
                int r2l = (r2 << h) ^ N;
                Dat[r2] = Mapping.applyAsLong(ProdComposition.apply(f, r2l, r2l + (1 << h)), Dat[r2]);
                if (r2 < N) Laz[r2] = Composition.applyAsLong(f, Laz[r2]);
            }
            l2 >>= 1; r2 >>= 1;
        }
        updateFrom(l, r);
    }

    public int maxRight(int l, java.util.function.LongPredicate g) {
        inclusiveRangeCheck(l);
        if (!g.test(E)) {
            throw new IllegalArgumentException("Identity element must satisfy the condition.");
        }
        if (l == MAX) return MAX;
        l += N;
        pushTo(l);
        long sum = E;
        do {
            l >>= Integer.numberOfTrailingZeros(l);
            if (!g.test(Op.applyAsLong(sum, Dat[l]))) {
                while (l < N) {
                    push(l, 31 - Integer.numberOfLeadingZeros(l));
                    l = l << 1;
                    if (g.test(Op.applyAsLong(sum, Dat[l]))) {
                        sum = Op.applyAsLong(sum, Dat[l]);
                        l++;
                    }
                }
                return l - N;
            }
            sum = Op.applyAsLong(sum, Dat[l]);
            l++;
        } while ((l & -l) != l);
        return MAX;
    }

    public int minLeft(int r, java.util.function.LongPredicate g) {
        inclusiveRangeCheck(r);
        if (!g.test(E)) {
            throw new IllegalArgumentException("Identity element must satisfy the condition.");
        }
        if (r == 0) return 0;
        r += N;
        pushTo(r - 1);
        long sum = E;
        do {
            r--;
            while (r > 1 && (r & 1) == 1) r >>= 1;
            if (!g.test(Op.applyAsLong(Dat[r], sum))) {
                while (r < N) {
                    push(r, 31 - Integer.numberOfLeadingZeros(r));
                    r = r << 1 | 1;
                    if (g.test(Op.applyAsLong(Dat[r], sum))) {
                        sum = Op.applyAsLong(Dat[r], sum);
                        r--;
                    }
                }
                return r + 1 - N;
            }
            sum = Op.applyAsLong(Dat[r], sum);
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

    // DEBUG

    private int indent = 6;

    public void setIndent(int newIndent) {
        this.indent = newIndent;
    }

    @Override
    public String toString() {
        return toSimpleString();
    }

    private long[] simulatePushAll() {
        long[] simDat = java.util.Arrays.copyOf(Dat, 2 * N);
        long[] simLaz = java.util.Arrays.copyOf(Laz, 2 * N);
        for (int k = 1; k < N; k++) {
            if (simLaz[k] == Id) continue;
            int lk = k << 1 | 0, rk = k << 1 | 1;
            simDat[lk] = Mapping.applyAsLong(simLaz[k], simDat[lk]);
            simDat[rk] = Mapping.applyAsLong(simLaz[k], simDat[rk]);
            if (lk < N) simLaz[lk] = Composition.applyAsLong(simLaz[k], simLaz[lk]);
            if (rk < N) simLaz[rk] = Composition.applyAsLong(simLaz[k], simLaz[rk]);
            simLaz[k] = Id;
        }
        return simDat;
    }

    public String toDetailedString() {
        return toDetailedString(1, 0, simulatePushAll());
    }

    private String toDetailedString(int k, int sp, long[] dat) {
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
        long[] dat = simulatePushAll();
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