package lib.math.number;

public class ModArithmeticDynamic extends ModArithmetic {
    public final long MOD;
    public ModArithmeticDynamic(long mod) {this.MOD = mod;}
    public long getMod() {return MOD;}
    public long mod(long a) {return (a %= MOD) < 0 ? a + MOD : a;}
    public long add(long a, long b) {return (a += b) >= MOD ? a - MOD : a;}
    public long sub(long a, long b) {return (a -= b) < 0 ? a + MOD : a;}
    public long mul(long a, long b) {return (a * b) % MOD;}
}
