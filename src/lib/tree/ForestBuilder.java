package lib.tree;

import lib.graph.SimpleEdge;

public final class ForestBuilder {
    private final int[] dsu;
    private int t = 0;
    private final int[] time;
    private final int[] root;
    private final int n;
    private int ptr = 0;
    private final SimpleEdge[] edges;
    private final int[] count;
    public ForestBuilder(int n) {
        this.n = n;
        this.dsu = new int[n];
        java.util.Arrays.fill(dsu, -1);
        this.time = new int[n];
        this.root = new int[n];
        java.util.Arrays.fill(root, -1);
        this.edges = new SimpleEdge[n - 1];
        this.count = new int[n];
    }
    public void setRoot(int u) {
        root[leader(u)] = u;
        time[u] = ++t;
    }
    public void setRootIfAbsent(int u) {
        if (root[leader(u)] < 0) {
            root[leader(u)] = u;
            time[u] = ++t;
        }
    }
    public boolean isConnected(int u, int v) {
        return leader(u) == leader(v);
    }
    public int addEdgeIfDisconnected(int u, int v, long cost) {
        if (leader(u) != leader(v)) return addEdge(u, v);
        return -1;
    }
    public int addEdgeIfDisconnected(int u, int v) {
        return addEdgeIfDisconnected(u, v, 1);
    }
    public int addEdge(int u, int v, long cost) {
        if (leader(u) == leader(v)) throw new AssertionError();
        edges[ptr] = new SimpleEdge(u, v, cost);
        count[u]++;
        count[v]++;
        merge(u, v);
        return ptr++;
    }
    public int addEdge(int u, int v) {
        return addEdge(u, v, 1);
    }
    public Forest build() {
        int[][] adj = new int[n][];
        for (int i = 0; i < n; i++) adj[i] = new int[count[i]];
        for (int i = 0; i < ptr; i++) {
            int u = edges[i].from;
            int v = edges[i].to;
            adj[u][--count[u]] = v;
            adj[v][--count[v]] = u;
        }
        for (int i = 0; i < n; i++) root[i] = root[leader(i)];
        int[] idx = new int[n];
        int[] ids = new int[n];
        int[][] groups = groups(idx, ids);
        return new Forest(n, adj, groups, ids, idx, root);
    }

    public WeightedForest buildWeightedForest() {
        int[][] adj = new int[n][];
        long[][] cst = new long[n][];
        for (int i = 0; i < n; i++) {
            adj[i] = new int[count[i]];
            cst[i] = new long[count[i]];
        }
        for (int i = 0; i < ptr; i++) {
            int u = edges[i].from;
            int v = edges[i].to;
            adj[u][--count[u]] = v;
            adj[v][--count[v]] = u;
            cst[u][count[u]] = edges[i].cost;
            cst[v][count[v]] = edges[i].cost;
        }
        for (int i = 0; i < n; i++) root[i] = root[leader(i)];
        int[] idx = new int[n];
        int[] ids = new int[n];
        int[][] groups = groups(idx, ids);
        return new WeightedForest(n, adj, groups, ids, idx, root, cst);
    }

    private void merge(int x, int y) {
        if ((x = leader(x)) == (y = leader(y))) return;
        if (-dsu[x] < -dsu[y]) {int tmp = x; x = y; y = tmp;}
        dsu[x] += dsu[y];
        dsu[y] = x;
        int rx = root[x], ry = root[y];
        if (ry >= 0 && (rx < 0 || time[rx] < time[ry])) {
            root[x] = ry;
        }
    }
    private int leader(int x) {
        int p = dsu[x];
        return p < 0 ? x : (dsu[x] = leader(p));
    }
    private int[][] groups(int[] idx, int[] ids) {
        int[] cnt = new int[n];
        int groupNum = 0;
        for (int i = 0; i < n; i++) {
            if (dsu[i] < 0) cnt[ids[i] = groupNum++] = -dsu[i];
        }
        int[][] groups = new int[groupNum][];
        for (int j = 0; j < groupNum; j++) {
            groups[j] = new int[cnt[j]];
        }
        for (int i = 0; i < n; i++) {
            int j = ids[leader(i)];
            groups[ids[i] = j][idx[i] = --cnt[j]] = i;
        }
        return groups;
    }
}
