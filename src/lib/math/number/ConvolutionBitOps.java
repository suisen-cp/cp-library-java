package lib.math.number;

import java.util.function.LongBinaryOperator;
import java.util.function.LongUnaryOperator;

public class ConvolutionBitOps {
    public static void subsetFZT(long[] f, int length, LongBinaryOperator op) {
        final int n = 1 << length;
        for (int i = 1; i < n; i <<= 1) for (int j = 0; j < n; j++) {
            if ((j & i) == 0) f[j | i] = op.applyAsLong(f[j | i], f[j]);
        }
    }

    public static void supsetFZT(long[] f, int length, LongBinaryOperator op) {
        final int n = 1 << length;
        for (int i = 1; i < n; i <<= 1) for (int j = 0; j < n; j++) {
            if ((j & i) == 0) f[j] = op.applyAsLong(f[j], f[j | i]);
        }
    }

    public static void subsetFMT(long[] f, int length, LongBinaryOperator op, LongUnaryOperator inv) {
        final int n = 1 << length;
        for (int i = 1; i < n; i <<= 1) for (int j = 0; j < n; j++) {
            if ((j & i) == 0) f[j | i] = op.applyAsLong(f[j | i], inv.applyAsLong(f[j]));
        }
    }

    public static void supsetFMT(long[] f, int length, LongBinaryOperator op, LongUnaryOperator inv) {
        final int n = 1 << length;
        for (int i = 1; i < n; i <<= 1) for (int j = 0; j < n; j++) {
            if ((j & i) == 0) f[j] = op.applyAsLong(f[j], inv.applyAsLong(f[j | i]));
        }
    }

    public static long[] convolutionOR(long[] f, long[] g, int length, long zero, LongBinaryOperator add, LongBinaryOperator mul, LongUnaryOperator addinv) {
        if (f.length != 1 << length || g.length != 1 << length) {
            throw new AssertionError();
        }
        long[] f2 = f.clone();
        long[] g2 = g.clone();
        subsetFZT(f2, length, add);
        subsetFZT(g2, length, add);
        for (int i = 0; i < f.length; i++) {
            f2[i] = mul.applyAsLong(f2[i], g2[i]);
        }
        subsetFMT(f2, length, add, addinv);
        return f2;
    }

    public static long[] convolutionAND(long[] f, long[] g, int length, long zero, LongBinaryOperator add, LongBinaryOperator mul, LongUnaryOperator addinv) {
        if (f.length != 1 << length || g.length != 1 << length) {
            throw new AssertionError();
        }
        long[] f2 = f.clone();
        long[] g2 = g.clone();
        supsetFZT(f2, length, add);
        supsetFZT(g2, length, add);
        for (int i = 0; i < f.length; i++) {
            f2[i] = mul.applyAsLong(f2[i], g2[i]);
        }
        supsetFMT(f2, length, add, addinv);
        return f2;
    }
}
