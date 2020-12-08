package lib.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;

/**
 * @author https://atcoder.jp/users/suisen
 */
public final class Range {
    private static final Range EMPTY = new Range(0, 0);
    public long l, r;
    public Range(final long l, final long r) {
        if (r <= l) {this.l = 0; this.r = 0;}
        else {this.l = l; this.r = r;}
    }
    public long length() {return r - l;}
    public boolean contains(final long v) {return in(l, v, r);}
    public boolean contains(final Range r) {return contains(this.l, this.r, r.l, r.r);}
    public boolean crosses(final Range r) {return crosses(this.l, this.r, r.l, r.r);}
    public Range crossRange(final Range r) {
        if (crosses(r)) return new Range(Math.max(this.l, r.l), Math.min(this.r, r.r));
        return EMPTY;
    }
    /**
     * this is a DESTRUCTIVE method. receive a list of {@code Range} and unite them.
     * including some {@code Iterator.remove()}, requires being LinkedList.
     * @param list   LinkedList.
     * @param sorted if the list is already sorted.
     */
    public static void unionRange(final LinkedList<Range> list, final boolean sorted) {
        if (!sorted) list.sort(Comparator.comparingLong(r -> r.l));
        final Iterator<Range> iter = list.iterator();
        if (iter.hasNext()) {
            Range now = iter.next();
            while (iter.hasNext()) {
                final Range r = iter.next();
                if (now.crosses(r)) {
                    now.r = Math.max(now.r, r.r);
                    iter.remove();
                } else now = r;
            }
        }
    }
    /**
     * this is a DESTRUCTIVE method. receive a list of {@code Range} and unite them.
     * including some {@code Iterator.remove()}, requires being LinkedList.
     * @param list (unsorted) LinkedList.
     */
    public static void unionRange(final LinkedList<Range> list) {unionRange(list, false);}
    /**
     * this is a DESTRUCTIVE method. receive a list of {@code Range}, unite them,
     * and calculate the sum of length. including some {@code Iterator.remove()},
     * requires being LinkedList.
     * @param list   LinkedList.
     * @param sorted if the list is already sorted.
     * @return sum of length of Ranges.
     */
    public static long lengthSum(final LinkedList<Range> list, final boolean sorted) {
        unionRange(list, sorted);
        long ret = 0;
        for (final Range r : list) ret += r.length();
        return ret;
    }
    /**
     * this is a DESTRUCTIVE method. receive a list of {@code Range}, unite them,
     * and calculate the sum of length. including some {@code Iterator.remove()},
     * requires being LinkedList.
     * @param list (unsorted) LinkedList.
     * @return sum of length of Ranges.
     */
    public static long lengthSum(final LinkedList<Range> list) {return lengthSum(list, false);}
    /**
     * judge value is in [l, r).
     * @param l     closed.
     * @param value evaluated value.
     * @param r     open.
     * @return value is in [l, r)
     */
    public static boolean in(final long l, final long value, final long r) {return l <= value && value < r;}
    /**
     * judge value is out of [l, r). (i.e. value is in (-Const. l) or [r, Const.)
     * @param l     closed.
     * @param value evaluated value.
     * @param r     open.
     * @return value is out of [l, r) (i.e. value is in (-Const. l) or [r, Const.)
     */
    public static boolean outOf(final long l, final long value, final long r) {return !in(l, value, r);}
    public static boolean crosses(final long l1, final long r1, final long l2, final long r2) {return l1 < r2 && l2 < r1;}
    public static boolean contains(final long l1, final long r1, final long l2, final long r2) {return l1 <= l2 && r2 <= r1;}
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Range)) return false;
        final Range r = (Range) o;
        return this.l == r.l && this.r == r.r;
    }
    @Override
    public int hashCode() {return Objects.hash(l, r);}
    @Override
    public String toString() {return "[" + l + ", " + r + ")";}
}