package lib.util.array;

/**
 * @author https://atcoder.jp/users/suisen
 */
public final class LongArrays {
    private LongArrays(){}
    public static void swap(long[] a, int u, int v) {
        long tmp = a[u]; a[u] = a[v]; a[v] = tmp;
    }
    public static void reverse(long[] a, int l, int r) {
        while (r - l > 1) swap(a, l++, --r);
    }
    public static void reverse(long[] a) {
        reverse(a, 0, a.length);
    }
    public static long sum(long[] a) {
        long sum = 0;
        for (long e : a) sum += e;
        return sum;
    }
    public static long max(long[] a) {
        long max = a[0];
        for (long e : a) max = Math.max(max, e);
        return max;
    }
    public static long min(long[] a) {
        long min = a[0];
        for (long e : a) min = Math.min(min, e);
        return min;
    }
    public static int argmax(long[] a) {
        long max = a[0];
        int argmax = 0;
        for (int i = 0; i < a.length; i++) if (max < a[i]) max = a[argmax = i];
        return argmax;
    }
    public static int argmin(long[] a) {
        long min = a[0];
        int argmin = 0;
        for (int i = 0; i < a.length; i++) if (min > a[i]) min = a[argmin = i];
        return argmin;
    }
    public static int find(long[] a, long v) {
        for (int i = 0; i < a.length; i++) if (a[i] == v) return i;
        return -1;
    }
    public static void permute(int[] p, long[] a) {
        int n = p.length;
        boolean[] settled = new boolean[n];
        for (int i = 0; i < n; i++) {
            for (int j = i; !settled[j]; j = p[j]) {
                if (p[j] == i) {settled[j] = true; break;}
                swap(a, j, p[j]);
                settled[j] = true;
            }
        }
    }
    public static void permute2(int[] p, long[] a, long[] b) {
        int n = p.length;
        boolean[] settled = new boolean[n];
        for (int i = 0; i < n; i++) {
            for (int j = i; !settled[j]; j = p[j]) {
                if (p[j] == i) {settled[j] = true; break;}
                swap(a, j, p[j]); swap(b, j, p[j]);
                settled[j] = true;
            }
        }
    }
    public static void permute3(int[] p, long[] a, long[] b, long[] c) {
        int n = p.length;
        boolean[] settled = new boolean[n];
        for (int i = 0; i < n; i++) {
            for (int j = i; !settled[j]; j = p[j]) {
                if (p[j] == i) {settled[j] = true; break;}
                swap(a, j, p[j]); swap(b, j, p[j]); swap(c, j, p[j]);
                settled[j] = true;
            }
        }
    }
    public static void permuteN(int[] p, long[]... as) {
        int n = p.length;
        boolean[] settled = new boolean[n];
        for (int i = 0; i < n; i++) {
            for (int j = i; !settled[j]; j = p[j]) {
                if (p[j] == i) {settled[j] = true; break;}
                for (long[] a : as) swap(a, j, p[j]);
                settled[j] = true;
            }
        }
    }
    public static int lowerBound(long[] sorted, long key) {
        int n = sorted.length;
        int l = -1, r = n;
        while (r - l > 1) {
            int m = (l + r) >> 1;
            if (sorted[m] < key) l = m;
            else r = m;
        }
        return r;
    }
    public static int upperBound(long[] sorted, long key) {
        int n = sorted.length;
        int l = -1, r = n;
        while (r - l > 1) {
            int m = (l + r) >> 1;
            if (sorted[m] <= key) l = m;
            else r = m;
        }
        return r;
    }
    public static int countOfRange(long[] sorted, long from, long to) {
        return lowerBound(sorted, to) - lowerBound(sorted, from);
    }
    public static String join(long[] a, String sep) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.length; i++) {
            sb.append(a[i]);
            if (i < a.length - 1) sb.append(sep);
        }
        return sb.toString();
    }
    public static String join(long[] a, char sep) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.length; i++) {
            sb.append(a[i]);
            if (i < a.length - 1) sb.append(sep);
        }
        return sb.toString();
    }
}