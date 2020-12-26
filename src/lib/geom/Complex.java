package lib.geom;

import java.io.Serializable;
import java.util.Comparator;

import lib.base.ArithmeticOperations;

/**
 * @author https://atcoder.jp/users/suisen
 */
public class Complex implements Cloneable, ArithmeticOperations<Complex>, Serializable {
    private static final long serialVersionUID = -5434623077861420738L;

    public static final double EPS = 1e-9;

    public static final double PI = Math.PI;
    public static final double E = Math.E;

    public static final Complex ZERO = new Complex(0, 0);
    public static final Complex ONE  = new Complex(1, 0);
    public static final Complex I    = new Complex(0, 1);
    public static final Complex NAN  = new Complex(Double.NaN, Double.NaN);

    public static final int TO_STRING_PRECISION = 9;

    public static final Comparator<Complex> XY_COMPARATOR = (c1, c2) -> {
        int sgnX = sgn(c1.x - c2.x);
        return sgnX == 0 ? sgn(c1.y - c2.y) : sgnX;
    };
    public static final Comparator<Complex> XY_COMPARATOR_DESC = (c1, c2) -> {
        int sgnX = sgn(c2.x - c1.x);
        return sgnX == 0 ? sgn(c2.y - c1.y) : sgnX;
    };
    public static final Comparator<Complex> YX_COMPARATOR = (c1, c2) -> {
        int sgnY = sgn(c1.y - c2.y);
        return sgnY == 0 ? sgn(c1.x - c2.x) : sgnY;
    };
    public static final Comparator<Complex> YX_COMPARATOR_DESC = (c1, c2) -> {
        int sgnY = sgn(c2.y - c1.y);
        return sgnY == 0 ? sgn(c2.x - c1.x) : sgnY;
    };
    public static final Comparator<Complex> ARG_COMPARATOR = (c1, c2) -> {
        int sgnArg = sgn(c1.arg() - c2.arg());
        return sgnArg == 0 ? sgn(c1.absSq() - c2.absSq()) : sgnArg;
    };
    public static final Comparator<Complex> ARG_COMPARATOR_DESC = (c1, c2) -> {
        int sgnArg = sgn(c2.arg() - c1.arg());
        return sgnArg == 0 ? sgn(c2.absSq() - c1.absSq()) : sgnArg;
    };
    
