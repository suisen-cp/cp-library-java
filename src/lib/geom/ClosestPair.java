package lib.geom;

import lib.util.pair.Pair;

import java.util.Arrays;

/**
 * @author https://atcoder.jp/users/suisen
 * 
 * http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=CGL_5_A
 */
public class ClosestPair {
    private static final int initialBlockSize = 4;
    public static Pair<Complex, Complex> solve(Complex... ps) {
        int n = ps.length;
        if (n == 0) throw new IllegalArgumentException();
        if (n == 1) return new Pair<>(ps[0], ps[0]);
        Complex[] points = new Complex[n];
        for (int i = 0; i < n; i++) {
            points[i] = ps[i].clone();
        }
        Arrays.sort(points, Complex.XY_COMPARATOR);
        double ansmin = Double.MAX_VALUE;
        Complex ans1 = null, ans2 = null;
        double[] dmin = new double[n];
        Complex[] work = new Complex[n];
        Complex[] y = new Complex[n];
        System.arraycopy(points, 0, y, 0, n);
        for (int l = 0, r; l < n; l = r) {
            dmin[l] = Double.MAX_VALUE;
            r = Math.min(l + initialBlockSize, n);
            for (int i = l; i < r; i++) for (int j = i + 1; j < r; j++) {
                double dist = points[i].sub(points[j]).abs();
                if (dist < dmin[l] && (dmin[l] = dist) < ansmin) {
                    ansmin = dist;
                    ans1 = points[i]; ans2 = points[j];
                }
            }
            Arrays.sort(y, l, r, Complex.YX_COMPARATOR);
        }
        for (int block = initialBlockSize; block <= n; block <<= 1) {
            for (int l = 0, m, r, maxl = n - block; l < maxl; l = r) {
                m = l + block; r = Math.min(m + block, n);
                double xb = points[m].x;
                double d = Math.min(dmin[l], dmin[m]);
                System.arraycopy(y, l, work, l, m - l);
                for (int i = l, wi = l, ti = m;; i++) {
                    if (ti == r) {
                        System.arraycopy(work, wi, y, i, m - wi);
                        break;
                    }
                    if (work[wi].y > y[ti].y) {
                        y[i] = y[ti++];
                    } else {
                        y[i] = work[wi++];
                        if (wi == m) break;
                    }
                }
                double min = d;
                for (int i = l, idx = l; i < r; i++) {
                    Complex c = y[i];
                    if (Geometry.sgn(Math.abs(c.x - xb) - d) > 0) {
                        continue;
                    }
                    for (int j = idx - 1; j >= l; j--) {
                        if (Geometry.sgn((c.y - work[j].y) - d) > 0) break;
                        double dist = c.sub(work[j]).abs();
                        if (dist < min && (min = dist) < ansmin) {
                            ansmin = dist;
                            ans1 = c; ans2 = work[j];
                        }
                    }
                    work[idx++] = c;
                }
                dmin[l] = min;
            }
        }
        return new Pair<>(ans1, ans2);
    }
}
