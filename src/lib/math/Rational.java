package lib.math;

import java.util.function.BinaryOperator;

import lib.base.ArithmeticOperations;
import lib.base.PrimitiveComparable;

public class Rational extends Number
    implements Comparable<Rational>, Cloneable, PrimitiveComparable, ArithmeticOperations<Rational> {
    private static final long serialVersionUID = 1L;
    public static final Rational ZERO    = new Rational( 0, 1);
    public static final Rational ONE     = new Rational( 1, 1);
    public static final Rational NAN     = new Rational( 0, 0);
    public static final Rational POS_INF = new Rational( 1, 0);
    public static final Rational NEG_INF = new Rational(-1, 0);
    private final long n;
    private final long d;

    public Rational(long numerator, long denominator) {
        if (denominator == 0) {
            this.d = 0;
            this.n = Long.signum(numerator);
        } else if (numerator == 0) {
            this.n = 0; this.d = 1;
        } else {
            if (denominator < 0) {numerator *= -1; denominator *= -1;}
            final long g = MathUtil.gcd(numerator, denominator);
            this.n = numerator / g; this.d = denominator / g;
        }
    }

    private Rational add(long num, long den) {
        final long g = MathUtil.gcd(d, den);
        final long d1 = d / g, d2 = den / g;
        return new Rational(n * d2 + num * d1, d1 * d2 * g);
    }

    private Rational mul(long num, long den) {
        final long g1 = MathUtil.gcd(n, den), g2 = MathUtil.gcd(num, d);
        return new Rational((n / g1) * (num / g2), (d / g2) * (den / g1));
    }

    @Override public Rational add(Rational r) {
        if (isNan() || r.isNan()) return NAN;
        if (isPositiveInfty()) return r.isNegativeInfty() ? NAN : POS_INF;
        if (isNegativeInfty()) return r.isPositiveInfty() ? NAN : NEG_INF;
        if (r.isPositiveInfty()) return POS_INF;
        if (r.isNegativeInfty()) return NEG_INF;
        if (isZero()) return r;
        if (r.isZero()) return this;
        return add(r.n, r.d);
    }

    @Override public Rational sub(Rational r) {
        if (isNan() || r.isNan()) return NAN;
        if (isPositiveInfty()) return r.isPositiveInfty() ? NAN : POS_INF;
        if (isNegativeInfty()) return r.isNegativeInfty() ? NAN : NEG_INF;
        if (r.isPositiveInfty()) return NEG_INF;
        if (r.isNegativeInfty()) return POS_INF;
        if (isZero()) return r.neg();
        if (r.isZero()) return this;
        return add(-r.n, r.d);
    }

    @Override public Rational mul(Rational r) {
        if (isNan() || r.isNan()) return NAN;
        if (isZero() || r.isZero()) return ZERO;
        if (isPositiveInfty()) return r.isPositive() ? POS_INF : NEG_INF;
        if (isNegativeInfty()) return r.isPositive() ? NEG_INF : POS_INF;
        if (r.isPositiveInfty()) return isPositive() ? POS_INF : NEG_INF;
        if (r.isNegativeInfty()) return isPositive() ? NEG_INF : POS_INF;
        return mul(r.n, r.d);
    }

    @Override public Rational div(Rational r) {return mul(r.reciprocal());}

    public Rational add(long x) {
        if (isNan()) return NAN;
        if (isInfty()) return this;
        if (isZero()) return valueOf(x);
        if (x == 0) return this;
        return add(x, 1);
    }

    public Rational sub(long x) {return add(-x);}

    public Rational mul(long x) {
        if (isNan()) return NAN;
        if (isZero() || x == 0) return ZERO;
        if (isPositiveInfty()) return x > 0 ? POS_INF : NEG_INF;
        if (isNegativeInfty()) return x < 0 ? NEG_INF : POS_INF;
        return mul(x, 1);
    }

    public Rational div(long x) {return mul(reciprocal(x));}
    
    public Rational reciprocal() {return new Rational(d, n);}

    public long numerator()   {return n;}
    public long denominator() {return d;}

    public Rational neg() {return new Rational(-n, d);}
    public Rational abs() {return new Rational(Math.abs(n), d);}

    public int signum() {return (int) ((n >> 63) | (-n >>> 63));}

    public boolean isZero()          {return equals(ZERO);}
    public boolean isNan()           {return equals(NAN);}
    public boolean isInfty()         {return equals(POS_INF) || equals(NEG_INF);}
    public boolean isPositiveInfty() {return equals(POS_INF);}
    public boolean isNegativeInfty() {return equals(NEG_INF);}
    public boolean isPositive()      {return n > 0;}
    public boolean isNegative()      {return n < 0;}
    public boolean isInteger()       {return d == 1;}

    @Override public int    intValue()    {return Math.toIntExact(n / d);}
    @Override public long   longValue()   {return n / d;}
    @Override public float  floatValue()  {return (float) doubleValue();}
    @Override public double doubleValue() {return (double) n / d;}

    @Override public int compareTo(Rational r) {
        if (isNan()) return 1;
        if (r.isNan()) return -1;
        if (isPositiveInfty()) return r.isPositiveInfty() ? 0 : 1;
        if (isNegativeInfty()) return r.isNegativeInfty() ? 0 : -1;
        if (n == 0) return -r.signum();
        if (r.n == 0) return signum();
        boolean rv = n < 0 || r.n < 0;
        if (rv) {if (n > 0) return 1; if (r.n > 0) return -1;}
        long n1 = Math.abs(n), d1 = d, n2 = Math.abs(r.n), d2 = r.d;
        for (; d1 != 0 && d2 != 0; rv = !rv) {
            long p1 = n1 / d1, p2 = n2 / d2;
            if (p1 != p2) return (p1 > p2) ^ rv ? 1 : -1;
            n1 %= d1; n2 %= d2;
            long t1 = n1; n1 = d1; d1 = t1;
            long t2 = n2; n2 = d2; d2 = t2;
        }
        long c = n1 * d2 - n2 * d1;
        return c == 0 ? 0 : (c > 0) ^ rv ? 1 : -1;
    }

    @Override public int compareTo(long x) {
        if (isPositiveInfty() || isNan()) return 1;
        if (isNegativeInfty()) return -1;
        long y = n / d;
        if (x != y) return Long.compare(y, x);
        return isInteger() ? 0 : isPositive() ? 1 : -1;
    }

    @Override public int compareTo(double d) {return Double.compare(doubleValue(), d);}

    @Override public Rational clone() {return new Rational(n, d);}

    @Override public String toString(){
        if (isNan()) return "nan";
        if (isPositiveInfty()) return "+Inf";
        if (isNegativeInfty()) return "-Inf";
        if (isInteger()) return String.valueOf(n);
        return n + "/" + d + " = " + doubleValue();
    }

    @Override public boolean equals(Object o){
        if (this == o) return true;
        if (o instanceof Rational) {
            Rational r = (Rational) o;
            return n == r.n && d == r.d;
        }
        return false;
    }

    @Override public int hashCode() {
        int hash = 1;
        int hashN = (int) (n ^ (n >>> 32));
        hash = hash * 31 + hashN;
        int hashD = (int) (d ^ (d >>> 32));
        hash = hash * 31 + hashD;
        return hash;
    }

    // * static methods *

    public static Rational valueOf(long x) {return new Rational(x, 1);}
    public static Rational reciprocal(long x) {return new Rational(1, x);}

    public static Rational approximate(double d, long max, boolean roundToZero) {
        double x = Math.abs(d);
        long ln = 0, ld = 1;
        long rn = 1, rd = 0;
        while (true) {
            long mn = ln + rn, md = ld + rd;
            if (mn > max || md > max) break;
            if (mn < ln || mn < rn || md < ld || md < rd) break;
            if ((double) mn / md < x) {
                ln = mn; ld = md;
            } else {
                rn = mn; rd = md;
            }
        }
        if (roundToZero) {
            return new Rational(d < 0 ? -ln : ln, ld);
        } else {
            return new Rational(d < 0 ? -rn : rn, rd);
        }
    }

    public static Rational neg(Rational r) {return r.neg();}
    public static Rational abs(Rational r) {return r.abs();}

    public static int signum(Rational r) {return r.signum();}

    public static boolean isZero         (Rational r) {return r.isZero();}
    public static boolean isNan          (Rational r) {return r.isNan();}
    public static boolean isInfty        (Rational r) {return r.isInfty();}
    public static boolean isPositiveInfty(Rational r) {return r.isPositiveInfty();}
    public static boolean isNegativeInfty(Rational r) {return r.isNegativeInfty();}
    public static boolean isPositive     (Rational r) {return r.isPositive();}
    public static boolean isNegative     (Rational r) {return r.isNegative();}
    public static boolean isInteger      (Rational r) {return r.isInteger();}

    public static int compare(Rational a, Rational b) {return a.compareTo(b);}

    public static Rational max(Rational a, Rational b) {
        if (a.isNan()) return b;
        if (b.isNan()) return a;
        return a.compareTo(b) >= 0 ? a : b;
    }
    public static Rational min(Rational a, Rational b) {
        if (a.isNan()) return b;
        if (b.isNan()) return a;
        return a.compareTo(b) <= 0 ? a : b;
    }

    // public static Rational add(Rational a, Rational b) {return a.add(b);}
    // public static Rational sub(Rational a, Rational b) {return a.sub(b);}
    // public static Rational mul(Rational a, Rational b) {return a.mul(b);}
    // public static Rational div(Rational a, Rational b) {return a.div(b);}

    public static Rational foldLeft(BinaryOperator<Rational> binop, Rational... rs) {
        final int l = rs.length;
        if (l == 0) return null;
        Rational r = rs[0];
        for (int i = 1; i < l; i++) r = binop.apply(r, rs[i]);
        return r;
    }
    public static Rational foldRight(BinaryOperator<Rational> binop, Rational... rs) {
        final int l = rs.length;
        if (l == 0) return null;
        Rational r = rs[l - 1];
        for (int i = l - 2; i >= 0; i--) r = binop.apply(rs[i], r);
        return r;
    }

    public static Rational sum (Rational... rs) {return foldLeft(Rational::add, rs);}
    public static Rational prod(Rational... rs) {return foldLeft(Rational::mul, rs);}
    
    public static Rational[] sternBrocot(int depth) {
        final int siz = 2 << depth;
        Rational[] t = new Rational[siz];
        for (int i = 1; i < siz; i++) {
            int lk = (i / ( i & - i)) >> 1;
            int rk = (i / (~i & -~i)) >> 1;
            Rational lr = lk == 0 ? ZERO    : t[lk];
            Rational rr = rk == 0 ? POS_INF : t[rk];
            t[i] = mid(lr, rr);
        }
        return t;
    }

    public static Rational[] FareySequence(int depth) {
        final int siz = 2 << depth;
        Rational[] t = new Rational[siz];
        for (int i = 1; i < siz; i++) {
            int lk = (i / ( i & - i)) >> 1;
            int rk = (i / (~i & -~i)) >> 1;
            Rational lr = lk == 0 ? ZERO : t[lk];
            Rational rr = rk == 0 ? ONE  : t[rk];
            t[i] = mid(lr, rr);
        }
        int[] s = new int[(depth + 1) << 1];
        Rational[] f = new Rational[siz + 1];
        f[0] = ZERO; f[siz] = ONE;
        int i = 1, k = 0;
        s[k++] = ~1;
        s[k++] =  1;
        while (k > 0) {
            int j = s[--k];
            if (j >= 0) {
                if (j << 1 < siz) {
                    s[k++] = ~(j << 1);
                    s[k++] =  (j << 1);
                }
            } else {
                j = ~j;
                f[i++] = t[j];
                if (j << 1 < siz) {
                    s[k++] = ~(j << 1 | 1);
                    s[k++] =  (j << 1 | 1);
                }
            }
        }
        return f;
    }

    private static Rational mid(Rational l, Rational r) {
        return new Rational(l.n + r.n, l.d + r.d);
    }
}