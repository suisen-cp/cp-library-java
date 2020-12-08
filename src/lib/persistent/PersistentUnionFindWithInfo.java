package lib.persistent;

import java.util.function.BinaryOperator;
import java.util.function.IntFunction;

public class PersistentUnionFindWithInfo<T> {
    final int N;
    PersistentIntArray Dat;
    final PersistentArray<T> Info;
    final BinaryOperator<T> op;

    public PersistentUnionFindWithInfo(int n, BinaryOperator<T> op, IntFunction<T> init) {
        this.N = n;
        this.Dat = new PersistentIntArray(n, -1);
        this.Info = new PersistentArray<>(n, init);
        this.op = op;
    }
    private PersistentUnionFindWithInfo(int n, PersistentIntArray dat, PersistentArray<T> info, BinaryOperator<T> op) {
        this.N = n;
        this.Dat = dat;
        this.Info = info;
        this.op = op;
    }
    public PersistentUnionFindWithInfo<T> merge(int x, int y) {
        x = leader(x); y = leader(y);
        if (x == y) return this;
        int xs = Dat.get(x);
        int ys = Dat.get(y);
        if (-ys > -xs) {int tmp = x; x = y; y = tmp;}
        PersistentIntArray newDat = Dat.set(x, xs + ys).set(y, x);
        PersistentArray<T> newInfo = Info.set(x, op.apply(Info.get(x), Info.get(y)));
        return new PersistentUnionFindWithInfo<>(N, newDat, newInfo, op);
    }
    public boolean same(int x, int y) {
        return leader(x) == leader(y);
    }
    public T getInfo(int x) {
        return Info.get(leader(x));
    }
    public int leader(int x) {
        int d = Dat.get(x);
        if (d < 0) return x;
        int r = leader(x);
        Dat = Dat.set(x, r);
        return r;
    }
    public int size(int x) {
        return -Dat.get(leader(x));
    }
    public int[][] groups() {
        int[] cmp = new int[N];
        int[] cnt = new int[N];
        int groupNum = 0;
        for (int i = 0; i < N; i++) {
            if (Dat.get(i) < 0) {
                cnt[cmp[i] = groupNum++] = -Dat.get(i);
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
