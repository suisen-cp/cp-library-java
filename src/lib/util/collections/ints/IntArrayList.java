package lib.util.collections.ints;

import java.util.Arrays;
import java.util.PrimitiveIterator;

import lib.util.array.IntArrays;

/**
 * @author https://atcoder.jp/users/suisen
 */
public final class IntArrayList implements Iterable<Integer>, Cloneable {
    private int[] a;
    private int tail = 0;
    private static final int DEFAULT_SIZE = 64;
    public IntArrayList(int capacity) {this.a = new int[Math.max(1, capacity)];}
    public IntArrayList() {this(DEFAULT_SIZE);}
    public void add(int v) {
        if (tail == a.length) grow();
        a[tail++] = v;
    }
    public int removeLast() {return a[--tail];}
    public int get(final int i) {
        if (i >= tail) throw new IndexOutOfBoundsException("Index " + i + " out of bounds for length " + size());
        return a[i];
    }
    public void set(final int i, final int v) {
        if (i >= tail) throw new IndexOutOfBoundsException("Index " + i + " out of bounds for length " + size());
        a[i] = v;
    }
    private void grow() {a = java.util.Arrays.copyOf(a, a.length << 1);}
    public int size() {return tail;}
    public void clear() {tail = 0;}
    public void reverse(final int begin, final int end) {IntArrays.reverse(a, begin, end);}
    public void reverse() {IntArrays.reverse(a, 0, tail);}
    public int[] toArray() {return java.util.Arrays.copyOf(a, tail);}
    public void sort() {Arrays.sort(a, 0, tail);}
    public boolean addIf(int v, boolean b) {
        if (!b) return false;
        add(v);
        return true;
    }
    public int removeIf(int from, int to, java.util.function.IntPredicate p) {
        int idx = from;
        for (int i = from; i < to; i++) {
            if (!p.test(a[i])) {
                a[idx++] = a[i];
            }
        }
        for (int i = to; i < tail; i++) {
            a[idx++] = a[i];
        }
        return tail = idx;
    }
    public int removeIf(java.util.function.IntPredicate p) {
        return removeIf(0, tail, p);
    }
    public IntArrayList clone() {
        IntArrayList copy = new IntArrayList(a.length);
        System.arraycopy(a, 0, copy.a, 0, a.length);
        copy.tail = tail;
        return copy;
    }
    public PrimitiveIterator.OfInt iterator() {return new IntArrayListIterator();}
    private class IntArrayListIterator implements PrimitiveIterator.OfInt {
        private int i = 0;
        public boolean hasNext() {return i < tail;}
        public int nextInt() {return a[i++];}
    }
}