    public final double x, y;
    private Complex(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Complex ofCartesian(double real, double imaginary) {
        return new Complex(real, imaginary);
    }
    public static Complex ofPolar(double rho, double theta) {
        return new Complex(rho * Math.cos(theta), rho * Math.sin(theta));
    }
    public static Complex ofCis(double theta) {
        return new Complex(Math.cos(theta), Math.sin(theta));
    }

    public static int sgn(double d) {
        return d > EPS ? 1 : d < -EPS ? -1 : 0;
    }

    public Complex negate() {
        return new Complex(-x, -y);
    }
    public Complex conj() {
        return new Complex(x, -y);
    }
    public Complex exp() {
        double exp = Math.exp(x);
        return new Complex(exp * Math.cos(y), exp * Math.sin(y));
    }
    public Complex[] nthRoot(int n) {
        Complex[] roots = new Complex[n];
        double absN = Math.pow(abs(), 1. / n);
        double phiN = arg() / n;
        double slice = 2. * PI / n;
        for (int k = 0; k < n; k++) {
            roots[k] = new Complex(absN * Math.cos(phiN + slice * k), absN * Math.sin(phiN + slice * k));
        }
        return roots;
    }

    public double arg() {
        return Math.atan2(y, x);
    }
    public double abs() {
        return Math.sqrt(x * x + y * y);
    }
    public double absSq() {
        return x * x + y * y;
    }

    public Complex add(Complex a) {
        return new Complex(x + a.x, y + a.y);
    }
    public Complex add(double real) {
        return new Complex(x + real, y);
    }
    public Complex addIm(double imaginary) {
        return new Complex(x, y + imaginary);
    }
    public Complex sub(Complex a) {
        return new Complex(x - a.x, y - a.y);
    }
    public Complex sub(double real) {
        return new Complex(x - real, y);
    }
    public Complex subIm(double imaginary) {
        return new Complex(x, y - imaginary);
    }
    public Complex subFrom(double real) {
        return new Complex(real - x, -y);
    }
    public Complex subFromIm(double imaginary) {
        return new Complex(-x, imaginary - y);
    }
    public Complex mul(Complex a) {
        return new Complex(x * a.x - y * a.y, y * a.x + x * a.y);
    }
    public Complex mul(double real) {
        return new Complex(real * x, real * y);
    }
    public Complex mulIm(double imaginary) {
        return new Complex(-y * imaginary, x * imaginary);
    }
    public Complex div(Complex a) {
        double d2 = a.x * a.x + a.y * a.y;
        double cx = x * a.x + y * a.y;
        double cy = y * a.x - x * a.y;
        return new Complex(cx / d2, cy / d2);
    }
    public Complex div(double real) {
        return new Complex(x / real, y / real);
    }
    public Complex divIm(double imaginary) {
        return new Complex(y / imaginary, -x / imaginary);
    }
    public Complex divFrom(double real) {
        double d2 = x * x + y * y;
        double cx = real * x;
        double cy = real * -y;
        return new Complex(cx / d2, cy / d2);
    }
    public Complex divFromIm(double imaginary) {
        double d2 = x * x + y * y;
        double cx = imaginary * y;
        double cy = imaginary * x;
        return new Complex(cx / d2, cy / d2);
    }

    public double dot(Complex a) {
        return x * a.x + y * a.y;
    }
    public double det(Complex a) {
        return x * a.y - y * a.x;
    }

    public Complex rot(double theta) {
        return mul(ofCis(theta));
    }

    public boolean isZero() {
        return sgn(x) == 0 && sgn(y) == 0;
    }

    public int compareXY(Complex c) {
        return XY_COMPARATOR.compare(this, c);
    }
    public int compareYX(Complex c) {
        return YX_COMPARATOR.compare(this, c);
    }
    public int compareXYDesc(Complex c) {
        return XY_COMPARATOR_DESC.compare(this, c);
    }
    public int compareYXDesc(Complex c) {
        return YX_COMPARATOR_DESC.compare(this, c);
    }

    public static Complex sum(Complex a, Complex b) {
        return a.add(b);
    }
    public static Complex sum(Complex a, Complex b, Complex c) {
        return a.add(b).add(c);
    }
    public static Complex sum(Complex a, Complex b, Complex c, Complex d) {
        return a.add(b).add(c).add(d);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Complex) {
            Complex c = (Complex) o;
            return sub(c).isZero();
        }
        return false;
    }
    public String toStringReal() {
        return doubleToString(x, TO_STRING_PRECISION);
    }
    public String toStringReal(int precision) {
        return doubleToString(x, precision);
    }
    public String toStringImag() {
        return doubleToString(y, TO_STRING_PRECISION);
    }
    public String toStringImag(int precision) {
        return doubleToString(y, precision);
    }
    public String toString(int precision) {
        return String.format("%s+i*%s", toStringReal(precision), toStringImag(precision));
    }
    @Override
    public String toString() {
        return toString(TO_STRING_PRECISION);
    }
    private static String doubleToString(double d, int precision) {
        StringBuilder sb = new StringBuilder();
        if (d < 0) {
            sb.append('-');
            d = -d;
        }
        d += Math.pow(10, -precision) / 2;
        sb.append((long) d).append('.');
        d -= (long) d;
        for(int i = 0; i < precision; i++){
            d *= 10;
            sb.append((int) d);
            d -= (int) d;
        }
        return sb.toString();
    }
    @Override
    public int hashCode() {
        return Double.hashCode(x) * 37 + Double.hashCode(y);
    }
    @Override
    public Complex clone() {
        return Complex.ofCartesian(x, y);
    }
}
