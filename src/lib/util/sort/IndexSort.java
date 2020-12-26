package lib.util.sort;

import java.util.Arrays;
import java.util.Comparator;

import lib.util.sort.ints.IntMergeSortUsingComparator;

public final class IndexSort {
    private static int[] indecies(int n) {
        int[] ret = new int[n];
        Arrays.setAll(ret, i -> i);
        return ret;
    }
    public static int[] sort(int[] a) {
        int[] index = indecies(a.length);
        IntMergeSortUsingComparator.sort(index, (i, j) -> a[i] - a[j]);
        return index;
    }
    public static int[] sortDesc(int[] a) {
        int[] index = indecies(a.length);
        IntMergeSortUsingComparator.sort(index, (i, j) -> a[j] - a[i]);
        return index;
    }
    public static int[] sort(long[] a) {
        int[] index = indecies(a.length);
        IntMergeSortUsingComparator.sort(index, (i, j) -> Long.compare(a[i], a[j]));
        return index;
    }
    public static int[] sortDesc(long[] a) {
        int[] index = indecies(a.length);
        IntMergeSortUsingComparator.sort(index, (i, j) -> Long.compare(a[j], a[i]));
        return index;
    }
    public static <T extends Comparable<T>> int[] sort(T[] a) {
        int[] index = indecies(a.length);
        IntMergeSortUsingComparator.sort(index, (i, j) -> a[i].compareTo(a[j]));
        return index;
    }
    public static <T> int[] sort(T[] a, Comparator<T> comparator) {
        int[] index = indecies(a.length);
        IntMergeSortUsingComparator.sort(index, (i, j) -> comparator.compare(a[i], a[j]));
        return index;
    }
}