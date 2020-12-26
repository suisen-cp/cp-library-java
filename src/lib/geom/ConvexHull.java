package lib.geom;

/**
 * @author https://atcoder.jp/users/suisen
 * 
 * http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=CGL_3_B
 * http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=CGL_4_A
 */
public class ConvexHull {
    public static Polygon solve(Complex... ps) {
        int n = ps.length;
        Complex[] points = new Complex[n];
        for (int i = 0; i < n; i++) {
            points[i] = ps[i].clone();
        }
        java.util.Arrays.sort(points, Complex.XY_COMPARATOR);
        java.util.HashSet<Complex> set = new java.util.HashSet<>();
        Complex[] stack = new Complex[n];
        int ptr = 0;
        for (int i = -(n - 1), t = 1; i <= n - 1; i++) {
            if (i == 1) t = ptr;
            Complex c = points[n - 1 - i * Integer.signum(i)];
            while (ptr > t) {
                Complex a = stack[ptr - 2];
                Complex b = stack[ptr - 1];
                if (Geometry.isp(a, b, c) > 0) break;
                --ptr;
                set.remove(b);
            }
            if (!set.contains(c)) {
                stack[ptr++] = c;
                set.add(c);
            }
        }
        Complex[] ch = new Complex[ptr];
        System.arraycopy(stack, 0, ch, 0, ptr);
        return new Polygon(ch);
    }
}
