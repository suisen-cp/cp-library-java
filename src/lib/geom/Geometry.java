package lib.geom;

import java.util.Arrays;

/**
 * @author https://atcoder.jp/users/suisen
 */
public class Geometry {
    /**
     * relations between three points X, Y, Z.
     */
    public static final int LEFT_CURVE    = +1; // +---------------+ Z is in 'a' => ISP = +1
    public static final int RIGHT_CURVE   = -1; // |aaaaaaaaaaaaaaa| Z is in 'b' => ISP = -1
    public static final int ONLINE_FRONT  = +2; // |ddd X eee Y ccc| Z is in 'c' => ISP = +2
    public static final int ONLINE_BACK   = -2; // |bbbbbbbbbbbbbbb| Z is in 'd' => ISP = -2
    public static final int ONLINE_MIDDLE =  0; // +---------------+ Z is in 'e' => ISP =  0

    public static final double EPS = Complex.EPS;

    public static final double PI = Complex.PI;
    public static final double E = Complex.E;

    public static int sgn(double d) {
        return d > EPS ? 1 : d < -EPS ? -1 : 0;
    }
    // http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=CGL_1_C
    public static int isp(Complex a, Complex b, Complex c) {
        Complex ab = b.sub(a);
        Complex ac = c.sub(a);
        int det = sgn(ab.det(ac));
        if (det > 0) {
            return LEFT_CURVE;
        } else if (det < 0) {
            return RIGHT_CURVE;
        } else if (sgn(ab.dot(ac)) < 0) {
            return ONLINE_BACK;
        } else if (sgn(a.sub(b).dot(c.sub(b))) < 0) {
            return ONLINE_FRONT;
        } else {
            return ONLINE_MIDDLE;
        }
    }

    public static Complex midPoint(Complex a, Complex b) {
        return a.add(b).div(2.);
    }

