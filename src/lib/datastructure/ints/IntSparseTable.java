package lib.datastructure.ints;

import java.util.function.IntBinaryOperator;

/**
 * @author https://atcoder.jp/users/suisen
 * 
 * for Static Range Query. (IntBoundedSemiLattice)
 * build: O(N*logN). query: O(1).
 */
public final class IntSparseTable {
    private final int n;
    public final int[][] table;
    private final IntBinaryOperator op;
    private final int e;
    private final int[] floorExponent;
    
    /**
     * CAUTION: bounded-semilattice meats idempotent law and bond law. bsl is in monoid. (max, min, gcd, lcm, and, or, ...)
     * So, there exists some monoid s.t. Sparse Table cannot handle. (ex. addition, xor, multiplication)
     * @param a int array.
     * @param idempotentOperator (max, min, gcd, lcm, and, or, rewrite, ...) NOT (addition, xor, multiplication, ...)
     */
    public IntSparseTable(final int[] a, final IntBinaryOperator idempotentOperator, int e) {
        this.n = a.length;
        this.table = new int[n][];
        this.op = idempotentOperator;
        this.e = e;
        this.floorExponent = new int[n + 1];
        buildFloorExponent();
        buildTable(a);
    }

    public int query(final int i, final int j) {
        if (j <= i) return e;
        final int exp = floorExponent[j - i];
        return op.applyAsInt(table[i][exp], table[j - (1 << exp)][exp]);
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

    private void buildTable(final int[] a) {
        for (int i = 0; i < n; i++) {
            table[i] = new int[floorExponent[n - i] + 1];
            table[i][0] = a[i];
        }
        for (int i = n - 1; i >= 0; i--) {
            final int[] e = table[i];
            for (int j = 1, k = 1; j < e.length; j++, k <<= 1) {
                e[j] = op.applyAsInt(e[j - 1], table[i + k][j - 1]);
            }
        }
    }
}