package lib.geom.light;

import lib.geom.Complex;

/**
 * @author https://atcoder.jp/users/suisen
 */
public class LPolygon {
    public final int n;
    public final LComplex[] points;
    public LPolygon(LComplex... points) {
        this.n = points.length;
        this.points = points;
    }
    public LComplex get(int i) {
        return points[(i %= n) < 0 ? i + n : i];
    }
    public boolean in(LComplex c) {
        return in(c, this);
    }
    public boolean on(LComplex c) {
        return on(c, this);
    }
    public boolean contains(LComplex c) {
        return contains(c, this);
    }
    public Complex pG() {
        return pG(this);
    }
    public double area() {
        return area(this);
    }
    // http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=CGL_3_C
    public static boolean in(LComplex c, LPolygon p) {
        if (on(c, p)) return false;
        LLine ray = LLine.ofRay(c, c.add(new LComplex(998244353, 1000000007)));
        boolean res = false;
        for (int i = 0; i < p.n; i++) {
            LComplex p1 = p.get(i);
            LComplex p2 = p.get(i + 1);
            LLine seg = LLine.ofSegment(p1, p2);
            if (LGeometry.hasCrossPoint(ray, seg)) {
                res = !res;
            }
        }
        return res;
    }
    // http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=CGL_3_C
    public static boolean on(LComplex c, LPolygon p) {
        int n = p.n;
        for (int i = 0; i < n; i++) {
            LComplex p1 = p.get(i);
            LComplex p2 = p.get(i + 1);
            LLine l = LLine.ofSegment(p1, p2);
            if (LGeometry.dist(c, l) == 0) {
                return true;
            }
        }
        return false;
    }
    public static boolean contains(LComplex c, LPolygon p) {
        return in(c, p) || on(c, p);
    }
    public static Complex pG(LPolygon p) {
        Complex g = Complex.ZERO;
        double s = 0;
        for (int i = 0; i < p.n; i++) {
            double w = p.get(i).det(p.get(i + 1));
            Complex z = LComplex.sum(p.get(i), p.get(i + 1)).toComplex().div(w);
            g = g.add(z);
            s += w;
        }
        return g.div(s * 3.);
    }
    public static double area(LPolygon p) {
        long res = 0;
        for (int i = 0; i < p.n; i++) {
            res += p.get(i).det(p.get(i + 1));
        }
        return res / 2.;
    }
}
