package lib.math.number;

import java.util.Arrays;

public final class ConvolutionNTTPrime extends Convolution {
    public final int PRIMITIVE_ROOT;

    private final long[] SUM_E;
    private final long[] SUM_IE;

    public ConvolutionNTTPrime(ModArithmetic MA) {
        super(MA);
        this.PRIMITIVE_ROOT = primitiveRoot(MA);
        this.SUM_E = new long[30];
        this.SUM_IE = new long[30];
        buildSum();
    }
    public long[] convolution(long[] a, long[] b, int deg) {
        int n = Math.min(a.length, deg + 1);
        int m = Math.min(b.length, deg + 1);
        if (n <= 0 || m <= 0) return new long[]{0};
        if (Math.max(n, m) <= THRESHOLD_NAIVE_CONVOLUTION) {
            return convolutionNaive(a, b, n, m);
        }
        int z = 1 << ceilPow2(n + m - 1);
        a = Arrays.copyOf(a, z);
        b = Arrays.copyOf(b, z);
        butterfly(a);
        butterfly(b);
        for (int i = 0; i < z; i++) a[i] = MA.mul(a[i], b[i]);
        butterflyInv(a);
        a = Arrays.copyOf(a, n + m - 1);
        long iz = MA.inv(z);
        for (int i = 0; i < n + m - 1; i++) a[i] = MA.mul(a[i], iz);
        return a;
    }
    private void butterfly(long[] a) {
        int h = ceilPow2(a.length);
        for (int ph = 1; ph <= h; ph++) {
            int w = 1 << (ph - 1), p = 1 << (h - ph);
            long now = 1;
            for (int s = 0; s < w; s++) {
                int offset = s << (h - ph + 1);
                for (int i = 0; i < p; i++) {
                    long l = a[i + offset];
                    long r = MA.mul(a[i + offset + p], now);
                    a[i + offset] = MA.add(l, r);
                    a[i + offset + p] = MA.sub(l, r);
                }
                int x = Integer.numberOfTrailingZeros(~s);
                now = MA.mul(now, SUM_E[x]);
            }
        }
    }
    private void butterflyInv(long[] a) {
        int h = ceilPow2(a.length);
        for (int ph = h; ph >= 1; ph--) {
            int w = 1 << (ph - 1), p = 1 << (h - ph);
            long inow = 1;
            for (int s = 0; s < w; s++) {
                int offset = s << (h - ph + 1);
                for (int i = 0; i < p; i++) {
                    long l = a[i + offset];
                    long r = a[i + offset + p];
                    a[i + offset] = MA.add(l, r);
                    a[i + offset + p] = MA.mul(MA.sub(l, r), inow);
                }
                int x = Integer.numberOfTrailingZeros(~s);
                inow = MA.mul(inow, SUM_IE[x]);
            }
        }
    }
    private void buildSum() {
        int mod = (int) MA.getMod();
        int tlz = Integer.numberOfTrailingZeros(mod - 1);
        long e = MA.pow(PRIMITIVE_ROOT, (mod - 1) >> tlz);
        long ie = MA.inv(e);
        long[] es = new long[30];
        long[] ies = new long[30];
        for (int i = tlz; i >= 2; i--) {
            es[i - 2] = e;
            ies[i - 2] = ie;
            e = MA.mul(e, e);
            ie = MA.mul(ie, ie);
        }
        long now;
        now = 1;
        for (int i = 0; i < tlz - 2; i++) {
            SUM_E[i] = MA.mul(es[i], now);
            now = MA.mul(now, ies[i]);
        }
        now = 1;
        for (int i = 0; i < tlz - 2; i++) {
            SUM_IE[i] = MA.mul(ies[i], now);
            now = MA.mul(now, es[i]);
        }
    }
    private static int ceilPow2(int n) {
        int x = 0;
        while (1L << x < n) x++;
        return x;
    }
    private static int primitiveRoot(final ModArithmetic MA) {
        final int m = (int) MA.getMod();
        if (m == 2) return 1;
        if (m == 167772161) return 3;
        if (m == 469762049) return 3;
        if (m == 754974721) return 11;
        if (m == 998244353) return 3;
        int[] divs = new int[20];
        divs[0] = 2;
        int cnt = 1;
        int x = (m - 1) >> 1;
        while ((x & 1) == 0) x >>= 1;
        for (int i = 3; (long) i * i <= x; i += 2) {
            if (x % i == 0) {
                divs[cnt++] = i;
                while (x % i == 0) x /= i;
            }
        }
        if (x > 1) divs[cnt++] = x;
        Outer : for (int g = 2; ; g++) {
            for (int i = 0; i < cnt; i++) {
                if (MA.pow(g, (m - 1) / divs[i]) == 1) {
                    continue Outer;
                }
            }
            return g;
        }
    }
}
