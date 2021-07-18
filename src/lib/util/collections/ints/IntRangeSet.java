package lib.util.collections.ints;

import java.util.Iterator;
import java.util.TreeSet;

public class IntRangeSet implements Iterable<IntRangeSet.ClosedRange> {
    public static final int INF = Integer.MAX_VALUE;

    public static class ClosedRange implements Comparable<ClosedRange> {
        private int l, r;
        ClosedRange(int l, int r) { this.l = l; this.r = r; }
        public int getLeft() { return l; }
        public int getRight() { return r; }
        @Override
        public int compareTo(ClosedRange o) { return l > o.l ? 1 : l < o.l ? -1 : 0; }
        @Override
        public boolean equals(Object obj) { return obj instanceof ClosedRange && l == ((ClosedRange) obj).l; }
        @Override
        public int hashCode() { return l; }
        @Override
        public String toString() {
            return String.format("[%d, %d]", l, r);
        }
    }

    private final TreeSet<ClosedRange> set;

    public IntRangeSet() {
        this.set = new TreeSet<>();
        set.add(new ClosedRange(-INF, -INF));
        set.add(new ClosedRange(+INF, +INF));
    }

    public void clear() {
        set.clear();
        set.add(new ClosedRange(-INF, -INF));
        set.add(new ClosedRange(+INF, +INF));
    }

    public ClosedRange getInclusingRange(int x) {
        assert -INF < x && x < +INF;
        ClosedRange m = new ClosedRange(x, x);
        ClosedRange l = set.floor(m);
        return x <= l.r ? l : null;
    }

    public boolean contains(int x) {
        return getInclusingRange(x) != null;
    }

    public boolean add(int x) {
        assert -INF < x && x < +INF;
        ClosedRange m = new ClosedRange(x, x);
        ClosedRange l = set.floor(m);
        if (x <= l.r) return false;
        ClosedRange r = set.ceiling(m);
        int lx = x, rx = x;
        if (l.r + 1 == x) {
            set.remove(l);
            lx = l.l;
        }
        if (x + 1 == r.l) {
            set.remove(r);
            rx = r.r;
        }
        set.add(new ClosedRange(lx, rx));
        return true;
    }

    public boolean remove(int x) {
        assert -INF < x && x < +INF;
        ClosedRange m = new ClosedRange(x, x);
        ClosedRange l = set.floor(m);
        if (l.r < x) return false;
        if (x + 1 <= l.r) {
            set.add(new ClosedRange(x + 1, l.r));
        }
        if (l.l <= x - 1) {
            l.r = x - 1;
        } else {
            set.remove(l);
        }
        return true;
    }

    /**
     * @param l <b>closed</b>
     * @param r <b>closed</b>
     * @return {@code true} if this set contains all of {l, ..., r}.
     */
    public boolean containsAll(int l, int r) {
        assert -INF < l && r < +INF;
        if (r < l) return false;
        ClosedRange m = new ClosedRange(l, l);
        ClosedRange f = set.floor(m);
        return r <= f.r;
    }

    /**
     * @param l <b>closed</b>
     * @param r <b>closed</b>
     * @return the number of added elements.
     */
    public int addAll(int l, int r) {
        assert -INF < l && r < +INF;
        if (r < l) return 0;
        ClosedRange m = new ClosedRange(l, r);
        ClosedRange f = set.floor(m);
        if (m.r <= f.r) return 0;
        if (m.l - 1 <= f.r) {
            set.remove(f);
            m.l = f.l;
            l = f.r + 1;
        }
        int added = 0;
        while (true) {
            ClosedRange c = set.ceiling(m);
            if (m.r < c.l - 1) {
                added += m.r - l + 1;
                break;
            } else {
                added += c.l - l;
                set.remove(c);
                l = c.r + 1;
                if (l > m.r) {
                    m.r = c.r;
                    break;
                }
            }
        }
        set.add(m);
        return added;
    }

