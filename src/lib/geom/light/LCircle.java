package lib.geom.light;

/**
 * @author https://atcoder.jp/users/suisen
 */
public final class LCircle {
    public final LComplex c;
    public final long r;
    public LCircle(LComplex center, long radius) {this.c = center; this.r = radius;}
    public LCircle(long x, long y, long radius) {this(new LComplex(x, y), radius);}
    public boolean on(LComplex a) {
        return on(a, this);
    }
    public boolean in(LComplex a) {
        return in(a, this);
    }
    public boolean contains(LComplex a) {
        return contains(a, this);
    }
    public static boolean on(LComplex a, LCircle c) {
        return a.sub(c.c).absSq() == c.r * c.r;
    }
    public static boolean in(LComplex a, LCircle c) {
        return a.sub(c.c).absSq() < c.r * c.r;
    }
    public static boolean contains(LComplex a, LCircle c) {
        return a.sub(c.c).absSq() <= c.r * c.r;
    }
}