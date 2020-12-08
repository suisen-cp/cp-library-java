package lib.util.itertools;

import java.util.Arrays;
import java.util.Iterator;

public final class Product implements Iterable<int[]> {
    private final int m, n;
    public Product(final int n, final int repeat) {this.m = repeat; this.n = n;}
    public Iterator<int[]> iterator() {return new ForLoopIterator();}
    private class ForLoopIterator implements Iterator<int[]> {
        private final int[] a = new int[m];
        private ForLoopIterator() {a[m - 1] = -1;}
        public boolean hasNext() {
            int i = m - 1;
            while (a[i] >= n - 1) if (i-- <= 0) return false;
            a[i]++;
            Arrays.fill(a, i + 1, m, 0);
            return true;
        }
        public int[] next() {return a;}
    }
}