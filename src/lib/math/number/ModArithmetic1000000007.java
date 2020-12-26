package lib.math.number;

import lib.base.Const;

public final class ModArithmetic1000000007 extends ModArithmetic {
    public static final ModArithmetic INSTANCE = new ModArithmetic1000000007();
    private ModArithmetic1000000007(){}
    public static final long MOD = Const.MOD1000000007;
    public long getMod() {return MOD;}
    public long mod(long a) {return (a %= MOD) < 0 ? a + MOD : a;}
    public long add(long a, long b) {return (a += b) >= MOD ? a - MOD : a;}
    public long sub(long a, long b) {return (a -= b) < 0 ? a + MOD : a;}
    public long mul(long a, long b) {return (a * b) % MOD;}
}
