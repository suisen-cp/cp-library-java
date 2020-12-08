package lib.util.array;

import java.util.Arrays;
import java.util.function.DoubleUnaryOperator;
import java.util.function.IntToDoubleFunction;

/**
 * @author https://atcoder.jp/users/suisen
 */
public final class DoubleArrayFactory {
    private DoubleArrayFactory() {}
    public static double[] filled(final int n, final double init) {
        final double[] ret = new double[n];
        Arrays.fill(ret, init);
        return ret;
    }
    public static double[][] filled(final int n, final int m, final double init) {
        final double[][] ret = new double[n][m];
        for (int i = 0; i < n; i++)  Arrays.fill(ret[i], init);
        return ret;
    }
    public static double[] indexToDouble(final int n, final IntToDoubleFunction f) {
        final double[] a = new double[n];
        Arrays.setAll(a, f);
        return a;
    }
    public static double[] map(final double[] a, final DoubleUnaryOperator f) {
        final double[] b = new double[a.length];
        Arrays.setAll(b, i -> f.applyAsDouble(a[i]));
        return b;
    }
}