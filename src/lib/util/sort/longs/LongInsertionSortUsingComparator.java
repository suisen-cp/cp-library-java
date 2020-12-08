package lib.util.sort.longs;

import lib.util.function.LongComparator;

public class LongInsertionSortUsingComparator {
    static void sort(long[] a, int from, int to, LongComparator comparator) {
        for (int i = from + 1; i < to; i++) {
            long tmp = a[i];
            if (comparator.gt(a[i - 1], tmp)) {
                int j = i;
                do {a[j] = a[--j];} while (j > from && comparator.gt(a[j - 1], tmp));
                a[j] = tmp;
            }
        }
    }
}
