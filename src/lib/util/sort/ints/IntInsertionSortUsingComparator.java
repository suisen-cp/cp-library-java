package lib.util.sort.ints;

import lib.util.function.IntComparator;

final class IntInsertionSortUsingComparator {
    static void sort(int[] a, int from, int to, IntComparator comparator) {
        for (int i = from + 1; i < to; i++) {
            int tmp = a[i];
            if (comparator.gt(a[i - 1], tmp)) {
                int j = i;
                do {a[j] = a[--j];} while (j > from && comparator.gt(a[j - 1], tmp));
                a[j] = tmp;
            }
        }
    }
}
