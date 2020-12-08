package lib.tree;

import lib.graph.SimpleEdge;

@SuppressWarnings("UnusedReturnValue")
public class TreeBuilder {
    private final int n;
    private int ptr = 0;
    private final int root;
    private final SimpleEdge[] edges;
    private final int[] count;
    public TreeBuilder(int n, int root) {
        this.n = n;
        this.root = root;
        this.edges = new SimpleEdge[n - 1];
        this.count = new int[n];
    }
    public TreeBuilder(int n) {
        this(n, 0);
    }
    public int addEdge(int u, int v, long cost) {
        edges[ptr] = new SimpleEdge(u, v, cost);
        count[u]++;
        count[v]++;
        return ptr++;
    }
    public int addEdge(int u, int v) {
        return addEdge(u, v, 1);
    }
    public SimpleEdge[] getEdges() {
        if (ptr < n - 1) throw new NullPointerException("too few edges.");
        return edges;
    }
    public Tree build() {
        int[][] adj = new int[n][];
        for (int i = 0; i < n; i++) {
            adj[i] = new int[count[i]];
        }
        for (SimpleEdge e : edges) {
            int u = e.from;
            int v = e.to;
            adj[u][--count[u]] = v;
            adj[v][--count[v]] = u;
        }
        return new Tree(n, root, adj);
    }
    public WeightedTree buildWeightedTree() {
        int[][] adj = new int[n][];
        long[][] cst = new long[n][];
        for (int i = 0; i < n; i++) {
            adj[i] = new int[count[i]];
            cst[i] = new long[count[i]];
        }
        for (SimpleEdge e : edges) {
            int u = e.from;
            int v = e.to;
            adj[u][--count[u]] = v;
            adj[v][--count[v]] = u;
            cst[u][count[u]] = e.cost;
            cst[v][count[v]] = e.cost;
        }
        return new WeightedTree(n, root, adj, cst);
    }
}