package lib.geom;

/**
 * @author https://atcoder.jp/users/suisen
 */
public class Circle {
    public final Complex c;
    public final double r;
    public Circle(Complex c, double r) {
        this.c = c;
        this.r = r;
    }
    public boolean on(Complex a) {
        return on(a, this);
    }
    public boolean in(Complex a) {
        return in(a, this);
    }
    public boolean contains(Complex a) {
        return contains(a, this);
    }
    public static boolean on(Complex a, Circle c) {
        return Complex.sgn(a.sub(c.c).abs() - c.r) == 0;
    }
    public static boolean in(Complex a, Circle c) {
        return Complex.sgn(a.sub(c.c).abs() - c.r) < 0;
    }
    public static boolean contains(Complex a, Circle c) {
        return on(a, c) || in(a, c);
    }
}
