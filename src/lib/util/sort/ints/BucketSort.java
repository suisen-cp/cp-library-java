package lib.util.sort.ints;

import java.util.Arrays;

public final class BucketSort {
    public static void sort(int[] a) {
        sort(a, 0, a.length);
    }
    public static void sortDesc(int[] a) {
        sortDesc(a, 0, a.length);
    }
    public static void sort(int[] a, int from, int to) {
        int max = 0;
        for (int i = from; i < to; i++) max = Math.max(a[i], max);
        int[] bucket = new int[max + 1];
        for (int i = from; i < to; i++) bucket[a[i]]++;
        for (int val = 0, i = from; val <= max; val++) {
            Arrays.fill(a, i, i += bucket[val], val);
        }
    }
    public static void sortDesc(int[] a, int from, int to) {
        int max = 0;
        for (int i = from; i < to; i++) max = Math.max(a[i], max);
        int[] bucket = new int[max + 1];
        for (int i = from; i < to; i++) bucket[a[i]]++;
        for (int val = max, i = from; val >= 0; val--) {
            Arrays.fill(a, i, i += bucket[val], val);
        }
    }
}