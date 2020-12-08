package lib.math.number;

import java.util.HashMap;
import lib.math.MathUtil;

/**
 * @author https://atcoder.jp/users/suisen
 */
public class DiscreteLogarithm {
    private static long coprimeLog(long x, long y, ModArithmetic ma, long ix) {
        long a = MathUtil.ceilSqrt(ma.getMod());
        HashMap<Long, Long> memo = new HashMap<>();
        for (long i = 0, powx = 1; i < a; i++) {
            memo.putIfAbsent(powx, i);
            powx = ma.mul(powx, x);
        }
        long inv = ma.pow(ix, a);
        for (long i = 0, invpow = y; i < a; i++) {
            long b = memo.getOrDefault(invpow, -1L);
            if (b >= 0) return a * i + b;
            invpow = ma.mul(invpow, inv);
        }
        return -1;
    }
    public static long log(long x, long y, long m) {
        ModArithmetic ma = new ModArithmeticBarrett(m);
        x = ma.mod(x);
        y = ma.mod(y);
        if (m == 1 || y == 1) return 0;
        long[] gi = ma.gcdInv(x);
        long g = gi[0], ix = gi[1];
        if (g != 1) {
            m /= g;
            if (y % g != 0) return -1;
            long log = log(x, (y / g) * ix, m);
            if (log < 0) return -1;
            return log + 1;
        }
        return coprimeLog(x, y, ma, ix);
    }
}