package lib.math.number;

public final class ModArithmeticBarrett extends ModArithmetic {
    public final long MOD;
    private static final long MASK = 0xffff_ffffL;
    private final long mh;
    private final long ml;
    public ModArithmeticBarrett(long mod) {
        this.MOD = mod;
        long a = (1L << 32) / mod, b = (1L << 32) % mod;
        long m = a * a * mod + 2 * a * b + (b * b) / mod;
        this.mh = m >>> 32;
        this.ml = m & MASK;
    }
    public long getMod() {return MOD;}
    public long mod(long a) {return (a %= MOD) < 0 ? a + MOD : a;}
    public long add(long a, long b) {return (a += b) >= MOD ? a - MOD : a;}
    public long sub(long a, long b) {return (a -= b) < 0 ? a + MOD : a;}
    public long mul(long a, long b) {return reduce(a * b);}
    public long reduce(long x) {
        long z = (x & MASK) * ml;
        z = (x & MASK) * mh + (x >>> 32) * ml + (z >>> 32);
        z = (x >>> 32) * mh + (z >>> 32);
        x -= z * MOD;
        return x < MOD ? x : x - MOD;
    }
}