package lib.datastructure.longs;

import java.util.function.LongBinaryOperator;

/**
 * @author https://atcoder.jp/users/suisen
 * 
 * for Static Range Query. (LongBoundedSemiLattice)
 * build: O(N*logN). query: O(1).
 */
public final class LongSparseTable {
    private final int n;
    public final long[][] table;
    private final LongBinaryOperator op;
    private final long e;
    private final int[] floorExponent;
    
    /**
     * CAUTION: bounded-semilattice meats idempotent law and bond law. bsl is in monoid. (max, min, gcd, lcm, and, or, ...)
     * So, there exists some monoid s.t. Sparse Table cannot handle. (ex. addition, xor, multiplication)
     * @param a long array.
     * @param idempotentOperator (max, min, gcd, lcm, and, or, rewrite, ...) NOT (addition, xor, multiplication, ...)
     */
    public LongSparseTable(final long[] a, final LongBinaryOperator idempotentOperator, long e) {
        this.n = a.length;
        this.table = new long[n][];
        this.op = idempotentOperator;
        this.e = e;
        this.floorExponent = new int[n + 1];
        buildFloorExponent();
        buildTable(a);
    }

    public long query(final int i, final int j) {
        if (j <= i) return e;
        final int exp = floorExponent[j - i];
        return op.applyAsLong(table[i][exp], table[j - (1 << exp)][exp]);
    }

    private void buildFloorExponent() {
        for (int i = 1, pow = 0, exp = 1; i <= n; i++) {
            if ((i & -i) > exp) {
                pow++;
                exp <<= 1;
            }
            floorExponent[i] = pow;
        }
    }

    private void buildTable(final long[] a) {
        for (int i = 0; i < n; i++) {
            table[i] = new long[floorExponent[n - i] + 1];
            table[i][0] = a[i];
        }
        for (int i = n - 1; i >= 0; i--) {
            final long[] e = table[i];
            for (int j = 1, k = 1; j < e.length; j++, k <<= 1) {
                e[j] = op.applyAsLong(e[j - 1], table[i + k][j - 1]);
            }
        }
    }
}