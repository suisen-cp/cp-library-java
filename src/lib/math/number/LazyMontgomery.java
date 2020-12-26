package lib.math.number;

public final class LazyMontgomery {
    public final ModArithmetic MA;
    public final long MOD;
    private final long negInv;
    private final long r2, r3;

    public LazyMontgomery(long mod) {
        if (mod % 2 == 0) throw new AssertionError();
        this.MOD = mod;
        this.MA = new ModArithmeticDynamic(mod);
        long inv = 0;
        long s = 1, t = 0;
        for (int i = 0; i < 32; i++) {
            if ((t & 1) == 0) {
                t += mod;
                inv += s;
            }
            t >>= 1;
            s <<= 1;
        }
        long r = (1L << 32) % mod;
        this.negInv = inv;
        this.r2 = (r * r) % mod;
        this.r3 = (r2 * r) % mod;
    }
    public long addR(long x) {
        return remR(x * r2);
    }
    public long remR(long x) {
        x = (x + ((x * negInv) & 0xffff_ffffL) * MOD) >>> 32;
        return x < MOD ? x : x - MOD;
    }
    public long mul(long a, long b) {
        return remR(a * b);
    }
    public long inv(long a) {
        long b = MOD;
        long u = 1, v = 0;
        while (b >= 1) {
            long t = a / b;
            a -= t * b;
            long tmp1 = a; a = b; b = tmp1;
            u -= t * v;
            long tmp2 = u; u = v; v = tmp2;
        }
        if (a != 1) throw new ArithmeticException("divide by zero");
        if (u < 0) u += MOD;
        return remR(u * r3);
    }
    public long pow(long a, long b) {
        long pow = addR(1);
        for (; b > 0; b >>= 1) {
            if ((b & 1) == 1) pow = mul(pow, a);
            a = mul(a, a);
        }
        return pow;
    }
    public static void main(String[] args) {
        int mod = 998244353;
        LazyMontgomery mont = new LazyMontgomery(mod);
        for (long x = mod - 1; x >= mod - 10000000; x--) {
            long ix = mont.remR(mont.inv(mont.addR(x)));
            assert (x * ix) % mod == 1;
        }
    }
}
