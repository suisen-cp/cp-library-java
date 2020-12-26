package lib.math.number;

public class LagrangeInterpolation {
    public static long lagrangeInterpolation(long[] y, long t, ModArithmetic MA) {
        int n = y.length - 1;
        t = MA.mod(t);
        if (0 <= t && t <= n) {
            return MA.mod(y[(int) t]);
        }
        long ret = 0;
        long[] l = new long[n + 1];
        long[] r = new long[n + 1];
        l[0] = r[n] = 1;
        for (int i = 0; i < n; i++) {
            l[i + 1] = MA.mul(l[i], t - i);
        }
        for (int i = n; i > 0; i--) {
            r[i - 1] = MA.mul(r[i], t - i);
        }
        long[] ifac = MA.factorialInv(n);
        for (int i = 0; i <= n; i++) {
            long v = MA.mul(MA.mod(y[i]), ifac[i], ifac[n - i], l[i], r[i]);
            if (((n - i) & 1) == 0) {
                ret += v;
            } else {
                ret -= v;
            }
        }
        return MA.mod(ret);
    }
}
