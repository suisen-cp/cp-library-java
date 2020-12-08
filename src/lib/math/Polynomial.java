package lib.math;

import lib.geom.Complex;

public class Polynomial {
    public static final Polynomial ZERO = new Polynomial(new double[]{0});
    public static final Polynomial ONE = new Polynomial(new double[]{1});
    public final int n;
    private final double[] c;

    public Polynomial(double[] c, int n) {
        this.n = n;
        this.c = new double[n];
        System.arraycopy(c, 0, this.c, 0, Math.min(n, c.length));
    }

    public Polynomial(double[] c) {this(c, c.length);}

    public double getCoefficient(int degree) {
        return c[degree];
    }

    public double apply(double x) {
        double ret = c[n - 1];
        for (int i = n - 2; i >= 0; i--) ret = ret * x + c[i];
        return ret;
    }

    public static Polynomial cut(Polynomial f, int n) {
        return new Polynomial(f.c, n);
    }

    public static Polynomial cut(Polynomial f) {
        for (int i = f.n; i > 0; i--) if (Math.abs(f.c[i - 1]) > 1e-12) return new Polynomial(f.c, i);
        return ZERO;
    }

    public static Polynomial add(Polynomial f, Polynomial g) {
        double[] newPoly = new double[Math.max(f.n, g.n)];
        System.arraycopy(f.c, 0, newPoly, 0, f.n);
        for (int i = 0; i < g.n; i++) newPoly[i] += g.c[i];
        return new Polynomial(newPoly);
    }

    public static Polynomial sum(Polynomial... fs) {
        Polynomial ret = ZERO; for (Polynomial f : fs) ret = add(ret, f);
        return ret;
    }

    public static Polynomial sub(Polynomial f, Polynomial g) {
        double[] newPoly = new double[Math.max(f.n, g.n)];
        System.arraycopy(f.c, 0, newPoly, 0, f.n);
        for (int i = 0; i < g.n; i++) newPoly[i] -= g.c[i];
        return new Polynomial(newPoly);
    }

    public static Polynomial mul(Polynomial f, Polynomial g) {
        int newN = 1; while (newN < f.n + g.n) newN <<= 1;
        Complex[] fc = new Complex[newN], gc = new Complex[newN];
        for (int i = 0; i < f.n; i++) fc[i] = Complex.ofCartesian(f.c[i], 0);
        for (int i = f.n; i < newN; i++) fc[i] = Complex.ZERO;
        for (int i = 0; i < g.n; i++) gc[i] = Complex.ofCartesian(g.c[i], 0);
        for (int i = g.n; i < newN; i++) gc[i] = Complex.ZERO;
        dft(fc, false, newN); dft(gc, false, newN);
        for (int i = 0; i < newN; i++) fc[i] = fc[i].mul(gc[i]);
        dft(fc, true, newN);
        double[] fg = new double[newN];
        for (int i = 0; i < newN; i++) fg[i] = fc[i].x / newN;
        return cut(new Polynomial(fg));
    }

    private static void dft(Complex[] f, boolean inv, int n) {
        if (n == 1) return;
        int m = n >> 1;
        Complex[] f0 = new Complex[m], f1 = new Complex[m];
        for (int i = 0; i < m; i++) f0[i] = f[i << 1];
        for (int i = 0; i < m; i++) f1[i] = f[(i << 1) | 1];
        dft(f0, inv, m); dft(f1, inv, m);
        int sign = inv ? -1 : 1;
        for (int i = 0; i < m; i++) f[i] = f0[i].add(f1[i].rot(sign * 2. * i * Math.PI / n));
        for (int i = 0; i < m; i++) f[m + i] = f0[i].add(f1[i].rot(sign * 2. * (m + i) * Math.PI / n));
    }

    public static Polynomial prod(Polynomial... fs) {
        Polynomial ret = ONE; for (Polynomial f : fs) ret = cut(mul(ret, f));
        return ret;
    }
}