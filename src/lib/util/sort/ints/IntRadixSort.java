package lib.util.sort.ints;

public class IntRadixSort {
    private static final int INT_INSERTION_SORT_THRESHOLD = 120;
    public static void sort(int[] a) {
        sort(a, 0, a.length);
    }
    public static void sortDesc(int[] a) {
        sortDesc(a, 0, a.length);
    }
    public static void sort(int[] a, int fr, int to) {
        if (to - fr <= INT_INSERTION_SORT_THRESHOLD) {
            IntInsertionSort.sort(a, fr, to);
            return;
        }
        int[] c0 = new int[0x101];
        int[] c1 = new int[0x101];
        int[] c2 = new int[0x101];
        int[] c3 = new int[0x101];
        c0[0] = c1[0] = c2[0] = c3[0] = fr;
        for (int i = fr; i < to; i++) {
            int v = a[i];
            c0[(v        & 0xff) + 1]++;
            c1[(v >>>  8 & 0xff) + 1]++;
            c2[(v >>> 16 & 0xff) + 1]++;
            c3[(v >>> 24 ^ 0x80) + 1]++;
        }
        for (int i = 0; i < 0x100; i++) {
            c0[i + 1] += c0[i];
            c1[i + 1] += c1[i];
            c2[i + 1] += c2[i];
            c3[i + 1] += c3[i];
        }
        int[] b = new int[a.length];
        for (int i = fr; i < to; i++) {int v = a[i]; b[c0[v        & 0xff]++] = v;}
        for (int i = fr; i < to; i++) {int v = b[i]; a[c1[v >>>  8 & 0xff]++] = v;}
        for (int i = fr; i < to; i++) {int v = a[i]; b[c2[v >>> 16 & 0xff]++] = v;}
        for (int i = fr; i < to; i++) {int v = b[i]; a[c3[v >>> 24 ^ 0x80]++] = v;}
    }
    public static void sortDesc(int[] a, int fr, int to) {
        sort(a, fr, to);
        int l = fr, r = to - 1;
        while (l < r) {int tmp = a[l]; a[l] = a[r]; a[r] = tmp; l++; r--;}
    }
}
