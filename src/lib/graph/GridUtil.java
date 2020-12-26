package lib.graph;

import java.util.Arrays;

import lib.base.Const;

public class GridUtil {
    private GridUtil() {}
    public static boolean[][] build(char[][] g, char passable) {
        final int H = g   .length;
        final int W = g[0].length;
        final boolean[][] G = new boolean[H][W];
        for (int i = 0; i < H; i++) for (int j = 0; j < W; j++) {
            G[i][j] = g[i][j] == passable;
        }
        return G;
    }
    public static boolean in(int r, int c, int h, int w) {
        return 0 <= r && r < h && 0 <= c && c < w;
    }
    public static boolean out(int r, int c, int h, int w) {
        return !in(r, c, h, w);
    }
    public static int[][] distance4(boolean[][] g, int sr, int sc) {
        final int H = g   .length;
        final int W = g[0].length;
        final int[][] d = new int[H][W];
        for (int i = 0; i < H; i++) Arrays.fill(d[i], -1);
        d[sr][sc] = 0;
        final int[] q = new int[H * W];
        int k = 0;
        q[k++] = sr * W + sc;
        while (k > 0) {
            int rc = q[--k];
            int r = rc / W, c = rc % W;
            for (int dr = 0; dr < 4; dr++) {
                int nr = r + Const.dy4[dr], nc = c + Const.dx4[dr];
                if (out(nr, nc, H, W) || d[nr][nc] >= 0) continue;
                d[nr][nc] = d[r][c] + 1;
                q[k++] = nr * W + nc;
            }
        }
        return d;
    }
    public static int[][] distance8(boolean[][] g, int sr, int sc) {
        final int H = g   .length;
        final int W = g[0].length;
        final int[][] d = new int[H][W];
        for (int i = 0; i < H; i++) Arrays.fill(d[i], -1);
        d[sr][sc] = 0;
        final int[] q = new int[H * W];
        int h = 0, t = 0;
        q[t++] = sr * W + sc;
        while (t > h) {
            int rc = q[h++];
            int r = rc / W, c = rc % W;
            for (int dr = 0; dr < 8; dr++) {
                int nr = r + Const.dy8[dr], nc = c + Const.dx8[dr];
                if (out(nr, nc, H, W) || d[nr][nc] >= 0) continue;
                d[nr][nc] = d[r][c] + 1;
                q[t++] = nr * W + nc;
            }
        }
        return d;
    }
    public static String toString(boolean[][] g) {
        StringBuilder sb = new StringBuilder();
        for (boolean[] gi : g) {
            for (boolean b : gi) {
                sb.append(b ? '.' : '#');
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}