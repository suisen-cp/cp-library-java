package lib.util.itertools;

import java.util.Arrays;
import java.util.Iterator;

public final class CombinationsWithReplacement implements Iterable<int[]> {
    private final int m, n;
    public CombinationsWithReplacement(final int n, final int repeat) {this.m = repeat; this.n = n;}
    public Iterator<int[]> iterator() {return new CombinationsWithReplacementIterator();}
    private class CombinationsWithReplacementIterator implements Iterator<int[]> {
        private final int[] a = new int[m];
        private CombinationsWithReplacementIterator() {a[m - 1] = -1;}
        public boolean hasNext() {
            int i = m - 1;
            while (a[i] >= n - 1) if (i-- <= 0) return false;
            a[i]++;
            Arrays.fill(a, i + 1, m, a[i]);
            return true;
        }
        public int[] next() {return a;}
    }
}