package lib.util;

public final class DoubleLinearFunction {
    public static final DoubleLinearFunction I = new DoubleLinearFunction(1, 0);
    public final double a, b;
    public DoubleLinearFunction(final double a, final double b) {this.a = a; this.b = b;}
    public double apply(double x) {return a * x + b;}
    public DoubleLinearFunction compose(DoubleLinearFunction other) {
        return new DoubleLinearFunction(a * other.a, a * other.b + b);
    }
    public DoubleLinearFunction inverse() {
        return new DoubleLinearFunction(1 / a, -b / a);
    }
}