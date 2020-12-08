package lib.datastructure;

import java.util.Arrays;

public final class DisjointSetUnion {
    final int N;
    final int[] Dat;
    public DisjointSetUnion(int n) {
        this.N = n;
        this.Dat = new int[N];
        clear();
    }
    public int merge(int x, int y) {
        x = leader(x);
        y = leader(y);
        if (x == y) return x;
        if (-Dat[y] > -Dat[x]) {
            int tmp = x; x = y; y = tmp;
        }
        Dat[x] += Dat[y];
        Dat[y] = x;
        return x;
    }
    public boolean same(int x, int y) {
        return leader(x) == leader(y);
    }
    public int leader(int x) {
        if (x < 0 || x >= N) {
            throw new IndexOutOfBoundsException(
                String.format("Index %d out of bounds for the length %d.", x, N)
            );
        }
        if (Dat[x] < 0) return x;
        return Dat[x] = leader(Dat[x]);
    }
    public int size(int x) {
        return -Dat[leader(x)];
    }
    public int[][] groups() {
        int[] cmp = new int[N];
        int[] cnt = new int[N];
        int groupNum = 0;
        for (int i = 0; i < N; i++) {
            if (Dat[i] < 0) {
                cnt[cmp[i] = groupNum++] = -Dat[i];
            }
        }
        int[][] groups = new int[groupNum][];
        for (int j = 0; j < groupNum; j++) {
            groups[j] = new int[cnt[j]];
        }
        for (int i = 0; i < N; i++) {
            int j = cmp[leader(i)];
            groups[j][--cnt[j]] = i;
        }
        return groups;
    }
    public void clear() {
        Arrays.fill(Dat, -1);
    }
}