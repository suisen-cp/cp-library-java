package lib.util;

public final class LongLinearFunction {
    public static final LongLinearFunction I = new LongLinearFunction(1, 0);
    public final long a, b;
    public LongLinearFunction(final long a, final long b) {this.a = a; this.b = b;}
    public long apply(long x) {return a * x + b;}
    public LongLinearFunction compose(LongLinearFunction other) {
        return new LongLinearFunction(a * other.a, a * other.b + b);
    }
    public LongLinearFunction add(LongLinearFunction other) {
        return new LongLinearFunction(a + other.a, b + other.b);
    }
    public String toString() {return String.format("%dx+%d", a, b);}
}