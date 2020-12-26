package lib.tree;

import java.util.Arrays;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;

import lib.util.collections.ints.IntArrayList;
import lib.util.collections.ints.IntDeque;

public class MergeTreeFoldPath {
    private final IntBinaryOperator op;
    private final IntUnaryOperator inv;
    private final int e;
    private final IntArrayList[] ancestor;
    private final IntArrayList[] edges;
    private final int[] par;
    private final int[] dep;
    private final int[] uf;
    private final int[] sum;
    private final int[] w;
    public MergeTreeFoldPath(int n, int[] w, IntBinaryOperator op, IntUnaryOperator inv, int e) {
        this.op = op;
        this.inv = inv;
        this.e = e;
        this.ancestor = new IntArrayList[n];
        this.edges = new IntArrayList[n];
        this.par = new int[n];
        this.dep = new int[n];
        this.sum = new int[n];
        this.w = w;
        System.arraycopy(w, 0, sum, 0, n);
        for (int i = 0; i < n; i++) {
            ancestor[i] = new IntArrayList();
            edges[i] = new IntArrayList();
        }
        this.uf = new int[n];
        Arrays.fill(uf, -1);
    }
    public void addEdgeIfDisconnected(int u, int v) {
        if (connected(u, v)) return;
        if (size(u) < size(v)) {
            addEdgeIfDisconnected(v, u);
            return;
        }
        par[v] = u;
        dep[v] = dep[u] + 1;
        sum[v] = op.applyAsInt(sum[u], w[v]);
        IntDeque q = new IntDeque(size(v));
        q.addLast(v);
        while (q.size() > 0) {
            int x = q.removeFirst();
            ancestor[x].clear();
            int px = par[x];
            ancestor[x].add(px);
            for (int i = 0, s = px; i < ancestor[s].size(); i++) {
                s = ancestor[s].get(i);
                ancestor[x].add(s);
            }
            edges[x].iterator().forEachRemaining((int y) -> {
                if (y != px) {
                    par[y] = x;
                    dep[y] = dep[x] + 1;
                    sum[y] = op.applyAsInt(sum[x], w[y]);
                    q.addLast(y);
                }
            });
        }
        edges[u].add(v);
        edges[v].add(u);
        unite(u, v);
    }
    public int queryForVertex(int u, int v) {
        if (!connected(u, v)) return e;
        int a = lca(u, v);
        int ua = op.applyAsInt(sum[u], inv.applyAsInt(sum[a]));
        int va = op.applyAsInt(sum[v], inv.applyAsInt(sum[a]));
        return op.applyAsInt(w[a], op.applyAsInt(ua, va));
    }
    public int root(final int x) {return uf[x] < 0 ? x : (uf[x] = root(uf[x]));}
    public boolean connected(final int x, final int y) {return root(x) == root(y);}
    public int size(final int x) {return -uf[root(x)];}
    public boolean isRoot(final int x) {return uf[x] < 0;}
    private void unite(int x, int y) {
        x = root(x); y = root(y);
        uf[x] += uf[y];
        uf[y] = x;
    }
    private int lca(int u, int v) {
        if (dep[u] > dep[v]) return lca(v, u);
        v = up(v, dep[v] - dep[u]);
        if (u == v) return u;
        int k = ancestor[u].size() - 1;
        while (k >= 0) {
            int uk = ancestor[u].get(k);
            int vk = ancestor[v].get(k);
            if (uk != vk) {u = uk; v = vk;}
            k = Math.min(k, ancestor[u].size()) - 1;
        }
        return par[u];
    }
    private int up(int v, int step) {
        while (step > 0) {
            v = ancestor[v].get(Integer.numberOfTrailingZeros(step));
            step ^= -step & step;
        }
        return v;
    }
}