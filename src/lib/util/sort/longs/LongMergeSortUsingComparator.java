package lib.util.sort.longs;

import lib.util.function.LongComparator;

public class LongMergeSortUsingComparator {
    private static final int INSERTION_SORT_THRESHOLD = 60;
    public static void sort(long[] a, LongComparator comparator) {
        sort(a, 0, a.length, comparator);
    }
    public static void sortDesc(long[] a, LongComparator comparator) {
        sortDesc(a, 0, a.length, comparator);
    }
    public static void sort(long[] a, int l, int r, LongComparator comparator) {
        for (int i = l;;) {
            int j = i + INSERTION_SORT_THRESHOLD;
            if (j < r) {
                LongInsertionSortUsingComparator.sort(a, i, i = j, comparator);
            } else {
                LongInsertionSortUsingComparator.sort(a, i, r, comparator);
                break;
            }
        }
        final int w = r - l;
        long[] work = new long[w];
        for (int size = INSERTION_SORT_THRESHOLD; size <= w; size <<= 1) {
            int size2 = size << 1;
            int blmax = r - size;
            for (int bl = l; bl < blmax; bl += size2) {
                int bm = bl + size;
                int br = Math.min(bl + size2, r);
                System.arraycopy(a, bl, work, 0, size);
                int ai = bl, li = 0, ri = bm;
                while (true) {
                    if (ri == br) {
                        System.arraycopy(work, li, a, ai, size - li);
                        break;
                    }
                    if (comparator.gt(work[li], a[ri])) {
                        a[ai++] = a[ri++];
                    } else {
                        a[ai++] = work[li++];
                        if (li == size) break;
                    }
                }
            }
        }
    }
    public static void sortDesc(long[] a, int from, int to, LongComparator comparator) {
        sort(a, from, to, comparator);
        int l = from, r = to - 1;
        while (l < r) {
            long tmp = a[l]; a[l] = a[r]; a[r] = tmp;
            l++; r--;
        }
    }
}
