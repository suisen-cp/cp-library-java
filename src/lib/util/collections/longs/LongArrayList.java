package lib.util.collections.longs;

import java.util.Arrays;
import java.util.PrimitiveIterator;

import lib.util.array.LongArrays;

/**
 * @author https://atcoder.jp/users/suisen
 */
public final class LongArrayList implements Iterable<Long> {
    private long[] a;
    private int tail = 0;
    private static final int DEFAULT_SIZE = 64;
    public LongArrayList(int capacity) {this.a = new long[Math.max(1, capacity)];}
    public LongArrayList() {this(DEFAULT_SIZE);}
    public void add(long v) {
        if (tail == a.length) grow();
        a[tail++] = v;
    }
    public long removeLast() {return a[--tail];}
    public long get(int i) {
        if (i >= a.length) throw new IndexOutOfBoundsException("Index " + i + " out of bounds for length " + size());
        return a[i];
    }
    public void set(int i, long v) {
        if (i >= a.length) throw new IndexOutOfBoundsException("Index " + i + " out of bounds for length " + size());
        a[i] = v;
    }
    private void grow() {a = java.util.Arrays.copyOf(a, a.length << 1);}
    public int size() {return tail;}
    public void clear() {tail = 0;}
    public void reverse(int begin, int end) {LongArrays.reverse(a, begin, end);}
    public void reverse() {LongArrays.reverse(a, 0, tail);}
    public long[] toArray() {return java.util.Arrays.copyOf(a, tail);}
    public void sort() {Arrays.sort(a, 0, tail);}
    public boolean addIf(long v, boolean b) {
        if (!b) return false;
        add(v);
        return true;
    }
    public PrimitiveIterator.OfLong iterator() {return new LongArrayListIterator();}
    private class LongArrayListIterator implements PrimitiveIterator.OfLong {
        private int i = 0;
        public boolean hasNext() {return i < tail;}
        public long nextLong() {return a[i++];}
    }
}