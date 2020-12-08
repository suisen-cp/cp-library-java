package lib.datastructure.ints;

import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;

public class DisjointSetUnionWithIntInfo {
    final int N;
    final int[] Dat;
    final int[] Info;
    final IntBinaryOperator op;

    public DisjointSetUnionWithIntInfo(int n, IntBinaryOperator op, IntUnaryOperator init) {
        this.N = n;
        this.Dat = new int[N];
        java.util.Arrays.fill(Dat, -1);
        this.Info = new int[n];
        java.util.Arrays.setAll(Info, init);
        this.op = op;
    }
    public int merge(int x, int y) {
        x = leader(x); y = leader(y);
        if (x == y) return x;
        if (-Dat[y] > -Dat[x]) {int tmp = x; x = y; y = tmp;}
        Dat[x] += Dat[y];
        Dat[y] = x;
        Info[x] = op.applyAsInt(Info[x], Info[y]);
        return x;
    }
    public boolean same(int x, int y) {
        return leader(x) == leader(y);
    }
    public void setInfo(int x, int info) {
        Info[leader(x)] = info;
    }
    public int getInfo(int x) {
        return Info[leader(x)];
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
}
