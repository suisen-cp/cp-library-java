package lib.util;

import java.util.function.LongBinaryOperator;

/**
 * @author https://atcoder.jp/users/suisen
 */
public final class Longs {
    private Longs(){}
    public static long max(long a, long b) {
        return Math.max(a, b);
    }
    public static long max(long a, long b, long c) {
        return Math.max(Math.max(a, b), c);
    }
    public static long max(long a, long b, long c, long d) {
        return Math.max(Math.max(Math.max(a, b), c), d);
    }
    public static long max(long a, long b, long c, long d, long e) {
        return Math.max(Math.max(Math.max(Math.max(a, b), c), d), e);
    }
    public static long max(long a, long b, long c, long d, long e, long f) {
        return Math.max(Math.max(Math.max(Math.max(Math.max(a, b), c), d), e), f);
    }
    public static long max(long a, long b, long c, long d, long e, long f, long g) {
        return Math.max(Math.max(Math.max(Math.max(Math.max(Math.max(a, b), c), d), e), f), g);
    }
    public static long max(long a, long b, long c, long d, long e, long f, long g, long h) {
        return Math.max(Math.max(Math.max(Math.max(Math.max(Math.max(Math.max(a, b), c), d), e), f), g), h);
    }
    public static long max(long a, long... vals) {
        long ret = a; for (long e : vals) ret = Math.max(ret, e);
        return ret;
    }
    public static long min(long a, long b) {
        return Math.min(a, b);
    }
    public static long min(long a, long b, long c) {
        return Math.min(Math.min(a, b), c);
    }
    public static long min(long a, long b, long c, long d) {
        return Math.min(Math.min(Math.min(a, b), c), d);
    }
    public static long min(long a, long b, long c, long d, long e) {
        return Math.min(Math.min(Math.min(Math.min(a, b), c), d), e);
    }
    public static long min(long a, long b, long c, long d, long e, long f) {
        return Math.min(Math.min(Math.min(Math.min(Math.min(a, b), c), d), e), f);
    }
    public static long min(long a, long b, long c, long d, long e, long f, long g) {
        return Math.min(Math.min(Math.min(Math.min(Math.min(Math.min(a, b), c), d), e), f), g);
    }
    public static long min(long a, long b, long c, long d, long e, long f, long g, long h) {
        return Math.min(Math.min(Math.min(Math.min(Math.min(Math.min(Math.min(a, b), c), d), e), f), g), h);
    }
    public static long min(long a, long... vals) {
        long ret = a; for (long e : vals) ret = Math.min(ret, e);
        return ret;
    }
    public static long fold(final LongBinaryOperator func, final long... a) {
        long ret = a[0]; for (int i = 1; i < a.length; i++) ret = func.applyAsLong(ret, a[i]);
        return ret;
    }
    public static boolean isPowerOfTwo(final long n) {return n != 0 && (-n & n) == n;}
    public static int ceilExponent(final long n) {return 63 - Long.numberOfLeadingZeros(n) + (isPowerOfTwo(n) ? 0 : 1);}
    public static int floorExponent(final long n) {return 63 - Long.numberOfLeadingZeros(n) + (n == 0 ? 1 : 0);}
    public static int ceilExponent(final long n, final int base) {
        if (base == 2) return ceilExponent(n);
        int i = 0;
        long m = 1;
        while (m < n) {
            i++;
            final long r = m * base;
            if ((m | base) >> 31 != 0 && r / base != m) break;
            m = r;
        }
        return i;
    }
    /**
     * @return ceil(a / b)
     */
    public static long cld(long a, long b) {
        if (a > 0 && b > 0) return (a - 1) / b + 1;
        if (a < 0 && b < 0) return (a + 1) / b + 1;
        return a / b;
    }
    /**
     * @return floor(a / b)
     */
    public static long fld(long a, long b) {
        if (a < 0 && b > 0) return (a + 1) / b - 1;
        if (a > 0 && b < 0) return (a - 1) / b - 1;
        return a / b;
    }
    /**
     * @return a * b <= c
     */
    public static boolean mulLeq(long a, long b, long c) {
        if (a > 0) return b <= fld(c, a);
        if (a < 0) return b >= cld(c, a);
        return c >= 0;
    }
    /**
     * @return a * b < c
     */
    public static boolean mulLt(long a, long b, long c) {
        if (a > 0) return b < cld(c, a);
        if (a < 0) return b > fld(c, a);
        return c > 0;
    }
    /**
     * @return a * b >= c
     */
    public static boolean mulGeq(long a, long b, long c) {
        if (a > 0) return b >= cld(c, a);
        if (a < 0) return b <= fld(c, a);
        return c <= 0;
    }
    /**
     * @return a * b > c
     */
    public static boolean mulGt(long a, long b, long c) {
        if (a > 0) return b > fld(c, a);
        if (a < 0) return b < cld(c, a);
        return c < 0;
    }
    /**
     * @return max{v | v <= a / b}
     */
    public static long leqDiv(long a, long b) {
        return fld(a, b);
    }
    /**
     * @return max{v | v < a / b}
     */
    public static long ltDiv(long a, long b) {
        return cld(a, b) - 1;
    }
    /**
     * @return min{v | v >= a / b}
     */
    public static long geqDiv(long a, long b) {
        return cld(a, b);
    }
    /**
     * @return min{v | v > a / b}
     */
    public static long gtDiv(long a, long b) {
        return fld(a, b) + 1;
    }
    public static String join(final String sep, final long... a) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.length; i++) {
            sb.append(a[i]);
            if (i < a.length - 1) sb.append(sep);
        }
        return sb.toString();
    }
}