    /**
     * @param l <b>closed</b>
     * @param r <b>closed</b>
     * @return the number of removed elements.
     */
    public int removeAll(int l, int r) {
        assert -INF < l && r < +INF;
        if (r < l) return 0;
        ClosedRange m = new ClosedRange(l, r);
        ClosedRange f = set.floor(m);
        if (m.r <= f.r) {
            if (m.r + 1 <= f.r) {
                set.add(new ClosedRange(m.r + 1, f.r));
            }
            if (f.l <= m.l - 1) {
                f.r = m.l - 1;
            } else {
                set.remove(f);
            }
            return m.r - m.l + 1;
        }
        int removed = 0;
        if (m.l <= f.r) {
            removed += f.r - m.l + 1;
            if (f.l <= m.l - 1) {
                f.r = m.l - 1;
            } else {
                set.remove(f);
            }
        }
        while (true) {
            ClosedRange c = set.ceiling(m);
            if (m.r < c.l) {
                break;
            } else {
                if (m.r >= c.r) {
                    removed += c.r - c.l + 1;
                    set.remove(c);
                } else {
                    removed += m.r - c.l + 1;
                    set.remove(c);
                    c.l = m.r + 1;
                    set.add(c);
                    break;
                }
            }
        }
        return removed;
    }

    /**
     * @param l <b>closed</b>
     * @param r <b>closed</b>
     * @return the number of common elements between this set and the set {l, ..., r}.
     */
    public int intersectionSize(int l, int r) {
        assert -INF < l && r < +INF;
        if (r < l) return 0;
        ClosedRange m = new ClosedRange(l, r);
        ClosedRange f = set.floor(m);
        int intersection = 0;
        if (m.l <= f.r) {
            intersection += f.r - m.l + 1;
            m.l = f.r + 1;
        }
        while (true) {
            ClosedRange c = set.ceiling(m);
            if (m.r < c.l) {
                break;
            } else {
                if (m.r >= c.r) {
                    intersection += c.r - c.l + 1;
                    m.l = c.r + 1;
                } else {
                    intersection += m.r - c.l + 1;
                    break;
                }
            }
        }
        return intersection;
    }

    public int minimumIncludedCeiling(int x) {
        ClosedRange m = new ClosedRange(x, x);
        ClosedRange f = set.floor(m);
        if (x <= f.r) {
            return x;
        } else {
            ClosedRange c = set.ceiling(m);
            return c.l;
        }
    }

    public int minimumIncludedHigher(int x) {
        assert x < INF;
        return minimumIncludedCeiling(x + 1);
    }

    public int maximumIncludedFloor(int x) {
        ClosedRange m = new ClosedRange(x, x);
        ClosedRange f = set.floor(m);
        if (x <= f.r) {
            return x;
        } else {
            return m.r;
        }
    }

    public int maximumIncludedLower(int x) {
        assert -INF < x;
        return maximumExcludedFloor(x - 1);
    }

    public int minimumExcludedCeiling(int x) {
        ClosedRange m = new ClosedRange(x, x);
        ClosedRange f = set.floor(m);
        if (x <= f.r) {
            return f.r + 1;
        } else {
            return x;
        }
    }

    public int minumumExcludedHigher(int x) {
        assert x < +INF;
        return minimumExcludedCeiling(x + 1);
    }

    public int maximumExcludedFloor(int x) {
        ClosedRange m = new ClosedRange(x, x);
        ClosedRange f = set.floor(m);
        if (x <= f.r) {
            return f.l - 1;
        } else {
            return x;
        }
    }

    public int maximumExcludedLower(int x) {
        assert -INF < x;
        return maximumExcludedFloor(x - 1);
    }

    @Override
    public Iterator<ClosedRange> iterator() {
        return set.subSet(new ClosedRange(-INF + 1, -INF + 1), new ClosedRange(+INF, +INF)).iterator();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ClosedRange segment : set) {
            if (segment.equals(new ClosedRange(-INF, -INF))) continue;
            if (segment.equals(new ClosedRange(+INF, +INF))) continue;
            int l = segment.l == -INF ? -INF + 1 : segment.l;
            int r = segment.r == +INF ? +INF - 1 : segment.r;
            sb.append('[').append(l).append(',').append(r).append(']');
        }
        return sb.toString();
    }
}
