package lib.util.array;

/**
 * @author https://atcoder.jp/users/suisen
 */
public final class IntArrays {
    private IntArrays(){}
    public static void swap(int[] a, int u, int v) {
        int tmp = a[u]; a[u] = a[v]; a[v] = tmp;
    }
    public static void reverse(int[] a, int l, int r) {
        while (r - l > 1) swap(a, l++, --r);
    }
    public static void reverse(int[] a) {
        reverse(a, 0, a.length);
    }
    public static int sum(int[] a) {
        int sum = 0;
        for (int e : a) sum += e;
        return sum;
    }
    public static int max(int[] a) {
        int max = a[0];
        for (int e : a) max = Math.max(max, e);
        return max;
    }
    public static int min(int[] a) {
        int min = a[0];
        for (int e : a) min = Math.min(min, e);
        return min;
    }
    public static int argmax(int[] a) {
        int max = a[0], argmax = 0;
        for (int i = 0; i < a.length; i++) if (max < a[i]) max = a[argmax = i];
        return argmax;
    }
    public static int argmin(int[] a) {
        int min = a[0], argmin = 0;
        for (int i = 0; i < a.length; i++) if (min > a[i]) min = a[argmin = i];
        return argmin;
    }
    public static int find(int[] a, int v) {
        for (int i = 0; i < a.length; i++) if (a[i] == v) return i;
        return -1;
    }
    public static void permute(int[] p, int[] a) {
        int n = p.length;
        boolean[] settled = new boolean[n];
        for (int i = 0; i < n; i++) {
            for (int j = i; !settled[j]; j = p[j]) {
                settled[j] = true;
                if (p[j] == i) break;
                swap(a, j, p[j]);
            }
        }
    }
    public static void permute2(int[] p, int[] a, int[] b) {
        int n = p.length;
        boolean[] settled = new boolean[n];
        for (int i = 0; i < n; i++) {
            for (int j = i; !settled[j]; j = p[j]) {
                settled[j] = true;
                if (p[j] == i) break;
                swap(a, j, p[j]); swap(b, j, p[j]);
            }
        }
    }
    public static void permute3(int[] p, int[] a, int[] b, int[] c) {
        int n = p.length;
        boolean[] settled = new boolean[n];
        for (int i = 0; i < n; i++) {
            for (int j = i; !settled[j]; j = p[j]) {
                settled[j] = true;
                if (p[j] == i) break;
                swap(a, j, p[j]); swap(b, j, p[j]); swap(c, j, p[j]);
            }
        }
    }
    public static void permuteN(int[] p, int[]... as) {
        int n = p.length;
        boolean[] settled = new boolean[n];
        for (int i = 0; i < n; i++) {
            for (int j = i; !settled[j]; j = p[j]) {
                settled[j] = true;
                if (p[j] == i) break;
                for (int[] a : as) swap(a, j, p[j]);
            }
        }
    }
    public static int lowerBound(int[] sorted, int key) {
        int n = sorted.length;
        int l = -1, r = n;
        while (r - l > 1) {
            int m = (l + r) >> 1;
            if (sorted[m] < key) l = m;
            else r = m;
        }
        return r;
    }
    public static int upperBound(int[] sorted, int key) {
        int n = sorted.length;
        int l = -1, r = n;
        while (r - l > 1) {
            int m = (l + r) >> 1;
            if (sorted[m] <= key) l = m;
            else r = m;
        }
        return r;
    }
    public static int countOfRange(int[] sorted, int from, int to) {
        return lowerBound(sorted, to) - lowerBound(sorted, from);
    }
    public static String join(int[] a, String sep) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.length; i++) {
            sb.append(a[i]);
            if (i < a.length - 1) sb.append(sep);
        }
        return sb.toString();
    }
    public static String join(int[] a, char sep) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.length; i++) {
            sb.append(a[i]);
            if (i < a.length - 1) sb.append(sep);
        }
        return sb.toString();
    }
}