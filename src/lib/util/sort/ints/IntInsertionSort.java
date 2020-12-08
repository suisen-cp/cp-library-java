package lib.util.sort.ints;

class IntInsertionSort {
    static void sort(int[] a) {
        sort(a, 0, a.length);
    }
    static void sort(int[] a, int from, int to) {
        for (int i = from + 1; i < to; i++) {
            int tmp = a[i];
            if (a[i - 1] > tmp) {
                int j = i;
                do {a[j] = a[--j];} while (j > from && a[j - 1] > tmp);
                a[j] = tmp;
            }
        }
    }
}
