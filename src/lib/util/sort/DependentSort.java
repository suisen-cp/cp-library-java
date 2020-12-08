package lib.util.sort;

import lib.util.sort.ints.IntRadixSort;
import lib.util.sort.longs.LongRadixSort;

public final class DependentSort {
    public static void sort(int[] a, Object... dependentArrays) {
        sort(a, false, dependentArrays);
    }
    public static void sortDesc(int[] a, Object... dependentArrays) {
        sort(a, true, dependentArrays);
    }
    private static void sort(int[] a, boolean descending, Object... dependentArrays) {
        int[] p;
        if (descending) {
            p = IndexSort.sortDesc(a); IntRadixSort.sortDesc(a);
        } else {
            p = IndexSort.sort(a); IntRadixSort.sort(a);
        }
        boolean[] settled = new boolean[a.length];
        boolean ok = true;
        for (Object obj : dependentArrays) {
            if (obj instanceof int[]) {
                permute(p, (int[]) obj, settled, ok);
            } else if (obj instanceof long[]) {
                permute(p, (long[]) obj, settled, ok);
            } else {
                throw new UnsupportedOperationException("Unsupported for the class; " + obj.getClass().getName());
            }
            ok = !ok;
        }
    }
    public static void sort(long[] a, Object... dependentArrays) {
        sort(a, false, dependentArrays);
    }
    public static void sortDesc(long[] a, Object... dependentArrays) {
        sort(a, true, dependentArrays);
    }
    private static void sort(long[] a, boolean descending, Object... dependentArrays) {
        int[] p;
        if (descending) {
            p = IndexSort.sortDesc(a); LongRadixSort.sortDesc(a);
        } else {
            p = IndexSort.sort(a); LongRadixSort.sort(a);
        }
        boolean[] settled = new boolean[a.length];
        boolean ok = true;
        for (Object obj : dependentArrays) {
            if (obj instanceof int[]) {
                permute(p, (int[]) obj, settled, ok);
            } else if (obj instanceof long[]) {
                permute(p, (long[]) obj, settled, ok);
            } else {
                throw new UnsupportedOperationException("Unsupported for the class; " + obj.getClass().getName());
            }
            ok = !ok;
        }
    }
    private static void permute(int[] p, int[] a, boolean[] settled, boolean flipped) {
        int n = p.length;
        for (int i = 0; i < n; i++) {
            for (int j = i; flipped ^ settled[j]; j = p[j]) {
                settled[j] = !settled[j];
                if (p[j] == i) break;
                swap(a, j, p[j]);
            }
        }
    }
    private static void permute(int[] p, long[] a, boolean[] settled, boolean flipped) {
        int n = p.length;
        for (int i = 0; i < n; i++) {
            for (int j = i; flipped ^ settled[j]; j = p[j]) {
                settled[j] = !settled[j];
                if (p[j] == i) break;
                swap(a, j, p[j]);
            }
        }
    }
    private static void swap(int[] a, int i, int j) {
        int tmp = a[i]; a[i] = a[j]; a[j] = tmp;
    }
    private static void swap(long[] a, int i, int j) {
        long tmp = a[i]; a[i] = a[j]; a[j] = tmp;
    }
}