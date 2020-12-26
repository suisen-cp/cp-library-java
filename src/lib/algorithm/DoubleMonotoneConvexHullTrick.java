package lib.algorithm;

import lib.base.Const;
import lib.util.collections.Deque;

public class DoubleMonotoneConvexHullTrick {
    private static final class Func {
        private final double a, b;
        private Func(double a, double b) {
            this.a = a; this.b = b;
        }
        public double f(double x) {
            return a * x + b;
        }
    }
    private final Deque<Func> lines;
    private final boolean isMinQuery;
    public DoubleMonotoneConvexHullTrick(boolean isMinQuery) {
        this.isMinQuery = isMinQuery;
        this.lines = new Deque<>();
    }
    public DoubleMonotoneConvexHullTrick(boolean isMinQuery, int lineNum) {
        this.isMinQuery = isMinQuery;
        this.lines = new Deque<>(lineNum);
    }
    public void addLine(double a, double b) {
        if (!isMinQuery) {
            a = -a; b = -b;
        }
        if (lines.size() == 0) {
            lines.addLast(new Func(a, b));
            return;
        }
        Func last = lines.getLast();
        Func first = lines.getFirst();
        Func f = new Func(a, b);
        if (a <= last.a) {
            if (a < last.a || b < last.b) addLast(f);
        } else if (a >= first.a) {
            if (a > first.a || b > first.b) addFirst(f);
        } else {
            throw new IllegalArgumentException("Not monotone.");
        }
    }
    private void addLast(Func f) {
        for (int size = lines.size(); size >= 2; size--) {
            Func lf = lines.get(size - 2);
            Func mf = lines.get(size - 1);
            if (!noneed(lf, mf, f)) break;
            lines.removeLast();
        }
        lines.addLast(f);
    }
    private void addFirst(Func f) {
        for (int size = lines.size(); size >= 2; size--) {
            Func mf = lines.get(0);
            Func rf = lines.get(1);
            if (!noneed(f, mf, rf)) break;
            lines.removeFirst();
        }
        lines.addFirst(f);
    }
    private static boolean noneed(Func lf, Func mf, Func rf) {
        return (lf.b - mf.b) / (mf.a - lf.a) >= (mf.b - rf.b) / (rf.a - mf.a);
    }
    private boolean incrFlg = false;
    private boolean decrFlg = false;
    private double incrLastX = -Const.DINF;
    private double decrLastX = Const.DINF;
    public double query(double x) {
        if (incrFlg || decrFlg) throw new UnsupportedOperationException();
        if (lines.size() == 0) return isMinQuery ? Const.DINF : -Const.DINF;
        int l = -1, r = lines.size() - 1;
        while (r - l > 1) {
            int m = (l + r) >> 1;
            if (lines.get(m).f(x) > lines.get(m + 1).f(x)) l = m;
            else r = m;
        }
        double y = lines.get(r).f(x);
        return isMinQuery ? y : -y;
    }
    public double increasingQuery(double x) {
        if (decrFlg) throw new UnsupportedOperationException();
        if (incrLastX > (incrLastX = x)) throw new IllegalArgumentException();
        incrFlg = true;
        if (lines.size() == 0) return isMinQuery ? Const.DINF : -Const.DINF;
        for (int size = lines.size(); size >= 2; size--) {
            if (lines.get(0).f(x) <= lines.get(1).f(x)) break;
            lines.removeFirst();
        }
        double y = lines.getFirst().f(x);
        return isMinQuery ? y : -y;
    }
    public double decreasingQuery(double x) {
        if (incrFlg) throw new UnsupportedOperationException();
        if (decrLastX < (decrLastX = x)) throw new IllegalArgumentException();
        decrFlg = true;
        if (lines.size() == 0) return isMinQuery ? Const.DINF : -Const.DINF;
        for (int size = lines.size(); size >= 2; size--) {
            if (lines.get(size - 2).f(x) >= lines.get(size - 1).f(x)) break;
            lines.removeLast();
        }
        double y = lines.getLast().f(x);
        return isMinQuery ? y : -y;
    }
}
