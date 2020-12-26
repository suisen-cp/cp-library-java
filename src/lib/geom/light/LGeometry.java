package lib.geom.light;

import lib.geom.Complex;

import java.util.Arrays;

/**
 * @author https://atcoder.jp/users/suisen
 */
public class LGeometry {
    /**
     * relations between three points X, Y, Z.
     */
    public static final int LEFT_CURVE    = +1; // +---------------+ Z is in 'a' => ISP = +1
    public static final int RIGHT_CURVE   = -1; // |aaaaaaaaaaaaaaa| Z is in 'b' => ISP = -1
    public static final int ONLINE_FRONT  = +2; // |ddd X eee Y ccc| Z is in 'c' => ISP = +2
    public static final int ONLINE_BACK   = -2; // |bbbbbbbbbbbbbbb| Z is in 'd' => ISP = -2
    public static final int ONLINE_MIDDLE =  0; // +---------------+ Z is in 'e' => ISP =  0

    public static int isp(LComplex a, LComplex b, LComplex c) {
        LComplex ab = b.sub(a);
        LComplex ac = c.sub(a);
        long det = ab.det(ac);
        if (det > 0) {
            return LEFT_CURVE;
        } else if (det < 0) {
            return RIGHT_CURVE;
        } else if (ab.dot(ac) < 0) {
            return ONLINE_BACK;
        } else if (a.sub(b).dot(c.sub(b)) < 0) {
            return ONLINE_FRONT;
        } else {
            return ONLINE_MIDDLE;
        }
    }

    public static void argsort(LComplex[] points, final LComplex center) {
        final LComplex x = center.add(LComplex.ONE);
        Arrays.sort(points, (a, b) -> {
            if (a.equals(b)) return 0;
            int ispa = isp(center, x, a), ispb = isp(center, x, b);
            if (ispa == 0) ispa = 2;
            if (ispb == 0) ispb = 2;
            if (ispa * ispb < 0) {
                return ispb;
            } else {
                int acb = isp(a, center, b);
                return acb == 0 ? 1 : acb;
            }
        });
    }
    
    // http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=CGL_2_B
    public static boolean hasCommonPoint(LLine l1, LLine l2) {
        if (hasCrossPoint(l1, l2)) return true;
        if (!LLine.isSame(l1, l2)) return false;
        if (l1.isLLine() || l2.isLLine()) return true;
        return l1.on(l2.a) || l1.on(l2.b) || l2.on(l1.a) || l2.on(l1.b);
    }
    public static boolean hasCrossPoint(LLine l1, LLine l2) {
        return crossPoint(l1, l2) != null;
    }
    // http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=CGL_2_C
    public static Complex crossPoint(LLine l1, LLine l2) {
        if (LLine.isParallel(l1, l2)) return null;
        LComplex a = l1.a, b = l1.b, c = l2.a, d = l2.b;
        LComplex ab = b.sub(a), cd = d.sub(c), ac = c.sub(a);
        double r = (double) ac.det(cd) / ab.det(cd);
        Complex p = a.toComplex().add(ab.toComplex().mul(r));
        if (l1.toLine().onRangeXY(p) && l2.toLine().onRangeXY(p)) return p;
        return null;
    }
    // http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=CGL_2_D
    public static double dist(LComplex c, LLine l) {
        LComplex a = l.a, b = l.b;
        LComplex ab = b.sub(a), ac = c.sub(a);
        if (l.isRay()) {
            if (ab.dot(ac) < 0) {
                return ac.abs();
            }
        } else if (l.isSegment()) {
            LComplex ba = a.sub(b), bc = c.sub(b);
            if (ab.dot(ac) < 0) {
                return ac.abs();
            } else if (ba.dot(bc) < 0) {
                return bc.abs();
            }
        }
        return Math.abs(ac.det(ab) / ab.abs());
    }
    // http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=CGL_2_D
    public static double dist(LLine l1, LLine l2) {
        LComplex a = l1.a, b = l1.b, c = l2.a, d = l2.b;
        if (hasCommonPoint(l1, l2)) {
            return 0;
        }
        if (l1.isLLine()) {
            if (l2.isLLine()) {                                  // line & line
                return dist(a, l2);
            } else if (l2.isRay()) {                            // line & ray
                return dist(c, l1);
            } else {                                            // line & segment
                return Math.min(dist(c, l1), dist(d, l1));
            }
        } else if (l1.isRay()) {
            if (l2.isLLine()) {                                  // ray & line
                return dist(a, l2);
            } else if (l2.isRay()) {                            // ray & ray
                return Math.min(dist(c, l1), dist(a, l2));
            } else {                                            // ray & segment
                return Math.min(dist(a, l2), Math.min(dist(c, l1), dist(d, l1)));
            }
        } else {
            if (l2.isLLine()) {                                  // segment & line
                return Math.min(dist(a, l2), dist(b, l2));
            } else if (l2.isRay()) {                            // segment & ray
                return Math.min(dist(c, l1), Math.min(dist(a, l2), dist(b, l2)));
            } else {                                            // segment & segment
                return Math.min(Math.min(dist(c, l1), dist(d, l1)), Math.min(dist(a, l2), dist(b, l2)));
            }
        }
    }
    public static boolean hasCommonPoint(LLine l, LCircle cir) {
        LComplex a = cir.c;
        long r = cir.r;
        LComplex b = l.a, c = l.b;
        if (Complex.sgn(dist(a, l) - r) > 0) return false;
        if (l.isSegment()) {
            LComplex ba = a.sub(b), ca = a.sub(c);
            return Complex.sgn(Math.max(ba.abs(), ca.abs()) - r) >= 0;
        } else {
            return true;
        }
    }
    // http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=CGL_7_A
    public static int commonTangentNum(LCircle c1, LCircle c2) {
        LComplex a = c1.c, b = c2.c;
        long r1 = c1.r, r2 = c2.r;
        if (r1 < r2) {
            long tmp = r1; r1 = r2; r2 = tmp;
        }
        long d = a.sub(b).absSq();
        long d1 = d - (r1 + r2) * (r1 + r2);
        long d2 = d - (r1 - r2) * (r1 - r2);
        if (d1 > 0) {
            return 4;
        } else if (d1 == 0) {
            return 3;
        } else if (d2 > 0) {
            return 2;
        } else if (d2 == 0) {
            return 1;
        } else {
            return 0;
        }
    }
    // http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=CGL_7_E
    public static int commonPointNum(LCircle c1, LCircle c2) {
        return 2 - Math.abs(commonTangentNum(c1, c2) - 2);
    }
    // http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=CGL_1_A
    public static Complex projection(LComplex c, LLine l) {
        LComplex a = l.a, b = l.b;
        LComplex ab = b.sub(a), ac = c.sub(a);
        return a.toComplex().add(ab.toComplex().mul((double) ab.dot(ac) / ab.absSq()));
    }
    // http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=CGL_1_B
    public static Complex reflection(LComplex c, LLine l) {
        Complex h = projection(c, l);
        return h.add(h.sub(c.toComplex()));
    }

    public static double signedArea(LComplex a, LComplex b, LComplex c) {
        LComplex ab = b.sub(a), ac = c.sub(a);
        return ab.det(ac) / 2.;
    }
    public static double unsignedArea(LComplex a, LComplex b, LComplex c) {
        LComplex ab = b.sub(a), ac = c.sub(a);
        return Math.abs(ab.det(ac) / 2.);
    }
}
