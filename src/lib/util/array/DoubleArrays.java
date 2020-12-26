package lib.util.array;

import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoublePredicate;
import java.util.function.DoubleUnaryOperator;

/**
 * @author https://atcoder.jp/users/suisen
 * 
 * 1. DESTRUCTIVE methods for double arrays.
 * 2. methods that receives arrays and return some results (except for double arrays).
 */
public final class DoubleArrays {
    private DoubleArrays(){}
    public static void swap(final double[] a, final int u, final int v) {
        final double tmp = a[u]; a[u] = a[v]; a[v] = tmp;
    }
    public static void reverse(final double[] a, int begin, int end) {
        while (end - begin > 1) swap(a, begin++, --end);
    }
    public static void reverse(final double[] a) {reverse(a, 0, a.length);}
    public static void sortDescending(final double[] a) {Arrays.sort(a); reverse(a);}
    public static double fold(final double[] a, final DoubleBinaryOperator op) {
        double ret = a[0]; for (int i = 1; i < a.length; i++) ret = op.applyAsDouble(ret, a[i]);
        return ret;
    }
    public static void map(final double[] a, final DoubleUnaryOperator op) {Arrays.setAll(a, i -> op.applyAsDouble(a[i]));}
    public static int filter(final double[] src, final double[] dst, final DoublePredicate p) {
        int idx = 0;
        for (final double e : src) if (p.test(e)) dst[idx++] = e;
        return idx;
    }
    public static int filterIndex(final double[] dst, final int beginIndex, final int endIndex, final DoublePredicate p) {
        int idx = 0;
        for (int i = beginIndex; i < endIndex; i++) if (p.test(i)) dst[idx++] = i;
        return idx;
    }
    public static int filterIndex(final double[] dst, final int endIndex, final DoublePredicate p) {
        return filterIndex(dst, 0, endIndex, p);
    }
    public static void accumulate(final double[] a, final DoubleBinaryOperator op) {
        for (int i = 1; i < a.length; i++) a[i] = op.applyAsDouble(a[i - 1], a[i]);
    }
    public static void accumulate(final double[] a) {
        for (int i = 1; i < a.length; i++) a[i] += a[i - 1];
    }
    public static void permute(double[] a, int[] p) {
        int n = p.length;
        boolean[] settled = new boolean[n];
        for (int i = 0; i < n; i++) {
            for (int j = i; !settled[j]; j = p[j]) {
                if (p[j] == i) {
                    settled[j] = true;
                    break;
                }
                swap(a, j, p[j]);
                settled[j] = true;
            }
        }
    }
    public static int compare(final double[] a, final double[] b) {
        for (int i = 0; i < a.length; i++) {
            if (i >= b.length) return -1;
            if (a[i] > b[i]) return 1;
            if (a[i] < b[i]) return -1;
        }
        return a.length < b.length ? 1 : 0;
    }
    public static boolean equals(final double[] a, final double[] b) {return compare(a, b) == 0;}
    public static String join(final double[] a, final String sep) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.length; i++) {
            sb.append(a[i]);
            if (i < a.length - 1) sb.append(sep);
        }
        return sb.toString();
    }
    public static String joinWithPrefixAndSuffix(final double[] a, final IntFunction<String> idxToPre, final IntFunction<String> idxToSuf, final String sep) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.length; i++) {
            sb.append(idxToPre.apply(i)).append(a[i]).append(idxToSuf.apply(i));
            if (i < a.length - 1) sb.append(sep);
        }
        return sb.toString();
    }
}