package lib.geom.light;

import lib.geom.Line;

/**
 * @author https://atcoder.jp/users/suisen
 */
public class LLine {
    public final LComplex a, b;
    public final Line.Type lineType;
    private LLine(LComplex a, LComplex b, Line.Type lineType) {
        this.a = a;
        this.b = b;
        this.lineType = lineType;
    }
    public static LLine ofSegment(LComplex endPoint1, LComplex endPoint2) {
        return new LLine(endPoint1, endPoint2, Line.Type.SEGMENT);
    }
    public static LLine ofRay(LComplex endPoint, LComplex passingPoint) {
        return new LLine(endPoint, passingPoint, Line.Type.RAY);
    }
    public static LLine ofLLine(LComplex passingPoint1, LComplex passingPoint2) {
        return new LLine(passingPoint1, passingPoint2, Line.Type.LINE);
    }
    public Line toLine() {
        if (isSegment()) return Line.ofSegment(a.toComplex(), b.toComplex());
        if (isRay()) return Line.ofRay(a.toComplex(), b.toComplex());
        return Line.ofLine(a.toComplex(), b.toComplex());
    }
    public boolean isSegment() {
        return lineType == Line.Type.SEGMENT;
    }
    public boolean isRay() {
        return lineType == Line.Type.RAY;
    }
    public boolean isLLine() {
        return lineType == Line.Type.LINE;
    }
    public boolean isParallel(LLine l) {
        return isParallel(this, l);
    }
    public boolean isOrthogonal(LLine l) {
        return isOrthogonal(this, l);
    }
    public boolean isSame(LLine l) {
        return isSame(this, l);
    }
    public boolean onRangeXY(LComplex c) {
        return onRangeXY(this, c);
    }
    public boolean on(LComplex c) {
        return on(this, c);
    }
    // http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=CGL_2_A
    public static boolean isParallel(LLine l1, LLine l2) {
        LComplex a = l1.a, b = l1.b, c = l2.a, d = l2.b;
        LComplex ab = b.sub(a), cd = d.sub(c);
        return ab.det(cd) == 0;
    }
    // http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=CGL_2_A
    public static boolean isOrthogonal(LLine l1, LLine l2) {
        LComplex a = l1.a, b = l1.b, c = l2.a, d = l2.b;
        LComplex ab = b.sub(a), cd = d.sub(c);
        return ab.dot(cd) == 0;
    }
    // http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=CGL_2_A
    public static boolean isSame(LLine l1, LLine l2) {
        LComplex a = l1.a, b = l1.b, c = l2.a, d = l2.b;
        LComplex ab = b.sub(a), cd = d.sub(c), ac = c.sub(a);
        return ab.det(cd) == 0 && ab.det(ac) == 0;
    }
    private static boolean onRangeX(LLine l, LComplex c) {
        if (l.isLLine()) return true;
        long ax = l.a.x, bx = l.b.x, cx = c.x;
        long dx = bx - ax;
        if (dx == 0) return cx - ax == 0;
        if (dx > 0 ^ cx < ax) return false;
        if (l.isRay()) return true;
        return dx > 0 == bx < cx;
    }
    private static boolean onRangeY(LLine l, LComplex c) {
        if (l.isLLine()) return true;
        long ay = l.a.y, by = l.b.y, cy = c.y;
        long dy = by - ay;
        if (dy == 0) return cy - ay == 0;
        if (dy > 0 ^ cy < ay) return false;
        if (l.isRay()) return true;
        return dy > 0 == by < cy;
    }
    public static boolean onRangeXY(LLine l, LComplex c) {
        return onRangeX(l, c) && onRangeY(l, c);
    }
    public static boolean on(LLine l, LComplex c) {
        if (!onRangeXY(l, c)) return false;
        long det = l.a.sub(c).det(l.b.sub(c));
        return det == 0;
    }
}
