package lib.util;

public final class IntLinearFunction {
    public static final IntLinearFunction I = new IntLinearFunction(1, 0);
    public final int a, b;
    public IntLinearFunction(final int a, final int b) {this.a = a; this.b = b;}
    public int apply(int x) {return a * x + b;}
    public IntLinearFunction compose(IntLinearFunction other) {
        return new IntLinearFunction(a * other.a, a * other.b + b);
    }
}