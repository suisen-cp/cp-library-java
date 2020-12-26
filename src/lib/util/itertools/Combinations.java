package lib.util.itertools;

import java.util.Arrays;
import java.util.Iterator;

public final class Combinations implements Iterable<int[]> {
    private final int m, n;
    public Combinations(final int n, final int m) {this.m = m; this.n = n;}
    public Iterator<int[]> iterator() {return new CombinationsIterator();}
    private class CombinationsIterator implements Iterator<int[]> {
        private final int[] a = new int[m];
        private CombinationsIterator() {Arrays.setAll(a, i -> i); a[m - 1]--;}
        public boolean hasNext() {
            int i = m - 1;
            while (a[i] >= n - m + i) if (i-- <= 0) return false;
            a[i]++;
            for (int j = i + 1; j < m; j++) a[j] = a[j - 1] + 1;
            return true;
        }
        public int[] next() {return a;}
    }
}