package lib.math.number;

import lib.base.Const;

public abstract class Convolution {
    static final int THRESHOLD_NAIVE_CONVOLUTION = 150;
    public final ModArithmetic MA;
    Convolution(ModArithmetic MA) {this.MA = MA;}
    public abstract long[] convolution(long[] a, long[] b, int deg);
    public final long[] convolution(long[] a, long[] b) {return convolution(a, b, Const.IINF);}
    final long[] convolutionNaive(long[] a, long[] b, int n, int m) {
        int k = n + m - 1;
        long[] ret = new long[k];
        for (int i = 0; i < n; i++) for (int j = 0; j < m; j++) ret[i + j] += MA.mul(a[i], b[j]);
        for (int i = 0; i < k; i++) ret[i] = MA.mod(ret[i]);
        return ret;
    }
}
