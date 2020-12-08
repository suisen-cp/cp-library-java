package lib.algorithm;

import lib.base.Const;
import lib.datastructure.longs.LongOrderedMap;
import lib.util.pair.LongEntry;

public class ConvexHullTrick {
    private final LongOrderedMap<Long> lines;
    private final boolean isMinQuery;
    public ConvexHullTrick(boolean isMinQuery) {
        this.isMinQuery = isMinQuery;
        this.lines = new LongOrderedMap<>();
    }
    public void addLine(long a, long b) {
        if (!isMinQuery) {
            a = -a; b = -b;
        }
        if (lines.getOrDefault(a, Long.MAX_VALUE) > b) lines.put(a, b);
        LongEntry<Long> h = lines.higherEntry(a);
        LongEntry<Long> l = lines.lowerEntry(a);
        long al = l == null ? 0 : l.getKey();
        long bl = l == null ? 0 : l.getValue();
        final long am = a;
        final long bm = lines.get(a);
        long ar = h == null ? 0 : h.getKey();
        long br = h == null ? 0 : h.getValue();
        if (l != null && h != null && noneed(al, bl, am, bm, ar, br)) {
            lines.remove(a);
            return;
        }
        if (h != null) {
            while (true) {
                long am1 = ar;
                long bm1 = br;
                if ((h = lines.higherEntry(am1)) == null) break;
                ar = h.getKey();
                br = h.getValue();
                if (!noneed(am, bm, am1, bm1, ar, br)) break;
                lines.remove(am1);
            }
        }
        if (l != null) {
            while (true) {
                long am1 = al;
                long bm1 = bl;
                if ((l = lines.lowerEntry(am1)) == null) break;
                al = l.getKey();
                bl = l.getValue();
                if (!noneed(al, bl, am1, bm1, am, bm)) break;
                lines.remove(am1);
            }
        }
    }
    private static boolean noneed(long la, long lb, long ma, long mb, long ra, long rb) {
        return (lb - mb) / (ma - la) <= (mb - rb) / (ra - ma);
    }
    private static long apply(LongEntry<Long> f, long x) {
        return f.getKey() * x + f.getValue();
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
            if (apply(lines.kthEntry(m), x) > apply(lines.kthEntry(m + 1), x)) l = m;
            else r = m;
        }
        long y = apply(lines.kthEntry(r), x);
        return isMinQuery ? y : -y;
    }
    public long increasingQuery(long x) {
        if (decrFlg) throw new UnsupportedOperationException();
        if (incrLastX > (incrLastX = x)) throw new IllegalArgumentException();
        incrFlg = true;
        if (lines.size() == 0) return isMinQuery ? Const.LINF : -Const.LINF;
        for (int size = lines.size(); size >= 2; size--) {
            if (apply(lines.kthEntry(0), x) <= apply(lines.kthEntry(1), x)) break;
            lines.removeKthEntry(0);
        }
        long y = apply(lines.kthEntry(0), x);
        return isMinQuery ? y : -y;
    }
    public long decreasingQuery(long x) {
        if (incrFlg) throw new UnsupportedOperationException();
        if (decrLastX < (decrLastX = x)) throw new IllegalArgumentException();
        decrFlg = true;
        if (lines.size() == 0) return isMinQuery ? Const.LINF : -Const.LINF;
        for (int size = lines.size(); size >= 2; size--) {
            if (apply(lines.kthEntry(size - 2), x) >= apply(lines.kthEntry(size - 1), x)) break;
            lines.removeKthEntry(size - 1);
        }
        long y = apply(lines.kthEntry(lines.size() - 1), x);
        return isMinQuery ? y : -y;
    }
}