    public static void argsort(Complex[] points, final Complex center) {
        final Complex x = center.add(Complex.ONE);
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
    public static boolean hasCommonPoint(Line l1, Line l2) {
        if (hasCrossPoint(l1, l2)) return true;
        if (!Line.isSame(l1, l2)) return false;
        if (l1.isLine() || l2.isLine()) return true;
        return l1.on(l2.a) || l1.on(l2.b) || l2.on(l1.a) || l2.on(l1.b);
    }
    public static boolean hasCrossPoint(Line l1, Line l2) {
        return crossPoint(l1, l2) != null;
    }
    // http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=CGL_2_C
    public static Complex crossPoint(Line l1, Line l2) {
        if (Line.isParallel(l1, l2)) return null;
        Complex a = l1.a, b = l1.b, c = l2.a, d = l2.b;
        Complex ab = b.sub(a), cd = d.sub(c), ac = c.sub(a);
        double r = ac.det(cd) / ab.det(cd);
        Complex p = a.add(ab.mul(r));
        if (l1.onRangeXY(p) && l2.onRangeXY(p)) return p;
        return null;
    }
    // http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=CGL_2_D
    public static double dist(Complex c, Line l) {
        Complex a = l.a, b = l.b;
        Complex ab = b.sub(a), ac = c.sub(a);
        if (l.isRay()) {
            if (sgn(ab.dot(ac)) < 0) {
                return ac.abs();
            }
        } else if (l.isSegment()) {
            Complex ba = a.sub(b), bc = c.sub(b);
            if (sgn(ab.dot(ac)) < 0) {
                return ac.abs();
            } else if (sgn(ba.dot(bc)) < 0) {
                return bc.abs();
            }
        }
        return Math.abs(ac.det(ab) / ab.abs());
    }
    // http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=CGL_2_D
    public static double dist(Line l1, Line l2) {
        Complex a = l1.a, b = l1.b, c = l2.a, d = l2.b;
        if (hasCommonPoint(l1, l2)) {
            return 0;
        }
        if (l1.isLine()) {
            if (l2.isLine()) {                                  // line & line
                return dist(a, l2);
            } else if (l2.isRay()) {                            // line & ray
                return dist(c, l1);
            } else {                                            // line & segment
                return Math.min(dist(c, l1), dist(d, l1));
            }
        } else if (l1.isRay()) {
            if (l2.isLine()) {                                  // ray & line
                return dist(a, l2);
            } else if (l2.isRay()) {                            // ray & ray
                return Math.min(dist(c, l1), dist(a, l2));
            } else {                                            // ray & segment
                return Math.min(dist(a, l2), Math.min(dist(c, l1), dist(d, l1)));
            }
        } else {
            if (l2.isLine()) {                                  // segment & line
                return Math.min(dist(a, l2), dist(b, l2));
            } else if (l2.isRay()) {                            // segment & ray
                return Math.min(dist(c, l1), Math.min(dist(a, l2), dist(b, l2)));
            } else {                                            // segment & segment
                return Math.min(Math.min(dist(c, l1), dist(d, l1)), Math.min(dist(a, l2), dist(b, l2)));
            }
        }
    }

    public static boolean hasCommonPoint(Line l, Circle cir) {
        Complex a = cir.c;
        double r = cir.r;
        Complex b = l.a, c = l.b;
        if (sgn(dist(a, l) - r) > 0) return false;
        if (l.isSegment()) {
            Complex ba = a.sub(b), ca = a.sub(c);
            return sgn(Math.max(ba.abs(), ca.abs()) - r) >= 0;
        } else {
            return true;
        }
    }
    // http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=CGL_7_D
    // http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=CGL_7_H
    public static Complex[] commonPoint(Line l, Circle cir) {
        if (!hasCommonPoint(l, cir)) return new Complex[]{};
        Complex a = l.a, b = l.b;
        Complex c = cir.c;
        double r = cir.r;
        Complex ab = b.sub(a);
        Complex h = projection(c, l);
        double x = Math.sqrt(Math.max(r * r - h.sub(c).absSq(), 0.));
        Complex xab = ab.mul(x / ab.abs());
        int sgna = sgn(a.sub(c).abs() - r), sgnb = sgn(b.sub(c).abs() - r);
        if (l.isSegment()) {
            if (sgna < 0) return new Complex[]{h.add(xab)};
            if (sgnb < 0) return new Complex[]{h.sub(xab)};
        } else if (l.isRay()) {
            if (sgna < 0) return new Complex[]{h.add(xab)};
        }
        if (sgn(dist(c, l) - r) == 0) {
            return new Complex[]{projection(c, l)};
        } else {
            return new Complex[]{h.add(xab), h.sub(xab)};
        }
    }
    // http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=CGL_7_F
    public static Complex[] tangent(Complex p, Circle cir) {
        if (cir.in(p)) return null;
        Complex a = cir.c;
        double r = cir.r;
        if (cir.on(p)) {
            Complex ap = p.sub(a);
            Complex q = p.add(ap.rot(PI * 0.5));
            return new Complex[]{q};
        } else {
            Complex ap = p.sub(a);
            double px = ap.x;
            double py = ap.y;
            double k = (px * px + py * py) / (r * r);
            double l = Math.sqrt(Math.max(k - 1, 0.));
            double tx1 = (px - py * l) / k;
            double ty1 = (py + px * l) / k;
            double tx2 = (px + py * l) / k;
            double ty2 = (py - px * l) / k;
            Complex t1 = Complex.ofCartesian(tx1, ty1).add(a);
            Complex t2 = Complex.ofCartesian(tx2, ty2).add(a);
            return new Complex[]{t1, t2};
        }
    }
    // http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=CGL_7_A
    public static int commonTangentNum(Circle c1, Circle c2) {
        Complex a = c1.c, b = c2.c;
        double r1 = c1.r, r2 = c2.r;
        if (r1 < r2) {
            double tmp = r1; r1 = r2; r2 = tmp;
        }
        double d = a.sub(b).abs();
        int d1 = sgn(d - (r1 + r2));
        int d2 = sgn(d - (r1 - r2));
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
    // http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=CGL_7_G
    public static Complex[][] commonTangent(Circle c1, Circle c2) {
        int num = commonTangentNum(c1, c2);
        Complex[][] tan = new Complex[2][num];
        if (num == 0) return tan;
        Complex a = c1.c, b = c2.c;
        double x1 = a.x, y1 = a.y, r1 = c1.r;
        double x2 = b.x, y2 = b.y, r2 = c2.r;
        double x12 = x2 - x1, y12 = y2 - y1, rp = r1 + r2, rm = r1 - r2, rd = r2 / r1;
        double sqxy = x12 * x12 + y12 * y12, sqrp = rp * rp, sqrm = rm * rm;
        double xr = x12 * r1, yr = y12 * r1;
        double rtp = Math.sqrt(Math.max(sqxy - sqrp, 0.));
        double rtm = Math.sqrt(Math.max(sqxy - sqrm, 0.));
        double al12 = xr * rp, ar12 = yr * rtp, bl12 = yr * rp, br12 = xr * rtp;
        double al34 = xr * rm, ar34 = yr * rtm, bl34 = yr * rm, br34 = xr * rtm;
        Complex p11 = Complex.ofCartesian((al12 + ar12) / sqxy, (bl12 - br12) / sqxy);
        Complex p12 = Complex.ofCartesian((al12 - ar12) / sqxy, (bl12 + br12) / sqxy);
        Complex p13 = Complex.ofCartesian((al34 + ar34) / sqxy, (bl34 - br34) / sqxy);
        Complex p14 = Complex.ofCartesian((al34 - ar34) / sqxy, (bl34 + br34) / sqxy);
        Complex p21 = p11.mul(rd), p22 = p12.mul(rd), p23 = p13.mul(rd), p24 = p14.mul(rd);
        tan[0][0] = a.add(p14); tan[1][0] = b.add(p24);
        if (num == 1) return tan;
        tan[0][1] = a.add(p13); tan[1][1] = b.add(p23);
        if (num == 2) return tan;
        tan[0][2] = a.add(p12); tan[1][2] = b.sub(p22);
        if (num == 3) return tan;
        tan[0][3] = a.add(p11); tan[1][3] = b.sub(p21);
        return tan;
    }
    // http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=CGL_7_E
    public static int commonPointNum(Circle c1, Circle c2) {
        return 2 - Math.abs(commonTangentNum(c1, c2) - 2);
    }
    // http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=CGL_7_E
    public static Complex[] commonPoint(Circle c1, Circle c2) {
        int num = commonPointNum(c1, c2);
        if (num == 0) {
            return new Complex[]{};
        }
        Complex a = c1.c, b = c2.c;
        double r = c1.r, s = c2.r;
        Complex ab = b.sub(a);
        double d = ab.abs();
        double x = (d * d + r * r - s * s) / (2. * d);
        Complex h = a.add(ab.mul(x / d));
        Complex t = Complex.ofCis(ab.arg() + PI * 0.5).mul(Math.sqrt(Math.max(0., r * r - x * x)));
        Complex p1 = h.add(t);
        Complex p2 = h.sub(t);
        if (num == 2) {
            return new Complex[]{p1, p2};
        } else {
            return new Complex[]{midPoint(p1, p2)};
        }
    }
    // http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=CGL_1_A
    public static Complex projection(Complex c, Line l) {
        Complex a = l.a, b = l.b;
        Complex ab = b.sub(a), ac = c.sub(a);
        return a.add(ab.mul(ab.dot(ac) / ab.absSq()));
    }
    // http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=CGL_1_B
    public static Complex reflection(Complex c, Line l) {
        Complex h = projection(c, l);
        return h.add(h.sub(c));
    }
    // not verified.
    public static double intersectionArea(Circle c1, Circle c2) {
        double r = c1.r, s = c2.r;
        if (r < s) {
            return intersectionArea(c2, c1);
        }
        Complex a = c1.c, b = c2.c;
        double d = a.sub(b).abs();
        if (sgn(d - (r + s)) >= 0) {
            return 0;
        } else if (sgn(d - (r - s)) <= 0) {
            return PI * s * s;
        } else {
            double x = (d * d + r * r - s * s) / (2. * d);
            double h = Math.sqrt(Math.max(r * r - x * x, 0.));
            double a1 = r * r * Math.acos(x / r);
            double a2 = s * s * Math.acos((d - x) / s);
            double a12 = d * h;
            return a1 + a2 - a12;
        }
    }

    public static Complex pG(Complex a, Complex b, Complex c) {
        return Complex.sum(a, b, c).div(3);
    }
    public static Complex pH(Complex a, Complex b, Complex c) {
        Complex o = pO(a, b, c).c;
        Complex oa = a.sub(o), ob = b.sub(o), oc = c.sub(o);
        return Complex.sum(o, oa, ob, oc);
    }
    // http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=CGL_7_B
    public static Circle pI(Complex a, Complex b, Complex c) {
        double la = b.sub(c).abs(), lb = c.sub(a).abs(), lc = a.sub(b).abs();
        double l = la + lb + lc; la /= l; lb /= l; lc /= l;
        Complex ctr = Complex.sum(a.mul(la), b.mul(lb), c.mul(lc));
        double r = 2. * unsignedArea(a, b, c) / l;
        return new Circle(ctr, r);
    }
    // http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=CGL_7_C
    public static Circle pO(Complex a, Complex b, Complex c) {
        Complex ab = b.sub(a), bc = c.sub(b), ca = a.sub(c);
        double la = bc.absSq(), lb = ca.absSq(), lc = ab.absSq();
        double s = la * (lb + lc - la);
        double t = lb * (lc + la - lb);
        double u = lc * (la + lb - lc);
        double l = s + t + u; s /= l; t /= l; u /= l;
        Complex ctr = Complex.sum(a.mul(s), b.mul(t), c.mul(u));
        double r = ctr.sub(a).abs();
        return new Circle(ctr, r);
    }
    public static Circle[] pIabc(Complex a, Complex b, Complex c) {
        Circle[] pIabc = new Circle[3];
        pIabc[0] = pI(a.negate(), b, c);
        pIabc[1] = pI(a, b.negate(), c);
        pIabc[2] = pI(a, b, c.negate());
        return pIabc;
    }

    public static double signedArea(Complex a, Complex b, Complex c) {
        Complex ab = b.sub(a), ac = c.sub(a);
        return ab.det(ac) / 2.;
    }
    public static double unsignedArea(Complex a, Complex b, Complex c) {
        Complex ab = b.sub(a), ac = c.sub(a);
        return Math.abs(ab.det(ac) / 2.);
    }
    // http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=CGL_4_C
    public static double leftCutArea(Polygon p, Line l) {
        Complex v = l.a, w = l.b;
        int n = p.n;
        boolean allLeft = true, allRight = true;
        Complex a = null, b = null;
        for (int i = 0; i < n; i++) {
            allLeft &= isp(v, w, p.get(i)) != RIGHT_CURVE;
            allRight &= isp(v, w, p.get(i)) != LEFT_CURVE;
            Line e = Line.ofSegment(p.get(i), p.get(i + 1));
            Complex c = crossPoint(e, l);
            if (c == null) continue;
            if (a == null) {
                a = c;
            } else if (!a.equals(c)) {
                b = c;
            }
        }
        if (allLeft) return p.area();
        if (allRight) return 0;
        if (sgn(b.sub(a).dot(w.sub(v))) < 0) {
            Complex tmp = a; a = b; b = tmp;
        }
        double s = a.det(b);
        for (int i = 0; i < n; i++) {
            Complex c = p.get(i);
            Complex d = p.get(i + 1);
            int abc = isp(a, b, c), abd = isp(a, b, d);
            if (abc == -1 && abd == -1) continue;
            if (abc == -1) {
                s += b.det(d);
            } else if (abd == -1) {
                s += c.det(a);
            } else {
                s += c.det(d);
            }
        }
        return s /= 2;
    }
    // http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=CGL_7_H
    public static double intersectionArea(Polygon poly, Circle cir) {
        double r2 = cir.r * cir.r;
        Complex c = cir.c;
        double area = 0;
        for (int i = 0; i < poly.n; i++) {
            Complex a = poly.get(i), b = poly.get(i + 1);
            boolean ina = cir.in(a), inb = cir.in(b);
            Complex ca = a.sub(c), cb = b.sub(c);
            if (ina & inb) {
                area += ca.det(cb);
                continue;
            }
            Complex[] ps = commonPoint(Line.ofSegment(a, b), cir);
            if (ps.length == 0) {
                area += r2 * cb.div(ca).arg();
            } else {
                Complex s = ps[0];
                Complex t = ps.length == 1 ? s : ps[1];
                if (sgn(t.sub(a).abs() - s.sub(a).abs()) < 0) {
                    Complex tmp = s; s = t; t = tmp;
                }
                Complex cs = s.sub(c), ct = t.sub(c);
                area += cs.det(ct);
                if (ina) {
                    area += ca.det(cs);
                } else {
                    area += r2 * cs.div(ca).arg();
                }
                if (inb) {
                    area += ct.det(cb);
                } else {
                    area += r2 * cb.div(ct).arg();
                }
            }
        }
        return area / 2;
    }
}
