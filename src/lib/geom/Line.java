package lib.geom;

/**
 * @author https://atcoder.jp/users/suisen
 */
public class Line {
    public enum Type {
        LINE,
        RAY,
        SEGMENT
    }

    public final Complex a, b;
    public final Type lineType;
    private Line(Complex a, Complex b, Type lineType) {
        this.a = a;
        this.b = b;
        this.lineType = lineType;
    }
    public static Line ofSegment(Complex endPoint1, Complex endPoint2) {
        return new Line(endPoint1, endPoint2, Type.SEGMENT);
    }
    public static Line ofRay(Complex endPoint, Complex passingPoint) {
        return new Line(endPoint, passingPoint, Type.RAY);
    }
    public static Line ofLine(Complex passingPoint1, Complex passingPoint2) {
        return new Line(passingPoint1, passingPoint2, Type.LINE);
    }
    public boolean isSegment() {
        return lineType == Type.SEGMENT;
    }
    public boolean isRay() {
        return lineType == Type.RAY;
    }
    public boolean isLine() {
        return lineType == Type.LINE;
    }
    public boolean isParallel(Line l) {
        return isParallel(this, l);
    }
    public boolean isOrthogonal(Line l) {
        return isOrthogonal(this, l);
    }
    public boolean isSame(Line l) {
        return isSame(this, l);
    }
    public boolean onRangeXY(Complex c) {
        return onRangeXY(this, c);
    }
    public boolean on(Complex c) {
        return on(this, c);
    }
    // http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=CGL_2_A
    public static boolean isParallel(Line l1, Line l2) {
        Complex a = l1.a, b = l1.b, c = l2.a, d = l2.b;
        Complex ab = b.sub(a), cd = d.sub(c);
        return Complex.sgn(ab.det(cd)) == 0;
    }
    // http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=CGL_2_A
    public static boolean isOrthogonal(Line l1, Line l2) {
        Complex a = l1.a, b = l1.b, c = l2.a, d = l2.b;
        Complex ab = b.sub(a), cd = d.sub(c);
        return Complex.sgn(ab.dot(cd)) == 0;
    }
    // http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=CGL_2_A
    public static boolean isSame(Line l1, Line l2) {
        Complex a = l1.a, b = l1.b, c = l2.a, d = l2.b;
        Complex ab = b.sub(a), cd = d.sub(c), ac = c.sub(a);
        return Complex.sgn(ab.det(cd)) == 0 && Complex.sgn(ab.det(ac)) == 0;
    }
    private static boolean onRangeX(Line l, Complex c) {
        if (l.isLine()) return true;
        double ax = l.a.x, bx = l.b.x, cx = c.x;
        int sgndx = Complex.sgn(bx - ax);
        if (sgndx == 0) return Complex.sgn(cx - ax) == 0;
        if (sgndx * Complex.sgn(cx - ax) < 0) return false;
        if (l.isRay()) return true;
        return sgndx * Complex.sgn(bx - cx) >= 0;
    }
    private static boolean onRangeY(Line l, Complex c) {
        if (l.isLine()) return true;
        double ay = l.a.y, by = l.b.y, cy = c.y;
        int sgndy = Complex.sgn(by - ay);
        if (sgndy == 0) return Complex.sgn(cy - ay) == 0;
        if (sgndy * Complex.sgn(cy - ay) < 0) return false;
        if (l.isRay()) return true;
        return sgndy * Complex.sgn(by - cy) >= 0;
    }
    public static boolean onRangeXY(Line l, Complex c) {
        return onRangeX(l, c) && onRangeY(l, c);
    }
    public static boolean on(Line l, Complex c) {
        if (!onRangeXY(l, c)) return false;
        double det = l.a.sub(c).det(l.b.sub(c));
        return Complex.sgn(det) == 0;
    }
}
