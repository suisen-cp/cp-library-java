package lib.persistent;

import lib.util.collections.ints.IntTreeMap;

public class PartiallyPersistentUnionFind {
    final int[] par;
    final int[] time;
    final IntTreeMap<Integer>[] size;
    int t = 0;
    @SuppressWarnings("unchecked")
    public PartiallyPersistentUnionFind(int n) {
        this.par = new int[n];
        this.time = new int[n];
        this.size = new IntTreeMap[n];
        java.util.Arrays.fill(par, 1);
        java.util.Arrays.fill(time, Integer.MAX_VALUE);
        for (int i = 0; i < n; i++) {
            //noinspection Convert2Diamond
            size[i] = new IntTreeMap<Integer>();
            size[i].put(0, 1);
        }
    }
    public int root(int t, int x) {
        return time[x] > t ? x : root(t, par[x]);
    }
    public int unite(int x, int y) {
        t++;
        if ((x = root(t, x)) == (y = root(t, y))) return t;
        if (par[x] < par[y]) {int tmp = x; x = y; y = tmp;}
        size[x].put(t, par[x] += par[y]);
        par[y] = x;
        return time[y] = t;
    }
    public boolean same(int t, int x, int y) {
        return root(t, x) == root(t, y);
    }
    public int size(int t, int x) {
        return size[root(t, x)].floorEntry(t).getValue();
    }
}
