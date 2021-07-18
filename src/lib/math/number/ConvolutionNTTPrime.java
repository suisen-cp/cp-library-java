package lib.math.number;

import java.util.Arrays;

public final class ConvolutionNTTPrime extends Convolution {
    public final int primitiveRoot;
    final int maxLog;

    private final long[] ws;
    private final long[] iws;

    public ConvolutionNTTPrime(ModArithmetic MA) {
        super(MA);
        this.primitiveRoot = MA.primitiveRoot();
        long m1 = MA.getMod() - 1;
        this.maxLog = Long.numberOfTrailingZeros(m1 & -m1);
        this.ws = new long[maxLog + 1];
        this.iws = new long[maxLog + 1];
        Arrays.setAll(ws, i -> MA.pow(primitiveRoot, m1 >> i));
        Arrays.setAll(iws, i -> MA.inv(ws[i]));
    }

    public long[] convolution(long[] a, long[] b, int deg) {
        int n = Math.min(deg + 1, a.length);
        int m = Math.min(deg + 1, b.length);
        if (n + m - 1 <= THRESHOLD_NAIVE_CONVOLUTION) return convolutionNaive(a, b, n, m);
        int log = ceilLog2(n + m - 1);
        assert log <= this.maxLog;
        int l = 1 << log;
        a = copyOfRange(a, n, l);
        b = copyOfRange(b, m, l);
        fft(a, log); fft(b, log);
        for (int i = 0; i < l; i++) a[i] = MA.mul(a[i], b[i]);
        ifft(a, log);
        long inv = MA.inv(l);
        for (int i = 0; i < l; i++) a[i] = MA.mul(a[i], inv);
        return a;
    }

    private void fft(long[] a, int log) {
        int n = a.length;
        for (int p = log; p > 0; p--) {
            int block = 1 << p;
            int halfBlock = block >> 1;
            long w = ws[p];
            for (int l = 0; l < n; l += block) {
                int m = l + halfBlock;
                long powW = 1;
                for (int i = 0; i < halfBlock; i++) {
                    long u = a[l + i], v = a[m + i];
                    a[l + i] = MA.mod(u + v);
                    a[m + i] = MA.mod((u - v) * powW);
                    powW = MA.mul(powW, w);
                }
            }
        }
    }

    private void ifft(long[] a, int log) {
        int n = a.length;
        for (int p = 1; p <= log; p++) {
            int block = 1 << p;
            int halfBlock = block >> 1;
            long iw = iws[p];
            for (int l = 0; l < n; l += block) {
                int m = l + halfBlock;
                long powIw = 1;
                for (int i = 0; i < halfBlock; i++) {
                    long u = a[l + i], v = MA.mul(a[m + i], powIw);
                    a[l + i] = MA.add(u, v);
                    a[m + i] = MA.sub(u, v);
                    powIw = MA.mul(powIw, iw);
                }
            }
        }
    }



    static long[] copyOfRange(long[] a, int toIndexExclusive, int newLength) {
        long[] b = new long[newLength];
        System.arraycopy(a, 0, b, 0, toIndexExclusive);
        return b;
    }
    static int ceilLog2(int n) {
        int res = 0;
        while (1 << res < n) ++res;
        return res;
    }
}
