package lib.math.number;

import java.util.Arrays;

public class Combinatrics {
    /**
     * @return (x - l) * (x - (l + 1)) * ... (x * (r - 1))
     */
    public static long[] perm(int l, int r, ModPolynomialFactory mpf) {
        if (l == r) {
            return new long[]{1};
        }
        return internalPerm(l, r, mpf).getCoefs();
    }
    
    private static ModPolynomialFactory.ModPolynomial internalPerm(int l, int r, ModPolynomialFactory mpf) {
        if (r - l == 1) {
            return mpf.create(new long[]{-l, 1}, 1);
        } else if (r - l == 2) {
            return mpf.create(new long[]{(long) l * (l + 1), - (2 * l + 1), 1}, 2);
        }
        int m = (l + r) >> 1;
        return internalPerm(l, m, mpf).mul(internalPerm(m, r, mpf));
    }

    public static long[] stirlingNumber1(int n, ModPolynomialFactory mpf) {
        return perm(0, n, mpf);
    }

    public static long[] stirlingNumber2(int n, ModPolynomialFactory mpf) {
        if (n == 0) {
            return new long[]{1};
        }
        int[] div = Eratosthenes.solve(n).divisors();
        long[] pow = new long[n + 1];
        pow[0] = 0; pow[1] = 1;
        for (int i = 2; i <= n; i++) {
            pow[i] = div[i] == i ? mpf.ma.pow(i, n) : mpf.ma.mul(pow[i / div[i]], pow[div[i]]);
        }
        long[] inv = mpf.ma.factorialInv(n);
        long[] a = new long[n + 1];
        long[] b = new long[n + 1];
        for (int i = 0; i <= n; i++) {
            a[i] = mpf.ma.mul(pow[i], inv[i]);
            b[i] = (i & 1) == 0 ? inv[i] : -inv[i];
        }
        return mpf.create(a).mul(mpf.create(b), n).getCoefs();
    }

    public static long[] bernoulliNumber(int n, ModPolynomialFactory mpf) {
        long[] c = mpf.create(Arrays.copyOfRange(mpf.ma.factorialInv(n + 1), 1, n + 2)).inv(n).getCoefs();
        long fac = 1;
        for (int i = 2; i <= n; i++) {
            fac = mpf.ma.mul(fac, i);
            c[i] = mpf.ma.mul(c[i], fac);
        }
        return mpf.create(c).getCoefs();
    }

    public static long[] bellNumber(int n, ModPolynomialFactory mpf) {
        long[] bell = stirlingNumber2(n, mpf);
        for (int i = 1; i <= n; i++) {
            bell[i] = mpf.ma.add(bell[i], bell[i - 1]);
        }
        return bell;
    }

    public static long[] partitionNumber(int n, ModPolynomialFactory mpf) {
        long[] inv = new long[n + 1];
        inv[0] = 1;
        for (int i = 1, k = 1; k <= n; i++) {
            if ((i & 1) == 0) {
                inv[k]++;
            } else {
                inv[k]--;
            }
            k += 3 * i + 1;
        }
        for (int i = 1, k = 2; k <= n; i++) {
            if ((i & 1) == 0) {
                inv[k]++;
            } else {
                inv[k]--;
            }
            k += 3 * i + 2;
        }
        return mpf.create(inv).inv(n).getCoefs();
    }

    public static long[] subsetSum(int n, int[] s, ModPolynomialFactory mpf) {
        int[] a = new int[n + 1];
        for (int e : s) {
            if (e <= 0) throw new AssertionError();
            if (e <= n) a[e]++;
        }
        long[] inv = mpf.ma.rangeInv(n);
        long[] log = new long[n + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; i * j <= n; j++) {
                if ((j & 1) == 0) {
                    log[i * j] -= inv[j] * a[i];
                } else {
                    log[i * j] += inv[j] * a[i];
                }
            }
        }
        return mpf.create(log).exp(n).getCoefs();
    }
}
