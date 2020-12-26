package lib.geom.light;

import java.io.Serializable;
import java.util.Comparator;

import lib.base.ArithmeticOperations;
import lib.geom.Complex;

/**
 * @author https://atcoder.jp/users/suisen
 */
public class LComplex implements Cloneable, Serializable, ArithmeticOperations<LComplex> {
    private static final long serialVersionUID = -5918426110460447574L;

    public static final LComplex ZERO = new LComplex(0, 0);
    public static final LComplex ONE  = new LComplex(1, 0);
    public static final LComplex I    = new LComplex(0, 1);

    public static final Comparator<LComplex> XY_COMPARATOR = Comparator.comparingLong((LComplex c) -> c.x).thenComparingLong(c -> c.y);
    public static final Comparator<LComplex> XY_COMPARATOR_DESC = XY_COMPARATOR.reversed();
    public static final Comparator<LComplex> YX_COMPARATOR = Comparator.comparingLong((LComplex c) -> c.y).thenComparingLong(c -> c.x);
    public static final Comparator<LComplex> YX_COMPARATOR_DESC = YX_COMPARATOR.reversed();
    public static final Comparator<LComplex> ARG_COMPARATOR = (c1, c2) -> {
        int sgnArg = Complex.sgn(c1.arg() - c2.arg());
        return sgnArg == 0 ? Complex.sgn(c1.absSq() - c2.absSq()) : sgnArg;
    };
    public static final Comparator<LComplex> ARG_COMPARATOR_DESC = (c1, c2) -> {
        int sgnArg = Complex.sgn(c2.arg() - c1.arg());
        return sgnArg == 0 ? Complex.sgn(c2.absSq() - c1.absSq()) : sgnArg;
    };
    
    public final long x, y;
    public LComplex(long x, long y) {
        this.x = x;
        this.y = y;
    }

    public Complex toComplex() {
        return Complex.ofCartesian(x, y);
    }

    public LComplex negate() {
        return new LComplex(-x, -y);
    }
    public LComplex conj() {
        return new LComplex(x, -y);
    }

    public double arg() {
        return Math.atan2(y, x);
    }
    public double abs() {
        return Math.sqrt(x * x + y * y);
    }
    public long absSq() {
        return x * x + y * y;
    }

    public LComplex add(LComplex a) {
        return new LComplex(x + a.x, y + a.y);
    }
    public LComplex add(long real) {
        return new LComplex(x + real, y);
    }
    public LComplex addIm(long imaginary) {
        return new LComplex(x, y + imaginary);
    }
    public LComplex sub(LComplex a) {
        return new LComplex(x - a.x, y - a.y);
    }
    public LComplex sub(long real) {
        return new LComplex(x - real, y);
    }
    public LComplex subIm(long imaginary) {
        return new LComplex(x, y - imaginary);
    }
    public LComplex subFrom(long real) {
        return new LComplex(real - x, -y);
    }
    public LComplex subFromIm(long imaginary) {
        return new LComplex(-x, imaginary - y);
    }
    public LComplex mul(LComplex a) {
        return new LComplex(x * a.x - y * a.y, y * a.x + x * a.y);
    }
    public LComplex mul(long real) {
        return new LComplex(real * x, real * y);
    }
    public LComplex mulIm(long imaginary) {
        return new LComplex(-y * imaginary, x * imaginary);
    }
    public LComplex div(LComplex t) {
        throw new UnsupportedOperationException();
    }

    public long dot(LComplex a) {
        return x * a.x + y * a.y;
    }
    public long det(LComplex a) {
        return x * a.y - y * a.x;
    }

    public boolean isZero() {
        return x == 0 && y == 0;
    }

    public int compareXY(LComplex c) {
        return XY_COMPARATOR.compare(this, c);
    }
    public int compareYX(LComplex c) {
        return YX_COMPARATOR.compare(this, c);
    }
    public int compareXYDesc(LComplex c) {
        return XY_COMPARATOR_DESC.compare(this, c);
    }
    public int compareYXDesc(LComplex c) {
        return YX_COMPARATOR_DESC.compare(this, c);
    }

    public static LComplex sum(LComplex a, LComplex b) {
        return a.add(b);
    }
    public static LComplex sum(LComplex a, LComplex b, LComplex c) {
        return a.add(b).add(c);
    }
    public static LComplex sum(LComplex a, LComplex b, LComplex c, LComplex d) {
        return a.add(b).add(c).add(d);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof LComplex) {
            LComplex c = (LComplex) o;
            return sub(c).isZero();
        }
        return false;
    }
    @Override
    public String toString() {
        return String.format("%d+i*%d", x, y);
    }
    @Override
    public int hashCode() {
        return Long.hashCode(x) * 37 + Long.hashCode(y);
    }
    @Override
    public LComplex clone() {
        return new LComplex(x, y);
    }
}
