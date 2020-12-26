package lib.math.number;

public final class ModArithmetic1 extends ModArithmetic {
    public static final ModArithmetic INSTANCE = new ModArithmetic1();
    private ModArithmetic1(){}
    public long getMod() {return 1;}
    public long mod(long a) {return 0;}
    public long add(long a, long b) {return a ^ b;}
    public long sub(long a, long b) {return a ^ b;}
    public long mul(long a, long b) {return a & b;}
}