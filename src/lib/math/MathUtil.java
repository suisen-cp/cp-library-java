package lib.math;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

import lib.util.ext_std.MapUtil;

/**
 * @author https://atcoder.jp/users/suisen
 */
public final class MathUtil {
    private MathUtil(){}

    public static void forEachPrimeFactorPower(long n, Consumer<LongPrimePower> con) {
        for (long p = 2; p * p <= n; p++) {
            int q = 0;
            while (n % p == 0) {
                n /= p;
                q++;
            }
            if (q > 0) con.accept(new LongPrimePower(p, q));
        }
        if (n > 1) con.accept(new LongPrimePower(n, 1));
    }
    public static ArrayList<LongPrimePower> factorizeToList(long n) {
        ArrayList<LongPrimePower> factors = new ArrayList<>();
        forEachPrimeFactorPower(n, factors::add);
        return factors;
    }
    public static HashMap<Long, Integer> factorizeToMap(long n) {
        HashMap<Long, Integer> factors = new HashMap<>();
        forEachPrimeFactorPower(n, pp -> factors.put(pp.prime, pp.index));
        return factors;
    }
    public static void forEachDivisor(long n, LongConsumer con) {
        for (long i = 1; i * i <= n; i++) {
            if (n % i == 0) {
                con.accept(i);
                long j = n / i;
                if (i != j) con.accept(j);
            }
        }
    }
    public static ArrayList<Long> divisors(long n) {
        ArrayList<Long> div = new ArrayList<>();
        forEachDivisor(n, div::add);
        return div;
    }
    public static HashMap<Long, Integer> factorizedLCM(long a, long b) {
        return MapUtil.merge(factorizeToMap(a), factorizeToMap(b), Integer::max);
    }
    public static HashMap<Long, Integer> factorizedLCM(long... xs) {
        HashMap<Long, Integer> lcm = new HashMap<>();
        for (long x : xs) lcm = MapUtil.merge(lcm, factorizeToMap(x), Integer::max);
        return lcm;
    }
    public static long lcm(long a, long b) {
        return (a / gcd(a, b)) * b;
    }
    public static long gcd(long a, long b) {
        if ((a = Math.abs(a)) < (b = Math.abs(b))) {
            long tmp = a; a = b; b = tmp;
        }
        if (a == 0 || b == 0) return a ^ b;
        for (long r = a % b; r != 0; r = a % b) {
            a = b; b = r;
        }
        return b;
    }
    public static long gcd(long... xs) {
        long gcd = 0;
        for (long x : xs) gcd = gcd(gcd, x);
        return gcd;
    }
    /**
     * Return one of the solutions to {@code ax+by=gcd(a, b)}.
     * 
     * @return {@code x}, {@code y}, {@code gcd(a, b)}.
     * @param a a of ax+by=gcd(a, b).
     * @param b b of ax+by=gcd(a, b).
     */
    public static long[] extGCD(long a, long b) {
        long s = a, sx = 1, sy = 0; // ax + by = s
        long t = b, tx = 0, ty = 1; // ax + by = t
        for (long tmp, u, ux, uy; s % t != 0;) {
            // u: ax + by = s % t
            tmp = s / t;
            u  = s  - t  * tmp; s  = t ; t  = u ;
            ux = sx - tx * tmp; sx = tx; tx = ux;
            uy = sy - ty * tmp; sy = ty; ty = uy;
        }
        return new long[]{tx, ty, a * tx + b * ty};
    }
    public static long eulerPhi(long n) {
        for (LongPrimePower pp : factorizeToList(n)) {
            long p = pp.prime;
            n = (n / p) * (p - 1);
        }
        return n;
    }
    public static long comb(long n, long r) {
        if (n < r) return 0;
        r = Math.min(r, n - r);
        long res = 1; for (long d = 1; d <= r; d++) {res *= n--; res /= d;}
        return res;
    }
    public static long ceilSqrt(long n) {
        long l = -1, r = n;
        while (r - l > 1) {
            long m = (r + l) >> 1;
            if (m * m >= n) r = m;
            else l = m;
        }
        return r;
    }
    public static long floorSqrt(long n) {
        long l = 0, r = n + 1;
        while (r - l > 1) {
            long m = (r + l) >> 1;
            if (m * m > n) r = m;
            else l = m;
        }
        return l;
    }
    public static long floorSum(long n, long m, long a, long b){
        long ans = 0;
        if (a >= m) {
            ans += (n - 1) * n * (a / m) / 2;
            a %= m;
        }
        if (b >= m) {
            ans += n * (b / m);
            b %= m;
        }
        long ymax = (a * n + b) / m;
        long xmax = ymax * m - b;
        if (ymax == 0) return ans;
        ans += (n - (xmax + a - 1) / a) * ymax;
        ans += floorSum(ymax, a, m, (a - xmax % a) % a);
        return ans;
    }
}
