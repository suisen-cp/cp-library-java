package lib.datastructure;

/**
 * @author https://atcoder.jp/users/suisen
 */
public final class TwoDimensionsBinaryIndexedTree {
    private final long[][] dat;
    private final int n, m;
    public TwoDimensionsBinaryIndexedTree(final int n, final int m) {
        this.n = n;
        this.m = m;
        this.dat = new long[n + 1][m + 1];
    }
    public void build(final long[][] array) {
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                dat[i][j] += array[i - 1][j - 1];
                final int pi = i + (-i & i);
                final int pj = j + (-j & j);
                if (pi <= n && pj <= m) {
                    dat[pi][pj] += dat[i][j];
                } else if (pi <= n) {
                    dat[pi][j] += dat[i][j];
                } else if (pj <= m) {
                    dat[i][pj] += dat[i][j];
                }
            }
        }
    }
    public void fill(final long v) {
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                dat[i][j] += v;
                final int pi = i + (-i & i);
                final int pj = j + (-j & j);
                if (pi <= n && pj <= m) {
                    dat[pi][pj] += dat[i][j];
                } else if (pi <= n) {
                    dat[pi][j] += dat[i][j];
                } else if (pj <= m) {
                    dat[i][pj] += dat[i][j];
                }
            }
        }
    }
    public long get(final int i, final int j) {
        return sum(i - 1, i, j - 1, j);
    }
    public long sum(final int il, final int ir, final int jl, final int jr) {
        return sum(ir, jr) - sum(il, jl);
    }
    private long sum(int i, int j) {
        int ret = 0;
        for (; i > 0; i -= -i & i) for (; j > 0; j -= -j & j) ret += dat[i][j];
        return ret;
    }
    public void add(int i, int j, final long v) {
        for (; i <= n; i += -i & i) for (; j <= m; j += -j & j) dat[i][j] += v;
    }
    public void update(final int i, final int j, final long v) {
        add(i, j, v - get(i, j));
    }
}