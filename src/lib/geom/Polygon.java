package lib.geom;

/**
 * @author https://atcoder.jp/users/suisen
 */
public class Polygon {
    public final int n;
    public final Complex[] points;
    public Polygon(Complex... points) {
        this.n = points.length;
        this.points = points;
    }
    public Complex get(int i) {
        return points[(i %= n) < 0 ? i + n : i];
    }
    public boolean in(Complex c) {
        return in(c, this);
    }
    public boolean on(Complex c) {
        return on(c, this);
    }
    public boolean contains(Complex c) {
        return contains(c, this);
    }
    public Complex pG() {
        return pG(this);
    }
    public double area() {
        return area(this);
    }
    private static final Complex CAR_E_PI = Complex.ofCartesian(Complex.E, Complex.PI);
    private static final Complex CAR_PI_E = Complex.ofCartesian(Complex.PI, Complex.E);
    // http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=CGL_3_C
    public static boolean in(Complex c, Polygon p) {
        if (on(c, p)) return false;
        Complex a = c.equals(CAR_E_PI) ? CAR_PI_E : CAR_E_PI;
        Line ray = Line.ofRay(c, a);
        boolean res = false;
        for (int i = 0; i < p.n; i++) {
            Complex p1 = p.get(i);
            Complex p2 = p.get(i + 1);
            Line seg = Line.ofSegment(p1, p2);
            if (Geometry.hasCrossPoint(ray, seg)) {
                res = !res;
            }
        }
        return res;
    }
    // http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=CGL_3_C
    public static boolean on(Complex c, Polygon p) {
        int n = p.n;
        for (int i = 0; i < n; i++) {
            Complex p1 = p.get(i);
            Complex p2 = p.get(i + 1);
            Line l = Line.ofSegment(p1, p2);
            if (Complex.sgn(Geometry.dist(c, l)) == 0) {
                return true;
            }
        }
        return false;
    }
    public static boolean contains(Complex c, Polygon p) {
        return in(c, p) || on(c, p);
    }
    public static Complex pG(Polygon p) {
        Complex g = Complex.ZERO;
        double s = 0;
        for (int i = 0; i < p.n; i++) {
            double w = p.get(i).det(p.get(i + 1));
            Complex z = Complex.sum(p.get(i), p.get(i + 1)).div(w);
            g = g.add(z);
            s += w;
        }
        return g.div(s * 3.);
    }
    // http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=CGL_3_A
    public static double area(Polygon p) {
        double res = 0;
        for (int i = 0; i < p.n; i++) {
            res += p.get(i).det(p.get(i + 1));
        }
        return res / 2.;
    }
}
