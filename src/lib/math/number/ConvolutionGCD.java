package lib.math.number;

import java.util.function.LongBinaryOperator;
import java.util.function.LongUnaryOperator;

public class ConvolutionGCD {
    public static long[] solve(long[] f, long[] g, long zero, LongBinaryOperator add, LongBinaryOperator mul, LongUnaryOperator addinv, int[] primes) {
        if (f.length != g.length) throw new AssertionError();
        int n = f.length;
        long[] f2 = f.clone(), g2 = g.clone();
        for (int p : primes) {
            int max = (n - 1) / p;
            for (int i = max; i >= 1; i--) {
                int ip = i * p;
                f2[i] = add.applyAsLong(f2[i], f2[ip]);
                g2[i] = add.applyAsLong(g2[i], g2[ip]);
            }
        }
        for (int i = 0; i < n; i++) {
            f2[i] = add.applyAsLong(zero, mul.applyAsLong(f2[i], g2[i]));
        }
        for (int p : primes) {
            int max = (n - 1) / p;
            for (int i = 1; i <= max; i++) {
                int ip = i * p;
                f2[i] = add.applyAsLong(f2[i], addinv.applyAsLong(f2[ip]));
            }
        }
        for (int i = 1; i < n; i++) {
            f2[i] = add.applyAsLong(f2[i], mul.applyAsLong(f[i], g[0]));
            f2[i] = add.applyAsLong(f2[i], mul.applyAsLong(f[0], g[i]));
        }
        return f2;
    }

    public static long[] solve(long[] f, long[] g, long zero, LongBinaryOperator add, LongBinaryOperator mul, LongUnaryOperator addinv) {
        return solve(f, g, zero, add, mul, addinv, Eratosthenes.solve(f.length).primes());
    }

    public static long[] solve(long[] f, long[] g, ModArithmetic MA, int[] primes) {
        if (f.length != g.length) throw new AssertionError();
        int n = f.length;
        long[] f2 = f.clone(), g2 = g.clone();
        for (int p : primes) {
            int max = (n - 1) / p;
            for (int i = max; i >= 1; i--) {
                int ip = i * p;
                f2[i] = MA.add(f2[i], f2[ip]);
                g2[i] = MA.add(g2[i], g2[ip]);
            }
        }
        for (int i = 0; i < n; i++) {
            f2[i] = MA.mul(f2[i], g2[i]);
        }
        for (int p : primes) {
            int max = (n - 1) / p;
            for (int i = 1; i <= max; i++) {
                int ip = i * p;
                f2[i] = MA.sub(f2[i], f2[ip]);
            }
        }
        for (int i = 1; i < n; i++) {
            f2[i] = MA.add(f2[i], MA.mul(f[i], g[0]), MA.mul(f[0], g[i]));
        }
        return f2;
    }

    public static long[] solve(long[] f, long[] g, ModArithmetic MA) {
        return solve(f, g, MA, Eratosthenes.solve(f.length).primes());
    }

    public static long[] solve(long[] f, long[] g, int[] primes) {
        if (f.length != g.length) throw new AssertionError();
        int n = f.length;
        long[] f2 = f.clone(), g2 = g.clone();
        for (int p : primes) {
            int max = (n - 1) / p;
            for (int i = max; i >= 1; i--) {
                int ip = i * p;
                f2[i] += f2[ip];
                g2[i] += g2[ip];
            }
        }
        for (int i = 0; i < n; i++) {
            f2[i] *= g2[i];
        }
        for (int p : primes) {
            int max = (n - 1) / p;
            for (int i = 1; i <= max; i++) {
                int ip = i * p;
                f2[i] = f2[i] - f2[ip];
            }
        }
        for (int i = 1; i < n; i++) {
            f2[i] += f[i] * g[0] + f[0] * g[i];
        }
        return f2;
    }

    public static long[] solve(long[] f, long[] g) {
        return solve(f, g, Eratosthenes.solve(f.length).primes());
    }
}
