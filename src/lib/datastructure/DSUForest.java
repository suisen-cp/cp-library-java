package lib.datastructure;

import lib.tree.Forest;
import lib.tree.ForestBuilder;

public class DSUForest {
    final int N;
    final int[] Dat;

    final int M;
    final ForestBuilder fb;
    final int[] SubtreeRoot;
    final int[] time;
    int now = 0;
    int idx;
    public DSUForest(int n) {
        this.N = n;
        this.Dat = new int[N];
        java.util.Arrays.fill(Dat, -1);

        this.M = 2 * N - 1;
        this.fb = new ForestBuilder(M);
        this.SubtreeRoot = new int[N];
        this.time = new int[M];
        this.idx = N;
        java.util.Arrays.setAll(SubtreeRoot, i -> i);
        java.util.Arrays.fill(time, N, M, Integer.MAX_VALUE);
    }
    public int merge(int x, int y) {
        now++;
        x = leader(x); y = leader(y);
        if (x == y) return now;
        if (-Dat[x] < -Dat[y]) {int tmp = x; x = y; y = tmp;}
        Dat[x] += Dat[y];
        Dat[y] = x;
        fb.addEdge(SubtreeRoot[x], idx);
        fb.addEdge(SubtreeRoot[y], idx);
        SubtreeRoot[x] = idx;
        return time[idx++] = now;
    }
    public int addedTime(int node) {
        return time[node];
    }
    public boolean same(int x, int y) {
        return leader(x) == leader(y);
    }
    public int leader(int x) {
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
    public Forest buildForest() {
        return fb.build();
    }
}
