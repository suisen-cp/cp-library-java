package lib.math.number;

import java.util.Arrays;

public class SumOfPowers {
    /**
     * Calc \sum_{i=0}^{n-1} i^k for k = 0, ..., m - 1 in O(m log m) time.
     * @param n maximum index
     * @param m the number of terms
     * @param mpf
     * @return [\sum_{i=0}^{n-1} i^k for k = 0, ..., m - 1]
     */
    public static long[] solve(long n, int m, ModPolynomialFactory mpf) {
        ModArithmetic ma = mpf.ma;
        ModFactorial mf = new ModFactorial(ma, m);
        long[] pow = ma.rangePower(ma.mod(n), m + 1);

        long[] f = new long[m + 1];
        long[] g = new long[m + 1];
        for (int i = 0; i <= m; i++) {
            f[i] = mf.facInv(i + 1);
            g[i] = ma.mul(f[i], pow[i + 1]);
        }
        ModPolynomialFactory.ModPolynomial h = mpf.create(g).div(mpf.create(f), m);

        long[] s = new long[m];
        Arrays.setAll(s, k -> ma.mul(h.getCoef(k), mf.fac(k)));
        return s;
    }
}
