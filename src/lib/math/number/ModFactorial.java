package lib.math.number;

import java.util.Arrays;

public final class ModFactorial {
    private int max;
    private long[] fac;
    private long[] facInv;
    public final ModArithmetic MA;
    public ModFactorial(ModArithmetic MA, int initialMax) {
        this.MA = MA;
        this.max = Math.max(initialMax, 1);
        this.fac    = new long[max + 1];
        this.facInv = new long[max + 1];
        fac[0] = 1;
        build(1, max);
    }
    public long fac(int n) {
        ensureSize(n);
        return fac[n];
    }
    public long facInv(int n) {
        ensureSize(n);
        return facInv[n];
    }
    public long comb(int n, int r) {
        if (r < 0 || r > n) return 0;
        ensureSize(n);
        return MA.mul(fac[n], facInv[r], facInv[n - r]);
    }
    public long perm(int n, int r) {
        if (r < 0 || r > n) return 0;
        ensureSize(n);
        return MA.mul(fac[n], facInv[n - r]);
    }
    private void build(int min, int max) {
        for (int i = min; i <= max; i++) fac[i] = MA.mul(fac[i - 1], i);
        facInv[max] = MA.inv(fac[max]);
        for (int i = max; i >= min; i--) facInv[i - 1] = MA.mul(facInv[i], i);
    }
    private void ensureSize(int n) {
        if (n <= max) return;
        int newMax = Math.max(max << 1, n);
        fac    = Arrays.copyOf(fac   , newMax + 1);
        facInv = java.util.Arrays.copyOf(facInv, newMax + 1);
        build(max + 1, max = newMax);
    }
}
