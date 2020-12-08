package lib.geom;

import lib.util.pair.Pair;

/**
 * @author https://atcoder.jp/users/suisen
 * 
 * http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=CGL_4_B
 */
public class FarthestPair {
    public static Pair<Complex, Complex> solve(Complex... points) {
        if (points.length == 0) throw new IllegalArgumentException();
        if (points.length == 1) return new Pair<>(points[0], points[0]);
        Polygon p = ConvexHull.solve(points);
        final int n = p.n;
        int i = 0, j = n - 1;
        Complex a = p.get(i);
        Complex b = p.get(j);
        for (int k = 0; k < n; k++) {
            Complex c = p.points[k];
            if (a.x > c.x) {i = k; a = c;}
            if (b.x < c.x) {j = k; b = c;}
        }
        double diam = 0;
        final int si = i, sj = j;
        Complex x = a, y = b;
        for (Complex ci = p.points[i], cj = p.points[j]; i != sj || j != si;) {
            double r = ci.sub(cj).abs();
            if (diam < r) {
                x = ci; y = cj;
                diam = r;
            }
            Complex ci1 = p.get(i + 1), cj1 = p.get(j + 1);
            if (ci1.sub(ci).det(cj1.sub(cj)) < 0) {
                if (++i >= n) i -= n;
                ci = ci1;
            } else {
                if (++j >= n) j -= n;
                cj = cj1;
            }
        }
        return new Pair<>(x, y);
    }
}
