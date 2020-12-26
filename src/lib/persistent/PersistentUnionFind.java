package lib.persistent;

public class PersistentUnionFind {
    private PersistentIntArray data;
    public PersistentUnionFind(int n) {
        this.data = new PersistentIntArray(n, -1);
    }
    private PersistentUnionFind(PersistentIntArray data) {
        this.data = data;
    }
    public int root(int x) {
        int d = data.get(x);
        if (d < 0) return x;
        int r = root(d);
        data = data.set(x, r);
        return r;
    }
    public boolean isSame(int x, int y) {return root(x) == root(y);}
    public PersistentUnionFind unite(int x, int y) {
        x = root(x); y = root(y);
        if (x == y) return this;
        int xs = data.get(x), ys = data.get(y);
        if (xs > ys) {int tmp = x; x = y; y = tmp; tmp = xs; xs = ys; ys = tmp;}
        return new PersistentUnionFind(data.set(x, xs + ys).set(y, x));
    }
    public int size(int x) {return -data.get(root(x));}
    public boolean isRoot(int x) {return data.get(x) < 0;}
}