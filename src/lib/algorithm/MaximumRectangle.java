package lib.algorithm;

import lib.util.collections.Deque;

public class MaximumRectangle {
    public static int max(boolean[][] g) {
        final int h = g.length;
        final int w = g[0].length;
        int[][] hst = new int[h][w];
        for (int j = 0; j < w; j++) hst[h - 1][j] = g[h - 1][j] ? 1 : 0;
        for (int i = h - 2; i >= 0; i--) for (int j = 0; j < w; j++) {
            hst[i][j] = g[i][j] ? hst[i + 1][j] + 1 : 0;
        }
        int max = 0;
        for (int i = 0; i < h; i++) max = Math.max(max, histRectMax(hst[i]));
        return max;
    }

    public static int histRectMax(int[] h) {
        final int n = h.length;
        Deque<int[]> s = new Deque<>();
        int max = 0;
        for (int j = 0; j < n; j++) {
            int hj = h[j];
            if (s.size() == 0) {
                s.addLast(new int[]{j, hj});
            } else {
                int[] top = s.getLast();
                if (top[1] < hj) {
                    s.addLast(new int[]{j, hj});
                } else if (top[1] > hj) {
                    while (s.size() > 0 && s.getLast()[1] >= hj) {
                        top = s.removeLast();
                        max = Math.max(max, top[1] * (j - top[0]));
                    }
                    s.addLast(new int[]{top[0], hj});
                }
            }
        }
        int[] top;
        while (s.size() > 0) {
            top = s.removeLast();
            max = Math.max(max, top[1] * (n - top[0]));
        }
        return max;
    }

    public static long histRectMax(long[] h) {
        final int n = h.length;
        Deque<long[]> s = new Deque<>();
        long max = 0;
        for (int j = 0; j < n; j++) {
            long hj = h[j];
            if (s.size() == 0) {
                s.addLast(new long[]{j, hj});
            } else {
                long[] top = s.getLast();
                if (top[1] < hj) {
                    s.addLast(new long[]{j, hj});
                } else if (top[1] > hj) {
                    while (s.size() > 0 && s.getLast()[1] >= hj) {
                        top = s.removeLast();
                        max = Math.max(max, top[1] * (j - top[0]));
                    }
                    s.addLast(new long[]{top[0], hj});
                }
            }
        }
        long[] top;
        while (s.size() > 0) {
            top = s.removeLast();
            max = Math.max(max, top[1] * (n - top[0]));
        }
        return max;
    }
}