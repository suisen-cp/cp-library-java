package lib.util;

import java.util.function.IntBinaryOperator;

/**
 * @author https://atcoder.jp/users/suisen
 */
public final class Ints {
    private Ints(){}
    public static int max(int a, int b) {
        return Math.max(a, b);
    }
    public static int max(int a, int b, int c) {
        return Math.max(Math.max(a, b), c);
    }
    public static int max(int a, int b, int c, int d) {
        return Math.max(Math.max(Math.max(a, b), c), d);
    }
    public static int max(int a, int b, int c, int d, int e) {
        return Math.max(Math.max(Math.max(Math.max(a, b), c), d), e);
    }
    public static int max(int a, int b, int c, int d, int e, int f) {
        return Math.max(Math.max(Math.max(Math.max(Math.max(a, b), c), d), e), f);
    }
    public static int max(int a, int b, int c, int d, int e, int f, int g) {
        return Math.max(Math.max(Math.max(Math.max(Math.max(Math.max(a, b), c), d), e), f), g);
    }
    public static int max(int a, int b, int c, int d, int e, int f, int g, int h) {
        return Math.max(Math.max(Math.max(Math.max(Math.max(Math.max(Math.max(a, b), c), d), e), f), g), h);
    }
    public static int max(int a, int... vals) {
        int ret = a; for (int e : vals) ret = Math.max(ret, e);
        return ret;
    }
    public static int min(int a, int b) {
        return Math.min(a, b);
    }
    public static int min(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }
    public static int min(int a, int b, int c, int d) {
        return Math.min(Math.min(Math.min(a, b), c), d);
    }
    public static int min(int a, int b, int c, int d, int e) {
        return Math.min(Math.min(Math.min(Math.min(a, b), c), d), e);
    }
    public static int min(int a, int b, int c, int d, int e, int f) {
        return Math.min(Math.min(Math.min(Math.min(Math.min(a, b), c), d), e), f);
    }
    public static int min(int a, int b, int c, int d, int e, int f, int g) {
        return Math.min(Math.min(Math.min(Math.min(Math.min(Math.min(a, b), c), d), e), f), g);
    }
    public static int min(int a, int b, int c, int d, int e, int f, int g, int h) {
        return Math.min(Math.min(Math.min(Math.min(Math.min(Math.min(Math.min(a, b), c), d), e), f), g), h);
    }
    public static int min(int a, int... vals) {
        int ret = a; for (int e : vals) ret = Math.min(ret, e);
        return ret;
    }
    public static int fold(final IntBinaryOperator func, final int... a) {
        int ret = a[0]; for (int i = 1; i < a.length; i++) ret = func.applyAsInt(ret, a[i]);
        return ret;
    }
    public static boolean isPowerOfTwo(final int n) {return n != 0 && (-n & n) == n;}
    public static int ceilExponent(final int n) {return 31 - Integer.numberOfLeadingZeros(n) + (isPowerOfTwo(n) ? 0 : 1);}
    public static int floorExponent(final int n) {return 31 - Integer.numberOfLeadingZeros(n) + (n == 0 ? 1 : 0);}
    /**
     * @return ceil(a / b)
     */
    public static int cld(int a, int b) {
        if (a > 0 && b > 0) return (a - 1) / b + 1;
        if (a < 0 && b < 0) return (a + 1) / b + 1;
        return a / b;
    }
    /**
     * @return floor(a / b)
     */
    public static int fld(int a, int b) {
        if (a < 0 && b > 0) return (a + 1) / b - 1;
        if (a > 0 && b < 0) return (a - 1) / b - 1;
        return a / b;
    }
    /**
     * @return a * b <= c
     */
    public static boolean mulLeq(int a, int b, int c) {
        if (a > 0) return b <= fld(c, a);
        if (a < 0) return b >= cld(c, a);
        return c >= 0;
    }
    /**
     * @return a * b < c
     */
    public static boolean mulLt(int a, int b, int c) {
        if (a > 0) return b < cld(c, a);
        if (a < 0) return b > fld(c, a);
        return c > 0;
    }
    /**
     * @return a * b >= c
     */
    public static boolean mulGeq(int a, int b, int c) {
        if (a > 0) return b >= cld(c, a);
        if (a < 0) return b <= fld(c, a);
        return c <= 0;
    }
    /**
     * @return a * b > c
     */
    public static boolean mulGt(int a, int b, int c) {
        if (a > 0) return b > fld(c, a);
        if (a < 0) return b < cld(c, a);
        return c < 0;
    }
    /**
     * @return max{v | v <= a / b}
     */
    public static int leqDiv(int a, int b) {
        return fld(a, b);
    }
    /**
     * @return max{v | v < a / b}
     */
    public static int ltDiv(int a, int b) {
        return cld(a, b) - 1;
    }
    /**
     * @return min{v | v >= a / b}
     */
    public static int geqDiv(int a, int b) {
        return cld(a, b);
    }
    /**
     * @return min{v | v > a / b}
     */
    public static int gtDiv(int a, int b) {
        return fld(a, b) + 1;
    }
    public static String join(final String sep, final int... a) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.length; i++) {
            sb.append(a[i]);
            if (i < a.length - 1)  sb.append(sep);
        }
        return sb.toString();
    }
}