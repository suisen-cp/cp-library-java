package lib.algorithm;

import lib.base.Const;
import lib.util.collections.Deque;

public class MonotoneConvexHullTrick {
    private static final class Func {
        private final long a, b;
        private Func(long a, long b) {
            this.a = a; this.b = b;
        }
        public long f(long x) {
            return a * x + b;
        }
    }
    private final Deque<Func> lines;
    private final boolean isMinQuery;
    public MonotoneConvexHullTrick(boolean isMinQuery) {
        this.isMinQuery = isMinQuery;
        this.lines = new Deque<>();
    }
    public MonotoneConvexHullTrick(boolean isMinQuery, int lineNum) {
        this.isMinQuery = isMinQuery;
        this.lines = new Deque<>(lineNum);
    }
    public void addLine(long a, long b) {
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
        checkOne(f);
        lines.addLast(f);
    }
    private void addFirst(Func f) {
        for (int size = lines.size(); size >= 2; size--) {
            Func mf = lines.get(0);
            Func rf = lines.get(1);
            if (!noneed(f, mf, rf)) break;
            lines.removeFirst();
        }
        checkOne(f);
        lines.addFirst(f);
    }
    private void checkOne(Func f) {
        if (lines.size() == 1) {
            Func last = lines.getLast();
            if (f.a == last.a && f.b < last.b) lines.removeLast();
        }
    }
    private static boolean noneed(Func lf, Func mf, Func rf) {
        return (double) (lf.b - mf.b) / (mf.a - lf.a) >= (double) (mf.b - rf.b) / (rf.a - mf.a);
    }
    private boolean incrFlg = false;
    private boolean decrFlg = false;
    private long incrLastX = Long.MIN_VALUE;
    private long decrLastX = Long.MAX_VALUE;
    public long query(long x) {
        if (incrFlg || decrFlg) throw new UnsupportedOperationException();
        if (lines.size() == 0) return isMinQuery ? Const.LINF : -Const.LINF;
        int l = -1, r = lines.size() - 1;
        while (r - l > 1) {
            int m = (l + r) >> 1;
            if (lines.get(m).f(x) > lines.get(m + 1).f(x)) l = m;
            else r = m;
        }
        long y = lines.get(r).f(x);
        return isMinQuery ? y : -y;
    }
    public long increasingQuery(long x) {
        if (decrFlg) throw new UnsupportedOperationException();
        if (incrLastX > (incrLastX = x)) throw new IllegalArgumentException();
        incrFlg = true;
        if (lines.size() == 0) return isMinQuery ? Const.LINF : -Const.LINF;
        for (int size = lines.size(); size >= 2; size--) {
            if (lines.get(0).f(x) <= lines.get(1).f(x)) break;
            lines.removeFirst();
        }
        long y = lines.getFirst().f(x);
        return isMinQuery ? y : -y;
    }
    public long decreasingQuery(long x) {
        if (incrFlg) throw new UnsupportedOperationException();
        if (decrLastX < (decrLastX = x)) throw new IllegalArgumentException();
        decrFlg = true;
        if (lines.size() == 0) return isMinQuery ? Const.LINF : -Const.LINF;
        for (int size = lines.size(); size >= 2; size--) {
            if (lines.get(size - 2).f(x) >= lines.get(size - 1).f(x)) break;
            lines.removeLast();
        }
        long y = lines.getLast().f(x);
        return isMinQuery ? y : -y;
    }
}
