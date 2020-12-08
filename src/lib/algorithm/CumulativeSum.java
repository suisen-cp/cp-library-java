package lib.algorithm;

/**
 * @author https://atcoder.jp/users/suisen
 */
public final class CumulativeSum {
    public static int[] build(int[] a) {
        int n = a.length;
        int[] s = new int[n + 1];
        for (int i = 1; i <= n; i++) s[i] = s[i - 1] + a[i - 1];
        return s;
    }
    public static int[][] build(int[][] a) {
        int n = a.length, m = a[0].length;
        int[][] s = new int[n + 1][m + 1];
        for (int i = 1; i <= n; i++) System.arraycopy(a[i - 1], 0, s[i], 1, m);
        for (int i = 1; i <= n; i++) for (int j = 1; j <= m; j++) {
            s[i][j] += s[i - 1][j];
        }
        for (int i = 1; i <= n; i++) for (int j = 1; j <= m; j++) {
            s[i][j] += s[i][j - 1];
        }
        return s;
    }
    public static int[][][] build(int[][][] a) {
        int n = a.length, m = a[0].length, l = a[0][0].length;
        int[][][] s = new int[n + 1][m + 1][l + 1];
        for (int i = 1; i <= n; i++) for (int j = 1; j <= m; j++) System.arraycopy(a[i - 1][j - 1], 0, s[i][j], 1, l);
        for (int i = 1; i <= n; i++) for (int j = 1; j <= m; j++) for (int k = 1; k <= l; k++) {
            s[i][j][k] += s[i - 1][j][k];
        }
        for (int i = 1; i <= n; i++) for (int j = 1; j <= m; j++) for (int k = 1; k <= l; k++) {
            s[i][j][k] += s[i][j - 1][k];
        }
        for (int i = 1; i <= n; i++) for (int j = 1; j <= m; j++) for (int k = 1; k <= l; k++) {
            s[i][j][k] += s[i][j][k - 1];
        }
        return s;
    }
    public static long[] build(long[] a) {
        int n = a.length;
        long[] s = new long[n + 1];
        for (int i = 1; i <= n; i++) s[i] = s[i - 1] + a[i - 1];
        return s;
    }
    public static long[][] build(long[][] a) {
        int n = a.length;
        int m = a[0].length;
        long[][] s = new long[n + 1][m + 1];
        for (int i = 1; i <= n; i++) System.arraycopy(a[i - 1], 0, s[i], 1, m);
        for (int i = 1; i <= n; i++) for (int j = 1; j <= m; j++) {
            s[i][j] += s[i - 1][j];
        }
        for (int i = 1; i <= n; i++) for (int j = 1; j <= m; j++) {
            s[i][j] += s[i][j - 1];
        }
        return s;
    }
    public static long[][][] build(long[][][] a) {
        int n = a.length, m = a[0].length, l = a[0][0].length;
        long[][][] s = new long[n + 1][m + 1][l + 1];
        for (int i = 1; i <= n; i++) for (int j = 1; j <= m; j++) System.arraycopy(a[i - 1][j - 1], 0, s[i][j], 1, l);
        for (int i = 1; i <= n; i++) for (int j = 1; j <= m; j++) for (int k = 1; k <= l; k++) {
            s[i][j][k] += s[i - 1][j][k];
        }
        for (int i = 1; i <= n; i++) for (int j = 1; j <= m; j++) for (int k = 1; k <= l; k++) {
            s[i][j][k] += s[i][j - 1][k];
        }
        for (int i = 1; i <= n; i++) for (int j = 1; j <= m; j++) for (int k = 1; k <= l; k++) {
            s[i][j][k] += s[i][j][k - 1];
        }
        return s;
    }
    public static int sum(int[] s, int l, int r) {
        return s[r] - s[l];
    }
    public static long sum(long[] s, int l, int r) {
        return s[r] - s[l];
    }
    public static int sum(int[][] s, int y1, int x1, int y2, int x2) {
        return s[y2][x2] - s[y1][x2] - s[y2][x1] + s[y1][x1];
    }
    public static long sum(long[][] s, int y1, int x1, int y2, int x2) {
        return s[y2][x2] - s[y1][x2] - s[y2][x1] + s[y1][x1];
    }
    public static int sum(int[][][] s, int i1, int j1, int k1, int i2, int j2, int k2) {
        int p0 = s[i2][j2][k2];
        int n1 = s[i1][j2][k2] + s[i2][j1][k2] + s[i2][j2][k1];
        int p2 = s[i1][j1][k2] + s[i2][j1][k1] + s[i1][j2][k1];
        int n3 = s[i1][j1][k1];
        return p0 - n1 + p2 - n3;
    }
    public static long sum(long[][][] s, int i1, int j1, int k1, int i2, int j2, int k2) {
        long p0 = s[i2][j2][k2];
        long n1 = s[i1][j2][k2] + s[i2][j1][k2] + s[i2][j2][k1];
        long p2 = s[i1][j1][k2] + s[i2][j1][k1] + s[i1][j2][k1];
        long n3 = s[i1][j1][k1];
        return p0 - n1 + p2 - n3;
    }
}